import javax.swing.JEditorPane;

import org.alexxarisis.controller.LatexEditorController;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.model.strategies.StableVersionsStrategy;
import org.alexxarisis.model.strategies.VolatileVersionsStrategy;
import org.alexxarisis.view.LatexEditorView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnableVersionsManagementCommandTest {

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
	}
	
	@Test
	@DisplayName("Enable volatile strategy")
	public void enableVolatileStrategy() {
		enableStrategy("volatileStrategy");
		assertTrue(versionsManager.isEnabled());
		assertEquals("volatileStrategy", versionsManager.getStrategyType());
		assertTrue(versionsManager.getStrategy() instanceof VolatileVersionsStrategy);
	}
	
	@Test
	@DisplayName("Enable stable strategy")
	public void enableStableStrategy() {
		enableStrategy("stableStrategy");
		assertTrue(versionsManager.isEnabled());
		assertEquals("stableStrategy", versionsManager.getStrategyType());
		assertTrue(versionsManager.getStrategy() instanceof StableVersionsStrategy);
	}
	
	private void enableStrategy(String strategy) {
		versionsManager.setStrategyType(strategy);
		latexEditorView.getController().enact("enableVersionsManagement");
	}
}
