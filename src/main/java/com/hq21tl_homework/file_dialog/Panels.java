package com.hq21tl_homework.file_dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.hq21tl_homework.file_dialog.TopPanelObjects.*;
import com.hq21tl_homework.file_dialog.BottomPanelObjects.*;


public class Panels {
    private Panels() {}

    public static class TopPanel extends JPanel{
        private final NavigateBackBtn backBt = new NavigateBackBtn();
        private final PathPanel pathPanel = new PathPanel();
        private final NavigateGoBtn goBtn = new NavigateGoBtn();
        
        public void initialize(JComponent parent, MyFileDialog root){
            setLayout(new GridBagLayout());
            setBackground(Color.YELLOW);
            setBorder(new EmptyBorder(2,5,2,5));
            
            pathPanel.initialize(this, root);
            goBtn.initialize(this, root);
            backBt.initialize(this, root);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridheight = 1;
            constraints.gridwidth = 2;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weighty = 0.05;
            constraints.weightx = 1;

            parent.add(this, constraints);
        }
    }
        
    public static class SidePanel extends JPanel {
        SPFileTree fileTree = new SPFileTree();

        public void initialize(JComponent parent, MyFileDialog root){
            setLayout(new FlowLayout());
            setBackground(Color.GREEN);
            
            fileTree.initialize(this, root);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 0.15;
            constraints.weighty = 1;

            parent.add(this, constraints);
        }
    }
    
    public static class BottomPanel extends JPanel {
        private final FileNameInputField inputField = new FileNameInputField();
        private final ConfirmButton confirmButton = new ConfirmButton();
        private final CancelButton cancelButton = new CancelButton();

        public void initialize(JComponent parent, MyFileDialog root){
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.BLUE.darker());

            inputField.initialize(this, root);
            confirmButton.initialize(this, root);
            cancelButton.initialize(this, root);
            
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 0.2;

            parent.add(this, constraints);  
        }
    }

    public static class CenterPanel extends JPanel {
        private final BottomPanel bottomPanel = new BottomPanel();
        private final FolderContentPanel folderContentPanel = new FolderContentPanel();

        public void initialize(JComponent parent, MyFileDialog root){
            setLayout(new GridBagLayout());
            setBackground(Color.BLUE);

            folderContentPanel.initialize(this, root);
            bottomPanel.initialize(this, root);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 1;

            parent.add(this, constraints);            
        }
        
    }
    
    public static class WindowPanel extends JPanel{
        TopPanel topPanel = new TopPanel();
        SidePanel sidePanel = new SidePanel();
        CenterPanel centerPanel = new CenterPanel();
        
        public void initilaize(MyFileDialog parent){
            
            setPreferredSize(parent.getSize());
            setLayout(new GridBagLayout());
            setBackground(new Color(100, 0, 100));

            topPanel.initialize(this, parent);
            sidePanel.initialize(this, parent);
            centerPanel.initialize(this, parent);
        
            parent.add(this, BorderLayout.CENTER);
        }
    }
    
    
}
