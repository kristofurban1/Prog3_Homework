package com.hq21tl_homework.file_dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.*;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.file_dialog.TopPanelObjects.*;
import com.hq21tl_homework.file_dialog.BottomPanelObjects.*;


public class Panels {
    private Panels() {}

    public static class PathPanel extends JPanel implements Locales.LocalizationChangeListener{
        private final JLabel pathLabel = new JLabel();
        private final PathInputField inputField = new PathInputField();

        public void initialize(JComponent parent, JDialog root){
            setLayout(new BorderLayout());
            setBackground(Color.YELLOW.darker());
            
            revalidate();
            repaint();

            inputField.initialize(this, root);
            add(pathLabel, BorderLayout.WEST);
            parent.add(this, BorderLayout.CENTER);
            
            localizationChanged();
        }
        @Override
        public void localizationChanged() {
            pathLabel.setText(Locales.getString("FileDialog_PathLabel"));
        }
    }

    public static class TopPanel extends JPanel{
        private JComponent parentComponent; 

        private final NavigateBackBtn backBt = new NavigateBackBtn();
        private final PathPanel pathPanel = new PathPanel();
        private final NavigateGoBtn goBtn = new NavigateGoBtn();
        
        public void initialize(JComponent parent, JDialog root){
            parentComponent = parent;
            setLayout(new BorderLayout());
            setBackground(Color.YELLOW);
            
            revalidate();
            repaint();
            pathPanel.initialize(this, root);
            goBtn.initialize(this, root);
            backBt.initialize(this, root);
            
            parent.add(this, BorderLayout.NORTH);
            root.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateDimension();
                }
            });
        }

        private void updateDimension(){
            if (parentComponent == null) return;
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setPreferredSize(new Dimension(
                parentComponent.getPreferredSize().width,
                (int)(parentComponent.getPreferredSize().height * .1)            
                ));
        }
    }
        
    public static class SidePanel extends JPanel {
        JComponent parentComponent;
        TopPanel tp = null;
        SPFileTree fileTree = new SPFileTree();

        public void initialize(JComponent parent, JDialog root){
            parentComponent = parent;
            setLayout(new FlowLayout());
            setBackground(Color.GREEN);

            Component[] comps = parent.getComponents();
            for(Component c : comps){
                if (c instanceof TopPanel topPanel){
                    tp = topPanel;
                    break;
                }
            }
            assert tp != null;
            updateDimension();
            
            revalidate();
            repaint();
            fileTree.initialize(this, root);

            parent.add(this, BorderLayout.WEST);
            root.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateDimension();
                }
            });
        }
        
        private void updateDimension(){
            if (parentComponent == null) return;
            setPreferredSize(new Dimension(
                (int)(parentComponent.getPreferredSize().width * .2),       
                parentComponent.getPreferredSize().height - tp.getPreferredSize().height
                ));
        }

    }
    
    public static class BottomPanel extends JPanel {
        private final FileNameInputField inputField = new FileNameInputField();
        private final ConfirmButton confirmButton = new ConfirmButton();
        private final CancelButton cancelButton = new CancelButton();

        private JComponent parentComponent = null;
        public void initialize(JComponent parent, JDialog root){
            parentComponent = parent;
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.BLUE.darker());

            revalidate();
            repaint();
            inputField.initialize(this, root);
            confirmButton.initialize(this, root);
            cancelButton.initialize(this, root);
            
            updateDimension();

            parent.add(this, BorderLayout.SOUTH);
            root.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateDimension();
                }
            });
        }
        private void updateDimension(){
            if (parentComponent == null) return;
            setPreferredSize(new Dimension(
                parentComponent.getPreferredSize().width,
                (int)(parentComponent.getPreferredSize().height * .8)            
                ));
        }


    }

    public static class CenterPanel extends JPanel {
        private final BottomPanel bottomPanel = new BottomPanel();
        private final FolderContentPanel folderContentPanel = new FolderContentPanel();

        public void initialize(JComponent parent, JDialog root){
            setLayout(new BorderLayout(1,1));
            setBackground(Color.BLUE);

            revalidate();
            repaint();
            folderContentPanel.initialize(this, root);
            bottomPanel.initialize(this, root);
            
            setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
            parent.add(this, BorderLayout.CENTER);
            
        }
        
    }
    
    public static class WindowPanel extends JPanel{
        TopPanel topPanel = new TopPanel();
        SidePanel sidePanel = new SidePanel();
        CenterPanel centerPanel = new CenterPanel();
        
        public void initilaize(JDialog parent){
            
            revalidate();
            repaint();
            topPanel.initialize(this, parent);
            sidePanel.initialize(this, parent);
            centerPanel.initialize(this, parent);
        
            parent.add(this, BorderLayout.CENTER);
        }
    }
    
    
}
