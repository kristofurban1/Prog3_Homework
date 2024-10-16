package com.hq21tl_homework.error_dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogResult;

public class DialogGUI {

    private DialogGUI() {
    }

    public static class MessagePanel extends JPanel {

        public void initialize(JComponent parent, ErrorDialog root) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(5, 10, 10, 10);
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = .25;
            parent.add(this, constraints);

            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(10, 20, 10, 10));

            JLabel iconLabel = new JLabel();
            iconLabel.setVerticalAlignment(SwingConstants.CENTER);
            iconLabel.setHorizontalAlignment(SwingConstants.LEADING);

            ImageIcon icon = root.getResource(root.getSettings().errorLevel());
            ImageIcon scaled = new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            iconLabel.setIcon(scaled);

            add(iconLabel, BorderLayout.WEST);

            JPanel textAreaPanel = new JPanel();
            textAreaPanel.setLayout(new GridBagLayout());
            textAreaPanel.setOpaque(false);
            add(textAreaPanel, BorderLayout.CENTER);
            
            JTextArea textArea = new JTextArea();
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setFont(UIManager.getFont("Label.font"));
            textArea.setBorder(UIManager.getBorder("Label.border"));
            textArea.setOpaque(false);
            textArea.setEditable(false);
            textArea.setText(root.getSettings().message());
            textArea.revalidate();
            
            FontMetrics metrics = textArea.getFontMetrics(textArea.getFont());
            int textHeight = (int)(metrics.getHeight() * textArea.getLineCount() * 1.1);
            textArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, textHeight));
            textArea.revalidate();
            repaint();

            GridBagConstraints textAreaConstraints = new GridBagConstraints();
            textAreaConstraints.fill = GridBagConstraints.HORIZONTAL;
            textAreaConstraints.insets = new Insets(5, 5, 5, 5);
            textAreaConstraints.gridx = 0;
            textAreaConstraints.gridy = 0;
            textAreaConstraints.weightx = 1;
            textAreaConstraints.weighty = 1;
            textAreaConstraints.anchor = GridBagConstraints.WEST;
            textAreaPanel.add(textArea, textAreaConstraints);
            
        }
    }

    public static class StackTracePanel extends JPanel {

        public void initialize(JComponent parent, ErrorDialog root) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(0, 10, 10, 10);
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weightx = 1;
            constraints.weighty = 1;
            parent.add(this, constraints);

            setLayout(new BorderLayout(0,5));

            JLabel stackTraceTitle = new JLabel("--StackTrace--");
            stackTraceTitle.setAlignmentX(SwingConstants.CENTER);
            stackTraceTitle.setAlignmentY(SwingConstants.CENTER);
            stackTraceTitle.setFont(new FontUIResource("Label.font", Font.BOLD, 16));
            add(stackTraceTitle, BorderLayout.NORTH);
            
            JTextArea textArea = new JTextArea();
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setFont(UIManager.getFont("Label.font"));
            textArea.setBorder(UIManager.getBorder("Label.border"));
            textArea.setOpaque(true);
            textArea.setEditable(false);
            textArea.setFont(new FontUIResource("Label.font", Font.BOLD, 9));
            textArea.setText(root.getSettings().stackTrace());
            
            JScrollPane scrollTextArea = new JScrollPane(textArea);
            
            add(scrollTextArea, BorderLayout.CENTER);
        }
    }

    public static class ButtonPanel extends JPanel {

        public void initialize(JComponent parent, ErrorDialog root) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.insets = new Insets(0, 10, 5, 10);
            constraints.gridx = 0;
            constraints.gridy = (root.hasStackTrace ? 2 : 1); // If has stacktrace this needs to be the third row
            constraints.weightx = 1;
            constraints.weighty = .001;
            parent.add(this, constraints);

            setLayout(new FlowLayout(FlowLayout.TRAILING, 10, 3));

            if (root.getSettings().dialogType() == ErrorDialog.DialogType.OK) {
                JButton okBtn = new JButton(Locales.getStringOrDefault("ErrorDialog_OK", "Ok"));
                okBtn.setMaximumSize(new Dimension(50, Integer.MAX_VALUE));
                add(okBtn);

                okBtn.addActionListener((ActionEvent e) -> {
                    root.setDialogResult(DialogResult.CANCEL);
                    root.dispose();
                });
            } else if (root.getSettings().dialogType() == ErrorDialog.DialogType.YES_NO
                    || root.getSettings().dialogType() == ErrorDialog.DialogType.YES_NO_CANCEL) {
                JButton yesBtn = new JButton(Locales.getStringOrDefault("ErrorDialog_Yes", "Yes"));
                yesBtn.setMaximumSize(new Dimension(50, Integer.MAX_VALUE));
                add(yesBtn);
                JButton noBtn = new JButton(Locales.getStringOrDefault("ErrorDialog_No", "No"));
                noBtn.setMaximumSize(new Dimension(50, Integer.MAX_VALUE));
                add(noBtn);

                yesBtn.addActionListener((ActionEvent e) -> {
                    root.setDialogResult(DialogResult.YES);
                    root.dispose();
                });
                noBtn.addActionListener((ActionEvent e) -> {
                    root.setDialogResult(DialogResult.NO);
                    root.dispose();
                });

                if (root.getSettings().dialogType() == ErrorDialog.DialogType.YES_NO_CANCEL){
                    JButton cancelBtn = new JButton(Locales.getStringOrDefault("ErrorDialog_Cancel", "Cancel"));
                    cancelBtn.setMaximumSize(new Dimension(50, Integer.MAX_VALUE));
                    add(cancelBtn);

                    cancelBtn.addActionListener((ActionEvent e) -> {
                        root.setDialogResult(DialogResult.CANCEL);
                        root.dispose();
                    });
                }
            }

            setMaximumSize(new Dimension(Integer.MAX_VALUE, getComponents()[0].getHeight() + 10));
        }
    }

    public static class WindowPanel extends JPanel {

        private final MessagePanel messagePanel = new MessagePanel();
        private final StackTracePanel stackTracePanel;
        private final ButtonPanel buttonPanel = new ButtonPanel();

        public WindowPanel(ErrorDialog root) {
            if (root.hasStackTrace) {
                stackTracePanel = new StackTracePanel();
            } else {
                stackTracePanel = null;
            }
        }

        public void initialize(ErrorDialog root) {
            setLayout(new GridBagLayout());

            messagePanel.initialize(this, root);
            if (root.hasStackTrace) {
                stackTracePanel.initialize(this, root);
            }
            buttonPanel.initialize(this, root);

            root.add(this, BorderLayout.CENTER);
        }
    }

}
