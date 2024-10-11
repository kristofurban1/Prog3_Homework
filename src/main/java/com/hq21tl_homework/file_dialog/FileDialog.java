package com.hq21tl_homework.file_dialog;

import com.hq21tl_homework.*;
import com.hq21tl_homework.file_dialog.Panels.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class FileDialog extends JDialog{

    private static FileDialog dialog = null;


    JPanel windowPanel = new JPanel();

    TopPanel topPanel = new TopPanel();
    SidePanel sidePanel = new SidePanel();
    CenterPanel centerPanel = new CenterPanel();

    private void guiBuilder(){
        setMinimumSize(new Dimension(750, 500));
        setTitle(Locales.getString("FileDialog_WindowName"));
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setAlwaysOnTop(true);
        setFocusable(true);
        windowPanel.setPreferredSize(getSize());
        windowPanel.setLayout(new BorderLayout(2, 2));
        windowPanel.setBackground(new Color(100, 100, 100));


        topPanel.initialize(windowPanel);
        sidePanel.initialize(windowPanel);
        centerPanel.initialize(windowPanel);
        
        add(windowPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    private FileDialog(){
        super(new JFrame());
        guiBuilder();
    }

    public static void openDialog(){
        if (dialog == null) dialog = new FileDialog();
        else dialog.setVisible(true);
    }

    public static void disposeDialog(){
        dialog = null;
    }
}