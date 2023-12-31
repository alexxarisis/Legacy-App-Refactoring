import javax.swing.JEditorPane;

import org.alexxarisis.controller.LatexEditorController;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.view.LatexEditorView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DisableVersionsManagementCommandTest {

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
	@DisplayName("Disable from volatile")
	public void disableVolatile() {
		enableStrategy("volatileStrategy");
		disableStrategy();
        assertFalse(versionsManager.isEnabled());
	}
	
	@Test
	@DisplayName("Disable from stable")
	public void disableStable() {
		enableStrategy("stableStrategy");
		disableStrategy();
        assertFalse(versionsManager.isEnabled());
	}

	private void enableStrategy(String strategy) {
		versionsManager.setStrategyType(strategy);
		latexEditorView.getController().enact("enableVersionsManagement");
	}
	
	private void disableStrategy() {
		latexEditorView.getController().enact("disableVersionsManagement");
	}
}
