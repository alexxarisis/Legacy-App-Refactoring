package org.alexxarisis.view;

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import java.util.ArrayList;
import java.util.List;

public class ChooseTemplate {

	private final LatexEditorView latexEditorView;
	private final String previousScreen;
	private JFrame frame;

	public ChooseTemplate(LatexEditorView latexEditorView, String previousScreen) {
		this.latexEditorView = latexEditorView;
		this.previousScreen = previousScreen;
		initialize();
		frame.setVisible(true);
	}


	private void deselectRadioButtons(JRadioButton radioButton1, JRadioButton radioButton2,
									  JRadioButton radioButton3, JRadioButton radioButton4) {
		if (radioButton1.isSelected()) {
			radioButton2.setSelected(false);
			radioButton3.setSelected(false);
			radioButton4.setSelected(false);
		}
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JRadioButton book = new JRadioButton("Book");
		JRadioButton article = new JRadioButton("Article");
		JRadioButton report = new JRadioButton("Report");
		JRadioButton letter = new JRadioButton("Letter");
		
		List<AbstractButton> buttons = new ArrayList<>();
		buttons.add(book);
		buttons.add(article);
		buttons.add(report);
		buttons.add(letter);
		
		book.addActionListener(e -> deselectRadioButtons(book, article, report, letter));
		book.setBounds(42, 51, 127, 25);
		frame.getContentPane().add(book);
		
		JLabel chooseTemplateLabel = new JLabel("Choose template. (Leave empty for blank document)");
		chooseTemplateLabel.setBounds(42, 13, 332, 16);
		frame.getContentPane().add(chooseTemplateLabel);
		
		article.addActionListener(e -> deselectRadioButtons(article, book, report, letter));
		article.setBounds(42, 137, 127, 25);
		frame.getContentPane().add(article);
		
		report.addActionListener(e -> deselectRadioButtons(report, article, book, letter));
		report.setBounds(213, 51, 127, 25);
		frame.getContentPane().add(report);
		
		letter.addActionListener(e -> deselectRadioButtons( letter, report, article, book));
		letter.setBounds(213, 137, 127, 25);
		frame.getContentPane().add(letter);

		frame.getContentPane().add(getCreateButton(buttons));
		frame.getContentPane().add(getBackButton());
	}

	private JButton getCreateButton(List<AbstractButton> buttons) {
		JButton createButton = new JButton("Create");
		createButton.addActionListener(e -> {
            String template = "emptyTemplate";
            for (AbstractButton button : buttons)
                if (button.isSelected())
                    template = button.getText().toLowerCase() + "Template";

            latexEditorView.getController().setType(template);
            latexEditorView.getController().enact("create");

            new MainWindow(latexEditorView);
            frame.dispose();
        });
		createButton.setBounds(213, 196, 97, 25);
		return createButton;
	}

	private JButton getBackButton() {
		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			if (previousScreen.equals("main"))
				new MainWindow(latexEditorView);
			else
				new OpeningWindow();
			frame.dispose();
		});
		backButton.setBounds(46, 196, 97, 25);
		return backButton;
	}
}
