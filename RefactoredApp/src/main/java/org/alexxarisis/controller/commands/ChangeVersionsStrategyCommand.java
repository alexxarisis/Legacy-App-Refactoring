package org.alexxarisis.controller.commands;

import org.alexxarisis.model.VersionsManager;

public class ChangeVersionsStrategyCommand implements Command {

	@Override
	public void execute() {
		VersionsManager versionsManager = VersionsManager.getInstance();
		versionsManager.changeStrategy();
	}
}