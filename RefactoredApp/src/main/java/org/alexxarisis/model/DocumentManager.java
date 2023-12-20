package org.alexxarisis.model;

import java.util.HashMap;
import java.util.Map;

public class DocumentManager {
	
	private final HashMap<String, Document> templates;
	
	public DocumentManager() {
		templates = new HashMap<>();
		createAllTemplateDocuments();
	}
	
	private void createAllTemplateDocuments() {
		for (Map.Entry<String, String> template : getAllTemplates().entrySet()) {
			String type = template.getKey();
			String content = template.getValue();
			addToTemplates(type, setupDocument(content));
		}
	}
	
	private void addToTemplates(String type, Document document) {
		templates.put(type, document);
	}
	
	private Document setupDocument(String content) {
		Document document = new Document();
		document.setContents(content);
		return document;
	}
	
	public Document createDocument(String type) {
		return templates.get(type).clone();
	}
	
	public String getContents(String type) {
		return templates.get(type).getContents();
	}
	
	public HashMap<String, String> getAllTemplates() {
		HashMap<String, String> allTemplates = new HashMap<>();
		allTemplates.put("emptyTemplate", "");
		allTemplates.put("articleTemplate",
                """
                \\documentclass[11pt,twocolumn,a4paper]{article}

                \\begin{document}
                \\title{Article: How to Structure a LaTeX Document}
                \\author{Author1 \\and Author2 \\and ...}
                \\date{\\today}

                \\maketitle

                \\section{Section Title 1}

                \\section{Section Title 2}

                \\section{Section Title.....}

                \\section{Conclusion}

                \\section*{References}

                \\end{document}
                """
		);
		allTemplates.put("bookTemplate",
                """
                \\documentclass[11pt,a4paper]{book}

                \\begin{document}
                \\title{Book: How to Structure a LaTeX Document}
                \\author{Author1 \\and Author2 \\and ...}
                \\date{\\today}

                \\maketitle

                \\frontmatter

                \\chapter{Preface}
                % ...

                \\mainmatter
                \\chapter{First chapter}
                \\section{Section Title 1}
                \\section{Section Title 2}

                \\section{Section Title.....}

                \\chapter{....}

                \\chapter{Conclusion}

                \\chapter*{References}


                \\backmatter
                \\chapter{Last note}

                \\end{document}
                """
		);
		allTemplates.put("letterTemplate",
                """
                \\documentclass{letter}
                \\usepackage{hyperref}
                \\signature{Sender's Name}
                \\address{Sender's address...}
                \\begin{document}

                \\begin{letter}{Destination address....}
                \\opening{Dear Sir or Madam:}

                I am writing to you .......


                \\closing{Yours Faithfully,}
                \\ps

                P.S. text .....

                \\encl{Copyright permission form}

                \\end{letter}
                \\end{document}
                """
		);
		allTemplates.put("reportTemplate",
                """
                \\documentclass[11pt,a4paper]{report}

                \\begin{document}
                \\title{Report Template: How to Structure a LaTeX Document}
                \\author{Author1 \\and Author2 \\and ...}
                \\date{\\today}
                \\maketitle

                \\begin{abstract}
                Your abstract goes here...
                ...
                \\end{abstract}

                \\chapter{Introduction}
                \\section{Section Title 1}
                \\section{Section Title 2}
                \\section{Section Title.....}

                \\chapter{....}

                \\chapter{Conclusion}


                \\chapter*{References}

                \\end{document}
                """
		);
		return allTemplates;
	}
}
