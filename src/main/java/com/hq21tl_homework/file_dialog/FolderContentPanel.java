package com.hq21tl_homework.file_dialog;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.filechooser.FileSystemView;

import com.hq21tl_homework.file_dialog.MyFileDialog.FilePathChangeListener;

public class FolderContentPanel extends JScrollPane implements FilePathChangeListener{

    public static class FileEntry extends JPanel{
        private File thisFile = null;
        public void setThisFile(File file) { 
            thisFile = file; 
            Icon got = FileSystemView.getFileSystemView().getSystemIcon(file);
            BufferedImage img = new BufferedImage(icon.getIconHeight(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            icon.paintIcon(null, img.getGraphics(), 0, 0);
            icon.setImage(img);
            label.setText(file.getName());
        }

        private ImageIcon icon = new ImageIcon();
        private JLabel label = new JLabel();
        public void initialize(JComponent parent, MyFileDialog root){

        }
    }

    JLabel testLabel = new JLabel();
    MyFileDialog root = null;
    public void initialize(JComponent parent, MyFileDialog root){
        this.root = root;
        setBackground(Color.RED);
        setLayout(new ScrollPaneLayout());
        add(testLabel);


        setWheelScrollingEnabled(true);
        setVerticalScrollBar(new JScrollBar());

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
        testLabel.setText(root.getPath());
    }
}
