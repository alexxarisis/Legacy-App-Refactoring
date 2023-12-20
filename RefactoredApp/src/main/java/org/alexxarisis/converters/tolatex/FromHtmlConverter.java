package org.alexxarisis.converters.tolatex;

import java.util.ArrayList;
import java.util.HashMap;

public class FromHtmlConverter implements ToLatexConverter {
	private HashMap<String, String> replacedContent;
	private final ArrayList<String> tabulars;
	private int columns;
	private int lines;
	private boolean isFirstTableRow;
	
	public FromHtmlConverter() {
		columns = 0;
		lines = 0;
		tabulars = new ArrayList<>();
		isFirstTableRow = true;
	}
	public String convert(String source) {
		replacedContent = new HashMap<>();
		
		createReplacedHtmlContents();
		return getLatexCodeFromHtml(source);
	}
	
	private void createReplacedHtmlContents() {
		replacedContent.put("<h1>#","\\chapter{#}");  
		replacedContent.put("<h2>#","\\section{#}");  
		replacedContent.put("<h3>#","\\subsection{#}");  
		replacedContent.put("<h4>#","\\subsubsection{#}");  	
		replacedContent.put("<ul>","\\begin{itemize}");  
		replacedContent.put("<li>#","\\item #");  
		replacedContent.put("</ul>","\\end{itemize}"); 
		replacedContent.put("<ol>","\\begin{enumerate}");  
		replacedContent.put("</ol>","\\end{enumerate}");	
		replacedContent.put("<table>","\\begin{table}");	
		replacedContent.put("</table>", """
                \\hline
                \\end{tabular}\s
                \\end{table}""");
		replacedContent.put("<caption>$","\\caption{#}\\label{#}");
		replacedContent.put("<figcaption>$","\\caption{#}\\label{#}");
		replacedContent.put("<tr>", """
                \\begin{tabular}{#}
                \\hline
                """);
		replacedContent.put("</td>!","#&"); 
		replacedContent.put("</tr>","\\\\");
		replacedContent.put("<img@","\\includegraphics[width=#,height=#]{#}"); 
		replacedContent.put("<figure>","\\begin{figure}");
		replacedContent.put("</figure>","\\end{figure}");
	}
	
	private String getLatexCodeFromHtml(String documentText) {
		String[] splitDocumentText = documentText.split("[\n\t]");
		return removeEmptyLines(getLatexCodeForContents(splitDocumentText));
	}
	
	private String removeEmptyLines(String text) {
		return text.replaceAll("(?m)^[ \t]*\r?\n", "");
	}
	
	private String getLatexCodeForContents(String[] splitDocumentText) {
		String latexCode = "";
		for(String documentTextLine: splitDocumentText) {
			for (String currentLatexText: replacedContent.keySet()) { 
				if(isStartOrEndWithCurrentHtmlText(currentLatexText, documentTextLine)) {
					latexCode += getCurrentLatexCode(currentLatexText, documentTextLine);
				}
			}
		}
		latexCode = replaceTabulars(latexCode);
		latexCode = refixTableLine(latexCode);
		return latexCode;
	}
	
	private boolean isStartOrEndWithCurrentHtmlText(String currentLatexText,String documentTextLine) {
		String latexTextPart = currentLatexText.substring(0, currentLatexText.length()-1);
		return documentTextLine.startsWith(latexTextPart) || documentTextLine.endsWith(latexTextPart);
	}
	
	private String getCurrentLatexCode(String currentLatexText,String documentTextLine) {
		String currentLatexCode = replacedContent.get(currentLatexText);
		
		if (currentLatexText.endsWith("#")) {
			currentLatexCode = getSimpleReplaceLatexCode(documentTextLine,currentLatexText) + "\n";
		} else if (currentLatexText.endsWith("$")) {
			currentLatexCode = getComplexReplaceLatexCode(documentTextLine,currentLatexText) + "\n";
		} else if (currentLatexText.endsWith("@")) {
			currentLatexCode = getFigureReplaceLatexCode(documentTextLine,currentLatexText) + "\n";
		} else if (currentLatexText.endsWith("!")) {
			currentLatexCode = getTableLineLatexCode(documentTextLine,currentLatexText);
			columns++;
		} else if (currentLatexText.contains("<tr>")) {
			if (isFirstTableRow) {
				isFirstTableRow = false;
			} else {
				currentLatexCode = "";
			}
		}
		else {
			currentLatexCode = replacedContent.get(currentLatexText) + "\n";
			if (currentLatexText.contains("/tr")) {
				lines++;
			}
			else if (currentLatexText.contains("</table>")) {
				addTemplateTabular();
				isFirstTableRow = true;
			}
		}	
		return currentLatexCode ;
	}
	
	private String getComplexReplaceLatexCode(String documentTextLine, String currentLatexText) {
		currentLatexText = getSimpleReplaceLatexCode(documentTextLine, currentLatexText);
		currentLatexText = currentLatexText.replace("#", getParagraphText(documentTextLine));
		return currentLatexText;
	}
	
	private String getSimpleReplaceLatexCode(String documentTextLine, String currentLatexText) {
		return replacedContent.get(currentLatexText).replaceFirst("#", documentTextLine.split("[<>]")[2]);
	}
	
	private String getParagraphText(String documentTextLine) {
		return documentTextLine.split("<p>")[1].replace("</p>", "");
	}
	
	private String getFigureReplaceLatexCode(String documentTextLine, String currentLatexText) {
		currentLatexText = replacedContent.get(currentLatexText).replaceFirst("#", documentTextLine.split("'")[1]);
		currentLatexText = currentLatexText.replaceFirst("#", documentTextLine.split("'")[3]);
		currentLatexText = currentLatexText.replaceFirst("#", documentTextLine.split("'")[5]);
		return currentLatexText;
	}
	
	private String getTableLineLatexCode(String documentTextLine, String currentLatexText) {
		return replacedContent.get(currentLatexText).replace("#", documentTextLine.split("[<>]")[2]);
	}
	
	private void addTemplateTabular() {
		String tabular="|";
		for (int i=0; i<(columns/lines); i++) {
			tabular += "c|";
		}
		tabulars.add(tabular);
		clearTableElements();
	}
	
	private void clearTableElements() {
		columns = 0;
		lines = 0;
	}
	
	private String replaceTabulars(String latexCode) {
        for (String tabular : tabulars) {
            if (latexCode.contains("#"))
                latexCode = replaceTabular(latexCode, tabular);
        }
		return latexCode;
	}
	
	private String replaceTabular(String latexCode,String tabular) {
		return latexCode.replaceFirst("#", tabular);
	}
	
	private String refixTableLine(String latexCode) {
		if (latexCode.contains("&\\\\")) {
			latexCode = latexCode.replace("&\\\\","\\\\");
		}
		return latexCode;
	}
}
