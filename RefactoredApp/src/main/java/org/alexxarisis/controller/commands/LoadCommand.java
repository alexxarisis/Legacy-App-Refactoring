package org.alexxarisis.controller.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.alexxarisis.converters.tolatex.ToLatexConverter;
import org.alexxarisis.converters.tolatex.ToLatexConverterFactory;
import org.alexxarisis.model.Document;
import org.alexxarisis.model.FileTemplateComparator;
import org.alexxarisis.model.VersionsManager;
import org.alexxarisis.view.LatexEditorView;

public class LoadCommand implements Command {

	private final LatexEditorView latexEditorView;
	private String filename;
	private String fileContents = "";
	
	public LoadCommand() {
		super();
		VersionsManager versionsManager = VersionsManager.getInstance();
		latexEditorView = versionsManager.getLatexEditorView();
	}
	
	@Override
	public void execute() {
		loadFromFile();
	}
	
	private void loadFromFile() {
		filename = latexEditorView.getController().getFilename();
		
		getFileContent();
		convertContentsIfNeeded();
		initializeDocument();
		latexEditorView.getController().setType(findFileTemplate());
	}
	
	private void getFileContent(){
		try {
			getScannedFileContent(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void getScannedFileContent(FileInputStream fileInputStream) {
		Scanner scanner = new Scanner(fileInputStream);
		while (scanner.hasNextLine())
			fileContents += scanner.nextLine() + "\n";
		scanner.close();
	}
	
	private void convertContentsIfNeeded(){
		String extension = getExtension();
		
		if (!extension.equals("tex")) {
			ToLatexConverterFactory factory = new ToLatexConverterFactory();
			ToLatexConverter converter = factory.getConverter(extension);
			fileContents = converter.convert(fileContents);
		}
	}
	
	private String getExtension() {
		return filename.substring(filename.indexOf(".") + 1);
	}
	
	private String findFileTemplate() {
		fileContents = fileContents.trim();
		FileTemplateComparator fileTemplateComparator = new FileTemplateComparator(fileContents);
		return fileTemplateComparator.compareToAllTemplateTypes();
	}
	
	private void initializeDocument() {
		Document currentDocument = new Document();
		currentDocument.setContents(fileContents);
		latexEditorView.setCurrentDocument(currentDocument);
	}
}
