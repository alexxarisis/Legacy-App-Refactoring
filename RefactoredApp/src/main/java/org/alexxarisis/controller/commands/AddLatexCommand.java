package org.alexxarisis.controller.commands;

import org.alexxarisis.model.ContentsManager;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.view.LatexEditorView;

public class AddLatexCommand implements Command  {
	
	@Override
	public void execute() {
		VersionsManager versionsManager = VersionsManager.getInstance();
		ContentsManager contentsManager = new ContentsManager();
		LatexEditorView latexEditorView = versionsManager.getLatexEditorView();
		
		contentsManager.addContent(latexEditorView.getController().getContentType());
		latexEditorView.getController().enact("edit");
	}
}