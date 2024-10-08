package com.hq21tl_homework;

import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FileDialog{

    private static JDialog frame = null;
    private static void GUI_Builder(){
        if (frame != null) {
            frame.setVisible(true);
            return;
        }

        frame = new JDialog();
        frame.setSize(100, 300);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setTitle("FileDialog");

        JLabel _path_label = new JLabel("Path: ");
        frame.add(_path_label);

        JTextField path_field = new JTextField();
        frame.add(path_field);

        frame.setVisible(true);
    }
    public FileDialog(){
        GUI_Builder();
    }
}