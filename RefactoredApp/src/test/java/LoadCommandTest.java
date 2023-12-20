import javax.swing.JEditorPane;

import org.alexxarisis.controller.LatexEditorController;
import org.alexxarisis.model.DocumentManager;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.view.LatexEditorView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadCommandTest {

	private LatexEditorView latexEditorView;
	private DocumentManager documentManager;
	@TempDir
	private File tempDir;

	@BeforeEach
	public void setup() {
		latexEditorView = new LatexEditorView();
		VersionsManager versionsManager = VersionsManager.getInstance();
		versionsManager.setLatexEditorView(latexEditorView);
		LatexEditorController controller = new LatexEditorController();
		latexEditorView.setController(controller);
		latexEditorView.setEditorPane(new JEditorPane());
		
		documentManager = new DocumentManager();
	}
	
	@Test
	@DisplayName("Load latex file")
	public void loadFile() {
		String filepath = tempDir.getAbsolutePath() + "loadTest1.tex";
		String type = "reportTemplate";
		
		createDocument(type);
		saveDocument(filepath);
		latexEditorView.getController().enact("load");
		
		String contents = documentManager.createDocument(type).getContents();
		String version = "0";
		assertEquals(contents, latexEditorView.getCurrentDocument().getContents());
		assertEquals(version, latexEditorView.getCurrentDocument().getVersionID());
	}
	
	@Test
	@DisplayName("Load html file")
	public void loadHtmlFile() {
		String filepath = tempDir.getAbsolutePath() + "loadTest2.html";
		String type = "emptyTemplate";
		
		createDocument(type);
		addLatexContents();
		saveDocument(filepath);
		latexEditorView.getController().enact("load");
		
		assertEquals(getLatexContents().trim(), latexEditorView.getCurrentDocument().getContents().trim());
	}
	
	private void createDocument(String type) {
		latexEditorView.getController().setType(type);
		latexEditorView.getController().enact("create");
		latexEditorView.getController().setText(
				documentManager.createDocument(type).getContents());
		latexEditorView.getController().enact("edit");
	}
	
	private void addLatexContents() {
		enactAddLatexCommandWithSpecificContent("chapter");
		enactAddLatexCommandWithSpecificContent("section");
		enactAddLatexCommandWithSpecificContent("subsection");
		enactAddLatexCommandWithSpecificContent("subsubsection");
		enactAddLatexCommandWithSpecificContent("itemize");
		enactAddLatexCommandWithSpecificContent("enumerate");
		enactAddLatexCommandWithSpecificContent("table");
		enactAddLatexCommandWithSpecificContent("figure");
		latexEditorView.getController().enact("edit");
	}
	
	private void enactAddLatexCommandWithSpecificContent(String type) {
		latexEditorView.getController().setContentType(type);
		latexEditorView.getController().enact("addLatex");
	}
	
	private void saveDocument(String filepath) {
		latexEditorView.getController().setFilename(filepath);
		latexEditorView.getController().enact("save");
	}
	
	private String getLatexContents() {
		return "\\begin{figure}\n"
				+ "\\includegraphics[width=...,height=...]{...}\n"
				+ "\\caption{....}\\label{...}\n"
				+ "\\end{figure}\n"
				+ "\\begin{table}\n"
				+ "\\caption{....}\\label{...}\n"
				+ "\\begin{tabular}{|c|c|c|}\n"
				+ "\\hline\n"
				+ "... &...&...\\\\\n"
				+ "... &...&...\\\\\n"
				+ "... &...&...\\\\\n"
				+ "\\hline\n"
				+ "\\end{tabular} \n"
				+ "\\end{table}\n"
				+ "\\begin{enumerate}\n"
				+ "\\item ...\n"
				+ "\\item ...\n"
				+ "\\end{enumerate}\n"
				+ "\\begin{itemize}\n"
				+ "\\item ...\n"
				+ "\\item ...\n"
				+ "\\end{itemize}\n"
				+ "\\subsubsection{...}\n"
				+ "\\subsection{...}\n"
				+ "\\section{...}\n"
				+ "\\chapter{...}";
	}
}