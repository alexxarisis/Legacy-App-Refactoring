import javax.swing.JEditorPane;

import org.alexxarisis.controller.LatexEditorController;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.view.LatexEditorView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditCommandTest {
	
	private LatexEditorView latexEditorView;
	private JEditorPane textPanel;
	
	@BeforeEach
	public void setup() {
		latexEditorView = new LatexEditorView();
		VersionsManager versionsManager = VersionsManager.getInstance();
		versionsManager.setLatexEditorView(latexEditorView);
		LatexEditorController controller = new LatexEditorController();
		latexEditorView.setController(controller);
		textPanel = new JEditorPane();
		latexEditorView.setEditorPane(textPanel);
	}
	
	@Test
	@DisplayName("Edit an empty template")
	public void editEmptyTemplate() {
		latexEditorView.getController().setType("emptyTemplate");
		latexEditorView.getController().enact("create");
		
		String before = "test";

		textPanel.setText(before);
		latexEditorView.getController().setText(textPanel.getText());
		latexEditorView.getController().enact("edit");
		
		String after = latexEditorView.getCurrentDocument().getContents();
		assertEquals(before, after);
	}
	
	@Test
	@DisplayName("Edit a book template")
	public void editBookTemplate() {
		latexEditorView.getController().setType("bookTemplate");
		latexEditorView.getController().enact("create");
		
		String before = "test";

		textPanel.setText(before);
		latexEditorView.getController().setText(textPanel.getText());
		latexEditorView.getController().enact("edit");
		
		String after = latexEditorView.getCurrentDocument().getContents();
		assertEquals(before, after);
	}
}
