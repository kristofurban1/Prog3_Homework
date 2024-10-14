package com.hq21tl_homework.file_dialog;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class SPFileTree extends JPanel {
    public void initialize(JComponent parent, MyFileDialog root){
        setPreferredSize(null);
        setBackground(Color.GREEN.darker());
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        parent.add(this);
    }
    
}
