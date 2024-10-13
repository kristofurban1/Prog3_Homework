package com.hq21tl_homework.file_dialog;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.*;

public class FolderContentPanel extends JPanel{
    public void initialize(JComponent parent, MyFileDialog root){
        setBackground(Color.RED);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        parent.add(this, constraints);  
    }
}
