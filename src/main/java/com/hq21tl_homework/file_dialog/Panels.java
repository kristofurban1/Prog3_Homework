package com.hq21tl_homework.file_dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.file_dialog.TopPanelObjects.*;
import com.hq21tl_homework.file_dialog.BottomPanelObjects.*;

public class Panels {
    private Panels() {}

    public static class PathPanel extends JPanel implements Locales.LocalizationChangeListener{
        private final JLabel pathLabel = new JLabel();
        private final PathInputField inputField = new PathInputField();



        public void initialize(JComponent parent){
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.YELLOW.darker());

            inputField.initialize(this);

            localizationChanged();
            add(pathLabel, BorderLayout.WEST);
            parent.add(this, BorderLayout.CENTER);

        }
        @Override
        public void localizationChanged() {
            pathLabel.setText(Locales.getString("FileDialog_PathLabel"));
        }

    }

    public static class BottomPanel extends JPanel{
        private final FileNameInputField inputField = new FileNameInputField();
        private final ConfirmButton confirmButton = new ConfirmButton();
        private final CancelButton cancelButton = new CancelButton();

        public void initialize(JComponent parent){
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.YELLOW.darker());

            inputField.initialize(this);
            confirmButton.initialize(this);
            cancelButton.initialize(this);
            
            parent.add(this, BorderLayout.CENTER);
        }
    }

    public static class TopPanel extends JPanel{
        private final NavigateBackBtn backBt = new NavigateBackBtn();
        private final PathPanel pathPanel = new PathPanel();
        private final NavigateGoBtn goBtn = new NavigateGoBtn();
        
        public void initialize(JComponent parent){
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.YELLOW);
            setPreferredSize(new Dimension(
                parent.getPreferredSize().width,
                (int)(parent.getPreferredSize().height * .1)            
                ));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            backBt.initialize(this);
            pathPanel.initialize(this);
            goBtn.initialize(this);
            
            parent.add(this, BorderLayout.NORTH);
        }
    }
        
    public static class SidePanel extends JPanel{

        SPFileTree fileTree = new SPFileTree();

        public void initialize(JComponent parent){
            setLayout(new FlowLayout());
            setBackground(Color.GREEN);

            Component[] comps = parent.getComponents();
            TopPanel tp = null;
            
            for(Component c : comps){
                if (c instanceof TopPanel topPanel){
                    tp = topPanel;
                    break;
                }
            }
            assert tp != null;
            setPreferredSize(new Dimension(
                (int)(parent.getPreferredSize().width * .1),       
                parent.getPreferredSize().height - tp.getPreferredSize().height
                ));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            fileTree.initilaize(this);

            parent.add(this, BorderLayout.WEST);
        }
    }
    
    public static class CenterPanel extends JPanel{
        public void initialize(JComponent parent){
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.BLUE);
            setPreferredSize(new Dimension(
                parent.getPreferredSize().width,
                (int)(parent.getPreferredSize().height * .1)            
                ));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            parent.add(this, BorderLayout.CENTER);
        }
    }
    
    
}
