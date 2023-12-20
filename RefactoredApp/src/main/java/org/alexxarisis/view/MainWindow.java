package org.alexxarisis.view;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.alexxarisis.model.VersionsManager;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;

import java.io.File;
import javax.swing.JScrollPane;
import javax.swing.JCheckBoxMenuItem;

public class MainWindow {

	private final JEditorPane editorPane = new JEditorPane();
	private final LatexEditorView latexEditorView;
	private final VersionsManager versionsManager;

	private JFrame frame;

	public MainWindow(LatexEditorView latexEditorView) {
		this.latexEditorView = latexEditorView;
		this.versionsManager = VersionsManager.getInstance();
		initialize();
		latexEditorView.setEditorPane(editorPane);
		frame.setVisible(true);
	}

	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 823, 566);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 805, 26);
		frame.getContentPane().add(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem createNewFileButton = new JMenuItem("New file");
		createNewFileButton.addActionListener(e -> {
            new ChooseTemplate(latexEditorView, "main");
            frame.dispose();
        });
		fileMenu.add(createNewFileButton);
		
		JMenuItem saveButton = new JMenuItem("Save");
		saveButton.addActionListener(e -> {
            latexEditorView.getController().setText(editorPane.getText());
            latexEditorView.getController().enact("edit");
        });
		fileMenu.add(saveButton);

		JMenuItem addChapterButton = new JMenuItem("Add chapter");
		JMenu commandsMenu = new JMenu("Commands");
		JMenuItem loadFileButton = getLoadFileButton(commandsMenu, addChapterButton);
		fileMenu.add(loadFileButton);

		JMenuItem saveFileMenu = getSaveFileMenu(saveButton);
		fileMenu.add(saveFileMenu);
		
		JMenuItem exitButton = new JMenuItem("Exit");
		exitButton.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });
		fileMenu.add(exitButton);
		
		
		menuBar.add(commandsMenu);
		if (latexEditorView.getController().getType().equals("letterTemplate")) {
			commandsMenu.setEnabled(false);
		}

		addChapterButton.addActionListener(e -> enactAddLatexCommandWithSpecificContent("chapter"));
		commandsMenu.add(addChapterButton);
		if (latexEditorView.getController().getType().equals("articleTemplate")) {
			addChapterButton.setEnabled(false);
		}
		
		JMenu addSection = new JMenu("Add Section");
		commandsMenu.add(addSection);
		
		JMenuItem addSectionButton = new JMenuItem("Add section");
		addSectionButton.addActionListener(e -> enactAddLatexCommandWithSpecificContent("section"));
		addSection.add(addSectionButton);
		
		JMenuItem addSubsectionButton = new JMenuItem("Add subsection");
		addSubsectionButton.addActionListener(e -> enactAddLatexCommandWithSpecificContent("subsection"));
		addSection.add(addSubsectionButton);
		
		JMenuItem addSubsubsectionButton = new JMenuItem("Add subsubsection");
		addSubsubsectionButton.addActionListener(e -> enactAddLatexCommandWithSpecificContent("subsubsection"));
		addSection.add(addSubsubsectionButton);
		
		JMenu addEnumerationListMenuButton = new JMenu("Add enumeration list");
		commandsMenu.add(addEnumerationListMenuButton);
		
		JMenuItem itemizeButton = new JMenuItem("Itemize");
		itemizeButton.addActionListener(e -> enactAddLatexCommandWithSpecificContent("itemize"));
		addEnumerationListMenuButton.add(itemizeButton);
		
		JMenuItem enumerateButton = new JMenuItem("Enumerate");
		enumerateButton.addActionListener(e -> enactAddLatexCommandWithSpecificContent("enumerate"));
		addEnumerationListMenuButton.add(enumerateButton);
		
		JMenuItem addTableButton = new JMenuItem("Add table");
		addTableButton.addActionListener(e -> enactAddLatexCommandWithSpecificContent("table"));
		commandsMenu.add(addTableButton);
		
		JMenuItem addFigureButton = new JMenuItem("Add figure");
		addFigureButton.addActionListener(e -> enactAddLatexCommandWithSpecificContent("figure"));
		commandsMenu.add(addFigureButton);
		
		JMenu strategiesMenuButton = new JMenu("Strategy");
		menuBar.add(strategiesMenuButton);
		
		JMenu enableVersionsStrategyMenuButton = new JMenu("Enable");
		strategiesMenuButton.add(enableVersionsStrategyMenuButton);
		
		JCheckBoxMenuItem volatileVersionsStrategyButton = new JCheckBoxMenuItem("Volatile");
		JCheckBoxMenuItem stableVersionsStrategyButton = new JCheckBoxMenuItem("Stable");
		stableVersionsStrategyButton.addActionListener(e -> {
            versionsManager.setStrategyType("stableStrategy");
            if (!versionsManager.isEnabled()) {
                latexEditorView.getController().enact("enableVersionsManagement");
            }
            else {
                latexEditorView.getController().enact("changeVersionsStrategy");
            }
			volatileVersionsStrategyButton.setSelected(false);
			stableVersionsStrategyButton.setEnabled(false);
			volatileVersionsStrategyButton.setEnabled(true);
        });

		volatileVersionsStrategyButton.addActionListener(e -> {
            versionsManager.setStrategyType("volatileStrategy");
            if (!versionsManager.isEnabled()) {
                latexEditorView.getController().enact("enableVersionsManagement");
            }
            else {
                latexEditorView.getController().enact("changeVersionsStrategy");
            }
			stableVersionsStrategyButton.setSelected(false);
			volatileVersionsStrategyButton.setEnabled(false);
			stableVersionsStrategyButton.setEnabled(true);
        });
		enableVersionsStrategyMenuButton.add(volatileVersionsStrategyButton);
		enableVersionsStrategyMenuButton.add(stableVersionsStrategyButton);
		
		JMenuItem disableVersionsStrategyButton = new JMenuItem("Disable");
		disableVersionsStrategyButton.addActionListener(e -> latexEditorView.getController().enact("disableVersionsManagement"));
		strategiesMenuButton.add(disableVersionsStrategyButton);
		
		JMenuItem rollbackButton = new JMenuItem("Rollback");
		rollbackButton.addActionListener(e -> {
            latexEditorView.getController().enact("rollbackToPreviousVersion");
            updateText();
        });
		strategiesMenuButton.add(rollbackButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 39, 783, 467);
		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(editorPane);
		
		updateText();
	}

	private JMenuItem getSaveFileMenu(JMenuItem saveButton) {
		JMenuItem saveFileMenu = new JMenuItem("Save file");
		saveFileMenu.addActionListener(e -> {
            saveButton.doClick();
            JFileChooser fileChooser = new JFileChooser();
            String presetFilename = latexEditorView.getCurrentDocument()
                    .getVersionID() + ".tex";
			fileChooser.setSelectedFile(new File(presetFilename));

            int option = fileChooser.showSaveDialog(null);
            if(option != JFileChooser.APPROVE_OPTION)
                return;

            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            latexEditorView.getController().setFilename(filename);
            latexEditorView.getController().enact("save");
        });
		return saveFileMenu;
	}

	private JMenuItem getLoadFileButton(JMenu mnCommands, JMenuItem addChapter) {
		JMenuItem loadFileButton = new JMenuItem("Load file");
		loadFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if(option == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();

                latexEditorView.getController().setFilename(filename);
                latexEditorView.getController().enact("load");
                mnCommands.setEnabled(true);
                addChapter.setEnabled(true);
                if(latexEditorView.getController().getType().equals("letterTemplate")) {
                    mnCommands.setEnabled(false);
                }
                if(latexEditorView.getController().getType().equals("articleTemplate")) {
                    addChapter.setEnabled(false);
                }
                updateText();
            }
        });
		return loadFileButton;
	}
	
	private void enactAddLatexCommandWithSpecificContent(String type) {
		latexEditorView.getController().setContentType(type);
		latexEditorView.getController().enact("addLatex");
	}
	
	private void updateText() {
		editorPane.setText(latexEditorView.getCurrentDocument().getContents());
	}
}
