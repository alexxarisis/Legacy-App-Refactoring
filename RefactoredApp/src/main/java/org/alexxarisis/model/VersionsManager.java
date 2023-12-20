package org.alexxarisis.model;

import javax.swing.JOptionPane;

import org.alexxarisis.model.strategies.VersionsStrategy;
import org.alexxarisis.model.strategies.VersionsStrategyFactory;
import org.alexxarisis.view.LatexEditorView;

public class VersionsManager {

	private static final VersionsManager instance = new VersionsManager();
	private boolean enabled;
	private VersionsStrategy strategy;
	private String type;
	private LatexEditorView latexEditorView;
	private VersionsStrategyFactory factory;
	
	private VersionsManager() {}
	
	public static VersionsManager getInstance() {
		if (instance.factory == null) {
			instance.factory = new VersionsStrategyFactory();
			instance.strategy = instance.factory.createStrategy("volatileStrategy");
			instance.type = "volatileStrategy";
		}
	    return instance;
	}
	
	public boolean isEnabled() {
		return instance.enabled;
	}
	
	public void enable() {
		instance.enabled = true;
	}

	public void disable() {
		instance.enabled = false;
	}
	
	public VersionsStrategy getStrategy() {
		return instance.strategy;
	}
	
	public void setStrategyType(String strategyType) {
		instance.type = strategyType;
	}
	
	public String getStrategyType() {
		return instance.type;
	}

	public LatexEditorView getLatexEditorView() {
		return instance.latexEditorView;
	}
	
	public void setLatexEditorView(LatexEditorView latexEditorView) {
		instance.latexEditorView = latexEditorView;
	}
	
	public void enableStrategy() {
		changeStrategy();
		enable();
	}
	
	public void changeStrategy() {
		VersionsStrategy newStrategy = instance.factory.createStrategy(instance.type);
		newStrategy.setEntireHistory(instance.strategy.getEntireHistory());
		instance.strategy = newStrategy;
	}

	public void putVersion(Document document) {
		instance.strategy.putVersion(document);
	}

	public void rollback() {
		if (!isEnabled()) {
			showErrorMessage("Strategy is not enabled");
			return;
		}
		Document document = instance.strategy.getVersion();
		if (isValidDocument(document)) {
			showErrorMessage("No version available");
			return;
		}
		rollbackToPreviousVersion();
	}
	
	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, 
				"InfoBox", JOptionPane.INFORMATION_MESSAGE);
	}

	private boolean isValidDocument(Document document) {
		return document == null || document.isFirstVersion();
	}
	
	private void rollbackToPreviousVersion() {
		instance.strategy.removeVersion();
		Document document = instance.strategy.getVersion();
		instance.latexEditorView.setCurrentDocument(document);
	}
}