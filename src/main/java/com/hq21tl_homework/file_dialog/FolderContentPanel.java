package com.hq21tl_homework.file_dialog;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import com.hq21tl_homework.file_dialog.MyFileDialog.FilePathChangeListener;

public class FolderContentPanel extends JPanel implements FilePathChangeListener, MouseListener{

    public static class FileEntry extends JPanel{
        private File thisFile = null;
        private boolean selected = false;

        private Icon icon;
        private final JLabel label = new JLabel();
        public FileEntry(File file, JComponent parent, boolean selected){
            super();
            thisFile = file; 
            icon = FileSystemView.getFileSystemView().getSystemIcon(file);
            this.selected = selected;

            setLayout(new GridBagLayout());
            if (selected){
                setBorder(new LineBorder(Color.BLUE, 2));
                setBackground(new Color(10,10,200, 20));
            }

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            label.setIcon(icon);
            label.setText(thisFile.getName());
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            add(label, constraints);

            addMouseListener((MouseListener)parent);
        }

        public File getFile(){
            return thisFile;
        }
    }

    MyFileDialog root = null;
    public void initialize(JComponent parent, MyFileDialog root){
        this.root = root;
        setBackground(Color.RED);
        setLayout(new GridLayout(1, 5));
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setWheelScrollingEnabled(true);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        parent.add(scrollPane, constraints);
        root.getEventHandler().addValueChangeListener(this);
    }

    @Override
    public void filePathChanged() {
        File current = root.getFile();
        if (current == null) return; 
        File dir = null;
        if (current.isDirectory()){
            dir = current;
            current = null;
        }
        else{
            dir = current.getParentFile();
        }

        File[] files = dir.listFiles();
        int neededRows = ((files.length - 1) % 5) + 1;
        removeAll();
        setLayout(new GridLayout(neededRows, 5));

        System.out.println("Dir " + dir.getAbsolutePath());
        for (File file : files) {
            FileEntry entry = new FileEntry(file, this, file.equals(current));
            entry.addMouseListener(this);
            
            System.out.println("Initialized " + file.getName());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!(e.getSource() instanceof FileEntry)) {
            //TODO: Error
            System.out.println("FD_FCP_Mouse Click not on FileEntry");
            return;
        }
        FileEntry clicked = (FileEntry)e.getSource();

        System.out.println("ClickedEntry: " + clicked.getFile().getName());
        root.setFile(clicked.getFile());
        
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}

    
}
