package org.alexxarisis.model.strategies;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.alexxarisis.model.Document;

public class StableVersionsStrategy implements VersionsStrategy {

	private String versionID = "0";
	
	@Override
	public void putVersion(Document document) {
		String filename = document.getVersionID() + ".tex";
		document.save(filename);
		versionID = document.getVersionID();
	}

	@Override
	public Document getVersion() {
		if (isNotValidVersion())
			return null;

		int version = Integer.parseInt(versionID);
		String fileContents = readFile(version);
		return setupDocument(fileContents, version);
	}

	@Override
	public void setEntireHistory(List<Document> documents) {
		if (documents.isEmpty()) {
			versionID = "0";
			return;
		}

        for (Document doc : documents) {
            doc.save(doc.getVersionID() + ".tex");
        }
		versionID = documents.get(documents.size()-1).getVersionID();
	}
	
	@Override
	public List<Document> getEntireHistory() {
		List<Document> documents = new ArrayList<>();
		if (isNotValidVersion())
			return documents;

		int maxVersion = Integer.parseInt(versionID);
		for (int version = 1; version <= maxVersion; version++) {
			String fileContents = readFile(version);
			Document document = setupDocument(fileContents, version);
			documents.add(document);
		}
		return documents;
	}
	
	@Override
	public void removeVersion() {
		if (isNotValidVersion())
			return;
		
		File fileToRemove = new File(versionID + ".tex");
		fileToRemove.delete();
		
		int n = Integer.parseInt(versionID);
		versionID = (n-1) + "";
	}
	
	private boolean isNotValidVersion() {
		return versionID.equals("0");
	}
	
	private String readFile(int version) {
		String fileContents = "";
		try {
			Scanner scanner = new Scanner(new FileInputStream(version + ".tex"));
			while (scanner.hasNextLine())
				fileContents += scanner.nextLine() + "\n";
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileContents.trim();
	}
	
	private Document setupDocument(String contents, int version) { 
		Document document = new Document();
		for (int i=0; i < version; i++)
			document.changeVersion();
		document.setContents(contents);
		return document;
	}
}
