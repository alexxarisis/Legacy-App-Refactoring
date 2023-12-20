package org.alexxarisis.controller;

import java.util.HashMap;
import java.util.List;

import org.alexxarisis.controller.commands.Command;
import org.alexxarisis.controller.commands.CommandFactory;

public class LatexEditorController {

	private final HashMap<String, Command> commands;
	private final CommandFactory commandFactory;
	private String contentType;
	private String type;
	private String text;
	private String filename;
	
	public LatexEditorController() {
		commandFactory = new CommandFactory();
		commands = new HashMap<>();
		createAllCommands();
	}
	private void createAllCommands() {
		for (String command : getCommandNames()) {
			commands.put(command, commandFactory.createCommand(command));
		}
	}

	private List<String> getCommandNames() {
        return List.of(
				"addLatex",
				"changeVersionsStrategy",
				"create",
				"disableVersionsManagement",
				"edit",
				"enableVersionsManagement",
				"load",
				"rollbackToPreviousVersion",
				"save"
		);
	}
	
	public void enact(String command) {
		commands.get(command).execute();
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
