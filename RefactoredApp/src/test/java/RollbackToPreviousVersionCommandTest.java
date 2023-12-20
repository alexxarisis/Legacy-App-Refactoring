import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;

import org.alexxarisis.controller.LatexEditorController;
import org.alexxarisis.model.Document;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.view.LatexEditorView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RollbackToPreviousVersionCommandTest {
	private VersionsManager versionsManager;
	private LatexEditorView latexEditorView;
	
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
	@DisplayName("Rollback with volatile")
	public void rollbackVolatile() {
		enableStrategy("volatileStrategy");
		int versions = 5;
		makeVersions(versions);
		
		for (int i=1; i < versions; i++) {
			latexEditorView.getController().enact("rollbackToPreviousVersion");
			List<Document> after = versionsManager.getStrategy().getEntireHistory();
			Document docAfter = latexEditorView.getCurrentDocument();
			
			int currentVersion = versions-i;
			assertEquals(currentVersion, after.size());
			assertEquals("" + currentVersion, docAfter.getVersionID());
			assertEquals("text" + currentVersion, docAfter.getContents());
		}
	}
	
	@Test
	@DisplayName("Rollback with stable")
	public void rollbackStable() {
		enableStrategy("stableStrategy");
		int versions = 5;
		makeVersions(versions);
		
		for (int i=1; i < versions; i++) {
			latexEditorView.getController().enact("rollbackToPreviousVersion");
			List<Document> after = versionsManager.getStrategy().getEntireHistory();
			Document docAfter = latexEditorView.getCurrentDocument();
			
			int currentVersion = versions-i;
			assertEquals(currentVersion, after.size());
			assertEquals("" + currentVersion, docAfter.getVersionID());
			assertEquals("text" + currentVersion, docAfter.getContents());
		}
	}
	
	private void enableStrategy(String strategy) {
		removeAnyVersionsRemaining();
		versionsManager.setStrategyType(strategy);
		latexEditorView.getController().enact("enableVersionsManagement");
	}
	
	private void removeAnyVersionsRemaining() {
		// JUnit's classloader loaded the singleton only one time,
		// and it interfered with the tests.
		// In particular, when enabling the strategy on the 2nd test,
		// there would already be a version (Version 1) in the strategy's history,
		// because it did not initialize the Singleton again, as it should.
		// So the new strategy had the previous version 1, plus the new ones
		// and threw an error.
		// One way to solve it was to use reflection and change the fields etc.
		// A more elegant (let's say) one, is to remove all previous versions
		// as shown below.
		while (!versionsManager.getStrategy().getEntireHistory().isEmpty())
			versionsManager.getStrategy().removeVersion();
	}
	
	private void makeVersions(int totalVersions) {
		List<String> versions = makeVersionsText(totalVersions);
		saveVersions(versions);
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
