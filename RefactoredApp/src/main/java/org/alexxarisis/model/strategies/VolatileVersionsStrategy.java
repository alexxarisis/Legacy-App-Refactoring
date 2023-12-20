package org.alexxarisis.model.strategies;

import java.util.ArrayList;
import java.util.List;

import org.alexxarisis.model.Document;

public class VolatileVersionsStrategy implements VersionsStrategy {

	private final ArrayList<Document> history;
	
	public VolatileVersionsStrategy() {
		super();
		history = new ArrayList<>();
	}

	@Override
	public void putVersion(Document document) {
		Document doc = document.clone();
		history.add(doc);
	}

	@Override
	public Document getVersion() {
		if (history.isEmpty())
			return null;
		return history.get(history.size() - 1);
	}

	@Override
	public void setEntireHistory(List<Document> documents) {
		history.clear();
		history.addAll(documents);
	}

	@Override
	public List<Document> getEntireHistory() {
		return history;
	}

	@Override
	public void removeVersion() {
		if (history.isEmpty())
			return;
		history.remove(history.size() - 1);
	}
}