package com.hq21tl_homework.file_dialog;

import com.hq21tl_homework.*;
import com.hq21tl_homework.file_dialog.Panels.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;

import javax.swing.*;

public class FileDialog extends JDialog {

    

    private static FileDialog dialog = null;

    WindowPanel windowPanel = new WindowPanel();

    private void guiBuilder(){
        setMinimumSize(new Dimension(750, 500));
        setTitle(Locales.getString("FileDialog_WindowName"));
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setAlwaysOnTop(true);
        setFocusable(true);
        windowPanel.setPreferredSize(getSize());
        windowPanel.setLayout(new BorderLayout());
        windowPanel.setBackground(new Color(100, 100, 100));

        revalidate();
        repaint();
        windowPanel.initilaize(this);
        setSize(getMinimumSize());
        setVisible(true);

        ComponentEvent resizeEvent = new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED);
        dispatchEvent(resizeEvent);

        setSize(getMinimumSize().width + 10, getMinimumSize().height + 10);
        revalidate();
        repaint();
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