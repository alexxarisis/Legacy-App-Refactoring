package org.alexxarisis.controller.commands;

import org.alexxarisis.model.VersionsManager;

public class RollbackToPreviousVersionCommand implements Command {
	
	@Override
	public void execute() {
		VersionsManager.getInstance().rollback();
	}
}