package com.hq21tl_homework.file_dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.hq21tl_homework.file_dialog.MyFileDialog.FilePathChangeListener;

public class TopPanelObjects {
    private TopPanelObjects() {}

    public static class NavigateBackBtn extends JButton implements ActionListener{
        MyFileDialog root = null;
        public void initialize(JComponent parent, MyFileDialog root){
            this.root = root;
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 0.01;
            constraints.weighty = 1;
            
            setText("<");
            
            parent.add(this, constraints);
            addActionListener(this);

        } 

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());

            File fileparent = root.getFile().getParentFile();
            if (fileparent == null) return;
            root.setFile(fileparent);
        }

    }

    public static class PathPanel extends JPanel implements FilePathChangeListener{
        private final JLabel pathLabel = new JLabel();
        private final JTextField inputField = new JTextField();

        MyFileDialog root = null;
        public void initialize(JComponent parent, MyFileDialog root){
            this.root = root;
            setLayout(new BorderLayout());
            setBackground(Color.YELLOW.darker());
            setBorder(new EmptyBorder(8,5,8,5));

            inputField.setText(root.getPath());
            
            add(pathLabel, BorderLayout.WEST);
            add(inputField, BorderLayout.CENTER);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            
            parent.add(this, constraints);
            
            root.getEventHandler().addValueChangeListener(this);
        }

        public String getInputText(){
            return inputField.getText();
        }
        @Override
        public void filePathChanged() {
            inputField.setText(root.getDirectoryPath());
        }
    }

    public static class NavigateGoBtn extends JButton implements ActionListener{
        private JComponent parentComponent = null;
        private PathPanel pathPanel = null;

        MyFileDialog root = null;
        public void initialize(JComponent parent, MyFileDialog root){
            this.root = root;
            parentComponent = parent;
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            
            setText(">");

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.weightx = 0.01;
            constraints.weighty = 1;
            
            parent.add(this, constraints);

            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());

            if (pathPanel == null){
                for (Component comp : parentComponent.getComponents()) {
                    if (comp instanceof PathPanel p){
                        pathPanel = p;
                        break;
                    } 
                }
                if (pathPanel == null) {
                    System.out.println("FD_NGB_UI_BROKEN");
                    return;
                }
            }

            root.setPath(pathPanel.getInputText());
        }
    }
    


}
