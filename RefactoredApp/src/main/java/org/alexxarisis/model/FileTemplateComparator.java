package org.alexxarisis.model;

import java.util.HashMap;

public class FileTemplateComparator {

	private final HashMap<String, String> allTemplates;
	private final String fileContents;
	private String templateType;
	
	public FileTemplateComparator(String fileContents) {
		DocumentManager documentManager = new DocumentManager();
		allTemplates = documentManager.getAllTemplates();	
		this.fileContents = fileContents;
		this.templateType = "emptyTemplate"; 
	}
	
	public String compareToAllTemplateTypes() {
		for (String currentTemplateType: allTemplates.keySet()) {
			this.templateType = currentTemplateType;
			if (isCurrentTemplateType()) {
				break;
			}
		}
		return templateType;
	}

	private boolean isCurrentTemplateType() {
		return fileContents.startsWith(getTemplateFirstLine());
	}
	
	private String getTemplateFirstLine() {
		return allTemplates.get(templateType).split("\n")[0];
	}
}
