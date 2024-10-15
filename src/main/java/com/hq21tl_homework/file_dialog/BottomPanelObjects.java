package com.hq21tl_homework.file_dialog;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.file_dialog.Panels.BottomPanel;

public class BottomPanelObjects {
    private BottomPanelObjects() {}

    public static class FileNameInputField extends JTextField implements MyFileDialog.FilePathChangeListener{
        
        private MyFileDialog root = null;
        public void initialize(JComponent parent, MyFileDialog root){
            this.root = root;
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.insets = new Insets(0,5,0,5);
            parent.add(this, constraints);

            root.getEventHandler().addValueChangeListener(this);

            getDocument().addDocumentListener(new DocumentListener(){
                @Override
                public void changedUpdate(DocumentEvent e) {
                    textChanged();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    textChanged();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    textChanged();
                }

            });
        }
        
        private void textChanged(){
            return;
            /* 
             * 
             if (root.getFile() == null) {
                return;
            }
            root.setPath(root.getDirectoryPath() + getText());
            */
            
        }

        @Override
        public void filePathChanged() {
             if (root.getFile() == null) {
                setText("");
                return;
            }
            if (root.getFile().isDirectory())
                setText("");
            else
                setText(root.getFile().getName());
        }
    }

    public static class ConfirmButton extends JButton implements ActionListener, Locales.LocalizationChangeListener{
        private JComponent parentComponent = null;
        private MyFileDialog root = null;
        public void initialize(JComponent parent, MyFileDialog root){
            this.parentComponent = parent;
            this.root = root;
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = .2;
            constraints.weighty = 1;
            constraints.insets = new Insets(0,5,0,5);
            parent.add(this, constraints);
            
            addActionListener(this);
            Locales.eventHandler.addValueChangeListener(this);
            localizationChanged();
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());

            
            if (root.getFile() == null) {
                setEnabled(false);
                return;
            }
            if (root.getFile().isDirectory()){
                String filename = ((BottomPanel)parentComponent).getFileNameInputField().getText();
                if (filename.isBlank()){
                    //TODO: ErrorMsg
                    System.out.println("FileDialog_PathIsNotFile");
                    return;
                }
                String dirPath = root.getPath();
                String path = dirPath + "\\" + filename;
                root.setPath(path);
            }
            root.dispose();
        }
        @Override
        public void localizationChanged() {
            setText(Locales.getString("FileDialog_Confirm"));
        }
    }

    public static class CancelButton extends JButton implements ActionListener, Locales.LocalizationChangeListener{

        private MyFileDialog root = null;
        public void initialize(JComponent parent, MyFileDialog root){
            this.root = root;
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = .2;
            constraints.weighty = 1;
            constraints.insets = new Insets(0,5,0,5);
            parent.add(this, constraints);
            
            addActionListener(this);
            Locales.eventHandler.addValueChangeListener(this);
            localizationChanged();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            root.setFile(null);
            root.dispose();
        }

        @Override
        public void localizationChanged() {
            setText(Locales.getString("FileDialog_Cancel"));
        }
    }
}
