package org.alexxarisis.view;

import javax.swing.JFrame;

import org.alexxarisis.controller.LatexEditorController;
import org.alexxarisis.model.VersionsManager;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class OpeningWindow {

	private JFrame frame;
	private final LatexEditorView latexEditorView;

	public OpeningWindow() {
		latexEditorView = new LatexEditorView();
		VersionsManager versionsManager = VersionsManager.getInstance();
		versionsManager.setLatexEditorView(latexEditorView);
		LatexEditorController controller = new LatexEditorController();
		latexEditorView.setController(controller);
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton createNewDocumentButton = new JButton("Create New Document");
		createNewDocumentButton.addActionListener(e -> {
            new ChooseTemplate(latexEditorView, "opening");
            frame.dispose();
        });

		createNewDocumentButton.setBounds(89, 26, 278, 36);
		frame.getContentPane().add(createNewDocumentButton);

		frame.getContentPane().add(getOpenExistingDocumentButton());
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });
		exitButton.setBounds(99, 169, 268, 25);
		frame.getContentPane().add(exitButton);
	}

	private JButton getOpenExistingDocumentButton() {
		JButton openExistingDocumentButton = new JButton("Open Existing Document");
		openExistingDocumentButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);

            if (option == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().toString();
                latexEditorView.getController().setFilename(filename);
                latexEditorView.getController().enact("load");
                new MainWindow(latexEditorView);
                frame.dispose();
            }
        });
		openExistingDocumentButton.setBounds(89, 92, 278, 36);
		return openExistingDocumentButton;
	}
}
