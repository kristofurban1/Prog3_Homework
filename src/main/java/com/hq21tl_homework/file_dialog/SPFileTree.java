package com.hq21tl_homework.file_dialog;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;


public class SPFileTree extends JPanel {
    public void initialize(JComponent parent, JDialog root){
        setPreferredSize(null);
        setBackground(Color.GREEN.darker());
        parent.add(this);
    }
    
}
