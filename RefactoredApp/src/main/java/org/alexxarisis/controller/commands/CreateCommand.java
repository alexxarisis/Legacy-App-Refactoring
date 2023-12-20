package org.alexxarisis.controller.commands;

import org.alexxarisis.model.Document;
import org.alexxarisis.model.DocumentManager;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.view.LatexEditorView;

public class CreateCommand implements Command {
	private final DocumentManager documentManager;
	
	public CreateCommand(DocumentManager documentManager) {
		super();
		this.documentManager = documentManager;
	}

	@Override
	public void execute() {
		LatexEditorView latexEditorView = VersionsManager.getInstance()
											.getLatexEditorView();
		String type = latexEditorView.getController().getType();
		Document document = documentManager.createDocument(type);
		latexEditorView.setCurrentDocument(document);
	}
}