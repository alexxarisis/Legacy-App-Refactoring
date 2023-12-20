package org.alexxarisis.controller.commands;

import org.alexxarisis.model.DocumentManager;

import java.util.HashMap;

public class CommandFactory {

	private final DocumentManager documentManager;
	private final HashMap<String, Command> commands;
	
	public CommandFactory() {
		super();
		documentManager = new DocumentManager();
		commands = new HashMap<>();
		createAllContents();
	}
	
	private void createAllContents() {
		commands.put("addLatex", new AddLatexCommand());
		commands.put("changeVersionsStrategy", new ChangeVersionsStrategyCommand());
		commands.put("create", new CreateCommand(documentManager));
		commands.put("disableVersionsManagement", new DisableVersionsManagementCommand());
		commands.put("edit", new EditCommand());
		commands.put("enableVersionsManagement", new EnableVersionsManagementCommand());
		commands.put("load", new LoadCommand());
		commands.put("rollbackToPreviousVersion", new RollbackToPreviousVersionCommand());
		commands.put("save", new SaveCommand());
	}

	public Command createCommand(String type) {
		return commands.get(type);
	}
}
