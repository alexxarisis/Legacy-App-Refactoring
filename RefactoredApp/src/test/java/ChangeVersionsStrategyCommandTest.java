import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;

import org.alexxarisis.controller.LatexEditorController;
import org.alexxarisis.model.Document;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.model.strategies.VersionsStrategy;
import org.alexxarisis.view.LatexEditorView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeVersionsStrategyCommandTest {

	private LatexEditorView latexEditorView;
	private VersionsManager versionsManager;
	
	@BeforeEach
	public void setup() {
		latexEditorView = new LatexEditorView();
		versionsManager = VersionsManager.getInstance();
		versionsManager.setLatexEditorView(latexEditorView);
		LatexEditorController controller = new LatexEditorController();
		latexEditorView.setController(controller);
		latexEditorView.setEditorPane(new JEditorPane());

		latexEditorView.getController().setType("emptyTemplate");
		latexEditorView.getController().enact("create");
	}
	
	@Test
	@DisplayName("Stable to Volatile")
	public void changeVersionsStrategyFromStableToVolatile() {
		enableStrategy("stableStrategy");
		swapStrategies("volatileStrategy");
		assertEquals("volatileStrategy", versionsManager.getStrategyType());
	}
	
	@Test
	@DisplayName("Stable to Volatile: Copied History")
	public void transferDocumentEvolutionHistoryToVolatile() {
		enableStrategy("stableStrategy");
		int versions = 5;
		makeVersions(versions);
		
		List<Document> before = versionsManager.getStrategy().getEntireHistory();
		swapStrategies("volatileStrategy");
		// for some reason, after is empty when it isn't?
		List<Document> after = versionsManager.getStrategy().getEntireHistory();
		for (int i=0; i < versions; i++) {
			assertEquals(before.get(i).getVersionID(), after.get(i).getVersionID());
			assertEquals(before.get(i).getContents(), after.get(i).getContents());
		}
	}
	
	@Test
	@DisplayName("Volatile to Stable")
	public void changeVersionsStrategyFromVolatileToStable() {
		enableStrategy("volatileStrategy");
		swapStrategies("stableStrategy");
		assertEquals("stableStrategy", versionsManager.getStrategyType());
	}
	
	@Test
	@DisplayName("Volatile to Stable: Copied History")
	public void transferDocumentEvolutionHistoryToStable() {
		enableStrategy("volatileStrategy");
		int versions = 5;
		makeVersions(versions);
		
		List<Document> before = versionsManager.getStrategy().getEntireHistory();
		swapStrategies("stableStrategy");
		
		List<Document> after = versionsManager.getStrategy().getEntireHistory();
		for (int i=0; i < versions; i++) {
			assertEquals(before.get(i).getVersionID(), after.get(i).getVersionID());
			assertEquals(before.get(i).getContents(), after.get(i).getContents());
		}
	}
	
	private void enableStrategy(String strategy) {
		versionsManager.setStrategyType(strategy);
		latexEditorView.getController().enact("enableVersionsManagement");
	}
	
	private void swapStrategies(String strategy) {
		versionsManager.setStrategyType(strategy);
		latexEditorView.getController().enact("changeVersionsStrategy");
	}
	
	private void makeVersions(int totalVersions) {
		List<String> versions = makeVersionsText(totalVersions);
		saveVersions(versions);
		
		VersionsStrategy strategy = versionsManager.getStrategy();
		assertEquals(totalVersions, strategy.getEntireHistory().size());
		
		List<Document> docs = strategy.getEntireHistory();
		for (int i=0; i < totalVersions; i++)
			assertEquals(versions.get(i), docs.get(i).getContents());
	}
	
	private void saveVersions(List<String> versions) {
        for (String version : versions) {
            latexEditorView.getController().setText(version);
            latexEditorView.getController().enact("edit");
        }
	}
	
	private List<String> makeVersionsText(int totalVersions) {
		List<String> versions = new ArrayList<>();
		for (int i=1; i <= totalVersions; i++)
			versions.add("text" + i);
		return versions;
	}
}