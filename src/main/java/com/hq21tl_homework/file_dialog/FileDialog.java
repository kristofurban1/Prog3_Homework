package com.hq21tl_homework.file_dialog;

import com.hq21tl_homework.*;
import com.hq21tl_homework.file_dialog.Panels.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;

import javax.swing.*;

public class FileDialog extends JDialog {

    

    private static FileDialog dialog = null;

    WindowPanel windowPanel = new WindowPanel();

    private void guiBuilder(){
        setMinimumSize(new Dimension(750, 500));
        setSize(new Dimension(750, 500));
        setTitle(Locales.getString("FileDialog_WindowName"));
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setAlwaysOnTop(true);
        setFocusable(true);
        
        windowPanel.initilaize(this);
        
        this.pack();
        setVisible(true);
    }

    private FileDialog(JFrame parent){
        super(parent, "", Dialog.ModalityType.APPLICATION_MODAL);
        guiBuilder();
    }

    public static void openDialog(JFrame parent){
        if (dialog == null) dialog = new FileDialog(parent);
        else dialog.setVisible(true);
    }

    public static void disposeDialog(){
        dialog = null;
    }
}