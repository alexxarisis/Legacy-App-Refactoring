package org.alexxarisis.controller.commands;

import org.alexxarisis.model.Document;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.view.LatexEditorView;

public class EditCommand implements Command {
	
	private final VersionsManager versionsManager;
	private final LatexEditorView latexEditorView;
	
	public EditCommand() {
		versionsManager = VersionsManager.getInstance();
		latexEditorView = versionsManager.getLatexEditorView();
	}
	
	@Override
	public void execute() {
		saveContents();
	}
	
	private void saveContents() {
		Document document = latexEditorView.getCurrentDocument();
		document.setContents(latexEditorView.getController().getText());
		changeDocumentVersion(document);
	}
	
	private void changeDocumentVersion(Document document) {
		if (versionsManager.isEnabled()) {
			document.changeVersion();
			versionsManager.putVersion(document);
		}
	}
}