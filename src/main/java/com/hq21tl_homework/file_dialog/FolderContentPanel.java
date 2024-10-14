package com.hq21tl_homework.file_dialog;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hq21tl_homework.file_dialog.MyFileDialog.FilePathChangeListener;

public class FolderContentPanel extends JPanel implements FilePathChangeListener{
    JLabel testLabel = new JLabel();
    MyFileDialog root = null;
    public void initialize(JComponent parent, MyFileDialog root){
        this.root = root;
        setBackground(Color.RED);
        setLayout(new FlowLayout());
        add(testLabel);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        parent.add(this, constraints);  
        root.getEventHandler().addValueChangeListener(this);
    }

    @Override
    public void filePathChanged() {
        testLabel.setText(root.getPath());
    }
}
