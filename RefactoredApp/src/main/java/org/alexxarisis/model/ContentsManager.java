package org.alexxarisis.model;

import java.util.HashMap;

import javax.swing.JEditorPane;

import org.alexxarisis.view.LatexEditorView;

public class ContentsManager {

	private final JEditorPane editorPane;
	private final LatexEditorView latexEditorView;
	
	public ContentsManager() {
		VersionsManager versionsManager = VersionsManager.getInstance();
		latexEditorView = versionsManager.getLatexEditorView();
		editorPane = latexEditorView.getEditorPane();
	}
	
	public HashMap<String, String> getAllContents() {
		HashMap<String, String> contents = new HashMap<>();
		contents.put("chapter",
                """

                \\chapter{...}
                """
		);
		contents.put("section",
                """

                \\section{...}
                """
		);
		contents.put("subsection",
                """

                \\subsection{...}
                """
		);
		contents.put("subsubsection",
                """

                \\subsubsection{...}
                """
		);
		contents.put("enumerate",
                """
                \\begin{enumerate}
                \\item ...
                \\item ...
                \\end{enumerate}
                """
		);
		contents.put("itemize",
                """
                \\begin{itemize}
                \\item ...
                \\item ...
                \\end{itemize}
                """
		);
		contents.put("table",
                """
                \\begin{table}
                \\caption{....}\\label{...}
                \\begin{tabular}{|c|c|c|}
                \\hline
                ... &...&...\\\\
                ... &...&...\\\\
                ... &...&...\\\\
                \\hline
                \\end{tabular}
                \\end{table}
                """
		);
		contents.put("figure",
                """
                \\begin{figure}
                \\includegraphics[width=...,height=...]{...}
                \\caption{....}\\label{...}
                \\end{figure}
                """
		);
		return contents;
	}
	
	public void addContent(String type) {
		setTexts(addContentBetweenPreviousContent(type));
	}
	
	private String addContentBetweenPreviousContent(String type) {
		String contents = editorPane.getText();
		String before = contents.substring(0, editorPane.getCaretPosition());
		String after = contents.substring(editorPane.getCaretPosition());
		return before + getAllContents().get(type) + after;
	}
	
	private void setTexts(String contents) {
		latexEditorView.getController().setText(contents);
		editorPane.setText(contents);
	}
}
