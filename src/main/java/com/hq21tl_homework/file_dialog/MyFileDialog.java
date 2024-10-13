package com.hq21tl_homework.file_dialog;

import com.hq21tl_homework.file_dialog.Panels.*;

import java.awt.Dialog;
import java.awt.Dimension;

import javax.swing.*;

public class MyFileDialog extends JDialog {

    private String path = null;
    private String selectedFile = null;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public String getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(String selectedFile) {
        this.selectedFile = selectedFile;
    }

    WindowPanel windowPanel = new WindowPanel();

    private void guiBuilder(){
        setMinimumSize(new Dimension(750, 500));
        setSize(new Dimension(750, 500));
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setAlwaysOnTop(true);
        setFocusable(true);
        
        windowPanel.initilaize(this);
        
        this.pack();
    }

    public MyFileDialog(JFrame parent, String dialogName){
        super(parent, dialogName, Dialog.ModalityType.APPLICATION_MODAL);
        guiBuilder();
    }

    public String openDialog(String path){
        setPath(path);
        setVisible(true);
        return null;
    }

    
}