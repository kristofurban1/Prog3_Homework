package com.hq21tl_homework.file_dialog;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class SPFileTree extends JPanel {
    public void initialize(JComponent parent, MyFileDialog root){
        setPreferredSize(null);
        setBackground(Color.GREEN.darker());
        
        revalidate();
        repaint();
        parent.add(this);
    }
    
}
