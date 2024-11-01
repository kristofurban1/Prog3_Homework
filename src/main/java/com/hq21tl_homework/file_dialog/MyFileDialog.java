package com.hq21tl_homework.file_dialog;

import java.awt.Dialog;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.hq21tl_homework.file_dialog.FileDialogPanels.WindowPanel;

public class MyFileDialog extends JDialog {
    public static interface FilePathChangeListener {
        void filePathChanged();
    }
    public static class FilePathChangeEventHandler {
        private final List<FilePathChangeListener> listeners = new ArrayList<>();

        // Method to add listeners
        public void addValueChangeListener(FilePathChangeListener listener) {
            listeners.add(listener);
        }

        // Method to remove listeners
        public void removeValueChangeListener(FilePathChangeListener listener) {
            listeners.remove(listener);
        }
        // Fire event to all registered listeners
        public void fireFilePathChanged() {
            for (FilePathChangeListener listener : listeners) {
                listener.filePathChanged();
            }
        }
    }
    

    private final FilePathChangeEventHandler eventHandler = new FilePathChangeEventHandler(); //NOSONAR // This will not be under such load that it would require serialiation.
    public FilePathChangeEventHandler getEventHandler() {return eventHandler; }

    private File inputFile;

    public File getFile(){
        return inputFile;
    }

    public void setFile(File file){
        inputFile = file;
        eventHandler.fireFilePathChanged();
    }

    public String getDirectoryPath(){
        if (inputFile == null) return null;
        if (inputFile.isDirectory()) return inputFile.getAbsolutePath();
        return inputFile.getParent();
    }

    public String getPath() {
        if (inputFile == null) return null;
        return inputFile.getAbsolutePath();
    }

    public void setPath(String path) {
        this.inputFile = new File(path);
        eventHandler.fireFilePathChanged();
    }


    WindowPanel windowPanel = new WindowPanel();

    private void guiBuilder(){
        setMinimumSize(new Dimension(750, 500));
        setSize(new Dimension(750, 500));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setFocusable(true);
        
        windowPanel.initilaize(this);
        
        this.pack();
    }

    public MyFileDialog(JFrame parent, String dialogName){
        super(parent, dialogName, Dialog.ModalityType.APPLICATION_MODAL);
        guiBuilder();
    }

    public File openDialog(String path){
        if (path == null || path.isBlank())
            path = System.getProperty("user.dir");
        
        setPath(path);
        setVisible(true);
        return getFile();
    }
}