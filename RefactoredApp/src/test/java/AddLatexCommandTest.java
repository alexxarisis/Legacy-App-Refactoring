import java.util.HashMap;

import javax.swing.JEditorPane;

import org.alexxarisis.model.ContentsManager;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.view.LatexEditorView;
import org.alexxarisis.controller.LatexEditorController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddLatexCommandTest {

	private LatexEditorView latexEditorView;
	private HashMap<String, String> contents;

	@BeforeEach
    public void setup() {
		latexEditorView = new LatexEditorView();
		VersionsManager versionsManager = VersionsManager.getInstance();
		versionsManager.setLatexEditorView(latexEditorView);
		LatexEditorController controller = new LatexEditorController();
		latexEditorView.setController(controller);
		latexEditorView.setEditorPane(new JEditorPane());
		
		ContentsManager contentsManager = new ContentsManager();
		contents = contentsManager.getAllContents();
		latexEditorView.getController().setType("emptyTemplate");
		latexEditorView.getController().enact("create");
    }
	
	@Test
	@DisplayName("Add 'chapter'")
	void addLatexCommandChapterTest() {
		enactAddLatexCommandWithSpecificContent("chapter");
		assertGuiTextVsPresetText();
	}
	
	@Test
	@DisplayName("Add 'section'")
	void addLatexCommandSectionTest() {
		enactAddLatexCommandWithSpecificContent("section");
		assertGuiTextVsPresetText();
	}
	
	@Test
	@DisplayName("Add 'subsection'")
	void addLatexCommandSubsectionTest() {
		enactAddLatexCommandWithSpecificContent("subsection");
		assertGuiTextVsPresetText();
	}
	
	@Test
	@DisplayName("Add 'subsubsection'")
	void addLatexCommandSubsubsectionTest() {
		enactAddLatexCommandWithSpecificContent("subsubsection");
		assertGuiTextVsPresetText();
	}
	
	@Test
	@DisplayName("Add 'enumerate'")
	void addLatexCommandEnumerateTest() {
		enactAddLatexCommandWithSpecificContent("enumerate");
		assertGuiTextVsPresetText();
	}
	
	@Test
	@DisplayName("Add 'itemize'")
	void addLatexCommandItemizeTest() {
		enactAddLatexCommandWithSpecificContent("itemize");
		assertGuiTextVsPresetText();
	}
	
	@Test
	@DisplayName("Add 'table'")
	void addLatexCommandTableTest() {
		enactAddLatexCommandWithSpecificContent("table");
		assertGuiTextVsPresetText();
	}
	
	@Test
	@DisplayName("Add 'figure'")
	void addLatexCommandFigureTest() {
		enactAddLatexCommandWithSpecificContent("figure");
		assertGuiTextVsPresetText();
	}
	
	private void enactAddLatexCommandWithSpecificContent(String type) {
		latexEditorView.setEditorPane(new JEditorPane());
		latexEditorView.getController().setContentType(type);
		latexEditorView.getController().enact("addLatex");
	}
	
	private void assertGuiTextVsPresetText() {
		String contentType = latexEditorView.getController().getContentType();
		String presetText = contents.get(contentType);
		String guiText = latexEditorView.getCurrentDocument().getContents();
		
		assertEquals(presetText, guiText);
	}
}