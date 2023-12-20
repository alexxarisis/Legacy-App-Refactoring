import org.alexxarisis.controller.LatexEditorController;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.view.LatexEditorView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateCommandTest {

	private LatexEditorView latexEditorView;
	
	@BeforeEach
	public void setup() {
		latexEditorView = new LatexEditorView();
		VersionsManager versionsManager = VersionsManager.getInstance();
		versionsManager.setLatexEditorView(latexEditorView);
		LatexEditorController controller = new LatexEditorController();
		latexEditorView.setController(controller);
	}
	
	@Test
	@DisplayName("Create: Empty template")
	public void createEmptyTemplate() {
		latexEditorView.getController().setType("emptyTemplate");
		latexEditorView.getController().enact("create");
		assertEquals("", latexEditorView.getCurrentDocument().getContents());
	}

	@Test
	@DisplayName("Create: Article template")
	public void createArticleTemplate() {
		latexEditorView.getController().setType("articleTemplate");
		latexEditorView.getController().enact("create");
		assertEquals(getContents("articleTemplate"), latexEditorView.getCurrentDocument().getContents());
	}
	
	@Test
	@DisplayName("Create: Book template")
	public void createBookTemplate() {
		latexEditorView.getController().setType("bookTemplate");
		latexEditorView.getController().enact("create");
		assertEquals(getContents("bookTemplate"), latexEditorView.getCurrentDocument().getContents());
	}
	
	@Test
	@DisplayName("Create: Letter template")
	public void createLetterTemplate() {
		latexEditorView.getController().setType("letterTemplate");
		latexEditorView.getController().enact("create");
		assertEquals(getContents("letterTemplate"), latexEditorView.getCurrentDocument().getContents());
	}
	
	@Test
	@DisplayName("Create: Report template")
	public void createReportTemplate() {
		latexEditorView.getController().setType("reportTemplate");
		latexEditorView.getController().enact("create");
		assertEquals(getContents("reportTemplate"), latexEditorView.getCurrentDocument().getContents());
	}
	
	private String getContents(String type) {
		if (type.equals("articleTemplate")) {
			return "\\documentclass[11pt,twocolumn,a4paper]{article}\n\n"+

					"\\begin{document}\n"+
					"\\title{Article: How to Structure a LaTeX Document}\n"+
					"\\author{Author1 \\and Author2 \\and ...}\n"+
					"\\date{\\today}\n\n"+

					"\\maketitle\n\n"+

					"\\section{Section Title 1}\n\n"+

					"\\section{Section Title 2}\n\n"+

					"\\section{Section Title.....}\n\n"+

					"\\section{Conclusion}\n\n"+

					"\\section*{References}\n\n"+

					"\\end{document}\n";
		}
		else if(type.equals("bookTemplate")) {
			return "\\documentclass[11pt,a4paper]{book}\n\n"+

					"\\begin{document}\n"+
					"\\title{Book: How to Structure a LaTeX Document}\n"+
					"\\author{Author1 \\and Author2 \\and ...}\n"+
					"\\date{\\today}\n\n"+

					"\\maketitle\n\n"+

					"\\frontmatter\n\n"+

					"\\chapter{Preface}\n"+
					"% ...\n\n"+

					"\\mainmatter\n"+
					"\\chapter{First chapter}\n"+
					"\\section{Section Title 1}\n"+
					"\\section{Section Title 2}\n\n"+

					"\\section{Section Title.....}\n\n"+

					"\\chapter{....}\n\n"+

					"\\chapter{Conclusion}\n\n"+

					"\\chapter*{References}\n\n\n"+


					"\\backmatter\n"+
					"\\chapter{Last note}\n\n"+

					"\\end{document}\n";
		}
		else if(type.equals("letterTemplate")) {
			return "\\documentclass{letter}\n"+
					"\\usepackage{hyperref}\n"+
					"\\signature{Sender's Name}\n"+
					"\\address{Sender's address...}\n"+
					"\\begin{document}\n\n"+

					"\\begin{letter}{Destination address....}\n"+
					"\\opening{Dear Sir or Madam:}\n\n"+

					"I am writing to you .......\n\n\n"+


					"\\closing{Yours Faithfully,}\n"+

					"\\ps\n\n"+

					"P.S. text .....\n\n"+

					"\\encl{Copyright permission form}\n\n"+

					"\\end{letter}\n"+
					"\\end{document}\n";
		}
		else {
			return "\\documentclass[11pt,a4paper]{report}\n\n"+

					"\\begin{document}\n"+
					"\\title{Report Template: How to Structure a LaTeX Document}\n"+
					"\\author{Author1 \\and Author2 \\and ...}\n"+
					"\\date{\\today}\n"+
					"\\maketitle\n\n"+

					"\\begin{abstract}\n"+
					"Your abstract goes here...\n"+
					"...\n"+
					"\\end{abstract}\n\n"+

					"\\chapter{Introduction}\n"+
					"\\section{Section Title 1}\n"+
					"\\section{Section Title 2}\n"+
					"\\section{Section Title.....}\n\n"+

					"\\chapter{....}\n\n"+

					"\\chapter{Conclusion}\n\n\n"+


					"\\chapter*{References}\n\n"+

					"\\end{document}\n";
		}
	}
}
