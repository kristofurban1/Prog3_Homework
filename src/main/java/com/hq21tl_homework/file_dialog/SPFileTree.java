package com.hq21tl_homework.file_dialog;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class SPFileTree extends JPanel{
    public void initilaize(JComponent parent){
        setPreferredSize(parent.getPreferredSize());
        setBackground(Color.GREEN.darker());
        parent.add(this);
    }
}
