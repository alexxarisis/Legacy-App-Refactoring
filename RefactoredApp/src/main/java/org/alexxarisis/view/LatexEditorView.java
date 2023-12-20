package org.alexxarisis.view;

import javax.swing.JEditorPane;

import org.alexxarisis.controller.LatexEditorController;
import org.alexxarisis.model.Document;

public class LatexEditorView {

	private LatexEditorController controller;
	private JEditorPane editorPane;  
	private Document currentDocument; 
	
	public LatexEditorController getController() {
		return controller;
	}

	public void setController(LatexEditorController controller) {
		this.controller = controller;
	}

	public void setEditorPane(JEditorPane editorPane) {
		this.editorPane = editorPane;
	}

	public JEditorPane getEditorPane() {
		return editorPane;
	}

	public Document getCurrentDocument() {
		return currentDocument;
	}

	public void setCurrentDocument(Document currentDocument) {
		this.currentDocument = currentDocument;
	}
}