package com.hq21tl_homework.file_dialog;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;

public class FolderContentPanel extends JPanel{
    public void initialize(JComponent parent, JDialog root){
        setBackground(Color.RED);

        parent.add(this, BorderLayout.CENTER);
    }
}
