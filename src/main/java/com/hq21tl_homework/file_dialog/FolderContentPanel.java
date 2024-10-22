package com.hq21tl_homework.file_dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import com.hq21tl_homework.file_dialog.MyFileDialog.FilePathChangeListener;

public class FolderContentPanel extends JScrollPane implements FilePathChangeListener, MouseListener{

    public static class FileEntry extends JPanel{

        
        private File thisFile = null;
        private Icon icon;
        
        private final JLabel label = new JLabel();
        public FileEntry(File file, JComponent parent, boolean selected){
            super();
            thisFile = file; 
            icon = FileSystemView.getFileSystemView().getSystemIcon(file);

            //setLayout(new BorderLayout());
            if (selected){
                setBorder(new LineBorder(Color.BLUE, 2));
                setBackground(new Color(10,10,200, 20));
            }
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            setAlignmentX(Component.CENTER_ALIGNMENT);

            label.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            label.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
            label.setIcon(icon);
            label.setText(thisFile.getName());
            //label.setHorizontalTextPosition(SwingConstants.CENTER);
            //label.setVerticalTextPosition(SwingConstants.BOTTOM);
            //label.setVerticalAlignment(SwingConstants.TOP);
            //label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.CENTER);
        }

        public File getFile(){
            return thisFile;
        }
    }

    JPanel fileConent = new JPanel();
    MyFileDialog root = null;
    public void initialize(JComponent parent, MyFileDialog root){
        this.root = root;
        setBackground(Color.RED);
        //JScrollPane scrollPane = new JScrollPane(this);

        setWheelScrollingEnabled(true);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        parent.add(this, constraints);
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
        fileConent.removeAll();

        fileConent.setLayout(new BoxLayout(fileConent, BoxLayout.Y_AXIS));
        
        fileConent.validate();
        fileConent.doLayout();

        for (File file : files) {
            
            FileEntry entry = new FileEntry(file, this, file.equals(current));
            entry.addMouseListener(this);
            fileConent.add(entry);
        }

        fileConent.revalidate();
        fileConent.repaint();
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
    public void mouseEntered(MouseEvent e) {
        // Added by MouseListener, unnesessery to implement.
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // Added by MouseListener, unnesessery to implement.
    }
    @Override
    public void mousePressed(MouseEvent e) {
        // Added by MouseListener, unnesessery to implement.
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // Added by MouseListener, unnesessery to implement.
    }

    
}
