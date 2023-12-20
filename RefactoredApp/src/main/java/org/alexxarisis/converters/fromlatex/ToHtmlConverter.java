package org.alexxarisis.converters.fromlatex;

import java.util.HashMap;

public class ToHtmlConverter implements FromLatexConverter {

	private HashMap<String, String> replacedHtml;
	private boolean isFigure;
	
	public String convert(String source) {
		isFigure = false;
		replacedHtml = new HashMap<>();
		createReplacedHtmlContents();
		return getHtmlCode(getHtmlCodeFromText(source));		
	}
	
	private void createReplacedHtmlContents() {
		replacedHtml.put("\\chapter","<h1>#</h1>");  
		replacedHtml.put("\\section","<h2>#</h2>"); 
		replacedHtml.put("\\subsection","<h3>#</h3>"); 
		replacedHtml.put("\\subsubsection","<h4>#</h4>"); 
		replacedHtml.put("\\begin{enumerate}","<ol>"); 
		replacedHtml.put("\\end{enumerate}","</ol>"); 
		replacedHtml.put("\\begin{itemize}","<ul>"); 
		replacedHtml.put("\\end{itemize}","</ul>"); 
		replacedHtml.put("\\item","<li>#</li>"); 
		replacedHtml.put("\\begin{figure}","<figure>"); 
		replacedHtml.put("\\includegraphics","<img width='#' height='#' src='#'>"); 
		replacedHtml.put("\\end{figure}","</figure>"); 
		replacedHtml.put("\\caption","<caption>#</caption><p>#</p>"); 
		replacedHtml.put("\\begin{table}","<table>"); 
		replacedHtml.put("\\end{table}","</table>"); 
		replacedHtml.put("\\\\",""); 
	}
	
	private String getHtmlCodeFromText(String documentText) {
		String[] splitDocumentText = documentText.split("\n");
		return removeEmptyLines(getHtmlCodeForContents(splitDocumentText));
	}
	
	private String removeEmptyLines(String text) {
		return text.replaceAll("(?m)^[ \t]*\r?\n", "");
	}
	
	private String getHtmlCodeForContents(String[] splitDocumentText) {
		String htmlCode = "";
		for (String documentTextLine: splitDocumentText) {
			for (String currentHtmlText: replacedHtml.keySet()) { 
				if (isStartOrEndWithCurrentLatexText(currentHtmlText, documentTextLine)) {
					htmlCode += getCurrentHtmlCode(currentHtmlText, documentTextLine);
				}
			}
		}
		return htmlCode;
	}
	
	private boolean isStartOrEndWithCurrentLatexText(String currentHtmlText, String documentTextLine) {
		return documentTextLine.startsWith(currentHtmlText) || documentTextLine.endsWith(currentHtmlText);
	}
	
	private String getCurrentHtmlCode(String currentHtmlText, String documentTextLine) {
		String currentHtmlCode = replacedHtml.get(currentHtmlText);
		if (documentTextLine.contains("\\label")) {
			currentHtmlCode = getCaptionLabelHtmlCode(documentTextLine, currentHtmlText);
			if (isFigure) {
				isFigure = false;
				currentHtmlCode = currentHtmlCode.replace("caption", "figcaption");
			}
		} else if (documentTextLine.contains("\\includegraphics")) {
			isFigure = true;
			currentHtmlCode = getFigureHtmlCode(documentTextLine, currentHtmlText);
		} else if (currentHtmlText.contentEquals("\\item")) {
			currentHtmlCode = getItemHtmlCode(documentTextLine, currentHtmlText);
		} else if (documentTextLine.endsWith("\\\\")) {
			currentHtmlCode = getTableHtmlCode(documentTextLine);
		} else if (currentHtmlCode.contains("#")) {
			currentHtmlCode = getOthersHtmlCode(documentTextLine, currentHtmlText);
		}	
		return "\t" + currentHtmlCode +  "\n";
	}
	
	private String getCaptionLabelHtmlCode(String documentTextLine, String currentHtmlText) {
		String[] splitDocumentTextLine = documentTextLine.split("}");
		return replaceLabelElement(splitDocumentTextLine,
				replaceCaptionElement(splitDocumentTextLine,currentHtmlText));
	}
	
	private String replaceCaptionElement(String[] splitDocumentTextLine, String currentHtmlText) {
		return replacedHtml.get(currentHtmlText).replaceFirst("#", 
				removeText(splitDocumentTextLine[0],currentHtmlText + "{"));
	}
	
	private String replaceLabelElement(String[] splitDocumentTextLine, String currentHtmlCode) {
		return currentHtmlCode.replaceFirst("#", 
				removeText(splitDocumentTextLine[1],"\\label{"));
	}
	
	private String getFigureHtmlCode(String documentTextLine, String currentHtmlText) {
		String[] splitDocumentTextLine = documentTextLine.split("[=,{]");
		return replaceFigureElements(splitDocumentTextLine, replacedHtml.get(currentHtmlText));
	}
	
	private String replaceFigureElements(String[] splitDocumentTextLine, String currentHtmlCode) {
		currentHtmlCode = currentHtmlCode.replaceFirst("#", splitDocumentTextLine[1]);
		currentHtmlCode = currentHtmlCode.replaceFirst("#", substringArrayText(splitDocumentTextLine,3));
		currentHtmlCode = currentHtmlCode.replaceFirst("#", substringArrayText(splitDocumentTextLine,4));
		return currentHtmlCode;
	}
	
	private String substringArrayText(String[] text, int index) {
		return text[index].substring(0, text[index].length() -1);
	}
	
	private String getOthersHtmlCode(String documentTextLine, String currentHtmlText) {
		documentTextLine = removeText(documentTextLine, currentHtmlText);
		documentTextLine = documentTextLine.substring(1, documentTextLine.length() -1);
		return replacedHtml.get(currentHtmlText).replaceFirst("#", documentTextLine);
	}
	
	private String getItemHtmlCode(String documentTextLine, String currentHtmlText) { 
		documentTextLine = removeText(documentTextLine, currentHtmlText).trim();
		return "\t" + replacedHtml.get(currentHtmlText).replace("#", documentTextLine);
	}
	
	private String getTableHtmlCode(String documentTextLine) { 
		String[] tableRows = removeText(documentTextLine,"\\\\").split("&");
		return getHtmlTableRow(getHtmlTableLinesCode(tableRows));
	}
	
	private String removeText(String text, String removedText) { 
		return text.replace(removedText, "");
	}
	
	private String getHtmlTableLinesCode(String[] tableRows) { 
		String htmlTableRowData = "";
        for (String tableRow : tableRows)
			htmlTableRowData += "\t\t<td>" + tableRow + "</td>\n";
		return htmlTableRowData;
	}
	
	private String getHtmlTableRow(String htmlTableRowData) { 
		return "<tr>" + "\n" + htmlTableRowData + "\t" + "</tr>" + "\n";
	}
	
	private String getHtmlCode(String htmlCodeFromText) {
		String htmlCodeStart = """
                <!DOCTYPE html>
                <html>
                <body>
                """;
		String htmlCodeEnd = "</body>\n"
				+ "</html>";
		return htmlCodeStart + htmlCodeFromText + htmlCodeEnd;
	}
}