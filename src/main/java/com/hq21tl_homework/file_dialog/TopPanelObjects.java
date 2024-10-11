package com.hq21tl_homework.file_dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.hq21tl_homework.Locales;

public class TopPanelObjects {
    private TopPanelObjects() {}

    public static class NavigateBackBtn extends JButton implements ActionListener, Locales.LocalizationChangeListener{
        private JComponent parentComponent = null;
        public void initialize(JComponent parent){
            parentComponent = parent;

            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
            localizationChanged();
            parentComponent.add(this, BorderLayout.WEST);
            addActionListener(this);
        } 

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());
        }

        @Override
        public void localizationChanged() {
            if (parentComponent == null) return;
            setText(Locales.getString("FileDialog_NavigateBack"));
            setMinimumSize(getPreferredSize());
            setPreferredSize(new Dimension(
                (int)(parentComponent.getPreferredSize().width * .05), 
                parentComponent.getPreferredSize().height
            ));
        }
    }

    public static class PathInputField extends JTextField implements Locales.LocalizationChangeListener{
        public void initialize(JComponent parent){
            

            parent.add(this, BorderLayout.CENTER);
        }

        @Override
        public void localizationChanged() {
            
        }
    }

    public static class NavigateGoBtn extends JButton implements ActionListener, Locales.LocalizationChangeListener{
        private JComponent parentComponent = null;
        public void initialize(JComponent parent){
            parentComponent = parent;

            setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
            localizationChanged();
            parentComponent.add(this, BorderLayout.EAST);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());
        }

        @Override
        public void localizationChanged() {
            if (parentComponent == null) return;
            setText(Locales.getString("FileDialog_Navigate"));setMinimumSize(getPreferredSize());
            setPreferredSize(new Dimension(
                (int)(parentComponent.getPreferredSize().width * .05), 
                parentComponent.getPreferredSize().height
                ));
        }
    }
    


}
