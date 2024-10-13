package com.hq21tl_homework.file_dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

import com.hq21tl_homework.Locales;

public class TopPanelObjects {
    private TopPanelObjects() {}

    public static class NavigateBackBtn extends JButton implements ActionListener, Locales.LocalizationChangeListener{
        private JComponent parentComponent = null;
        public void initialize(JComponent parent, JDialog root){
            parentComponent = parent;

            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            updateDimension();
            parentComponent.add(this, BorderLayout.WEST);
            addActionListener(this);

            root.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateDimension();
                }
            });
        } 

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());
        }

        private void updateDimension(){
            if (parentComponent == null) return;
            setText("^");
            
            setMinimumSize(getPreferredSize());
            setPreferredSize(new Dimension(
                (int)(parentComponent.getPreferredSize().width * .05), 
                parentComponent.getPreferredSize().height
            ));

            if (getMinimumSize().width > getPreferredSize().width)
                setPreferredSize(getMinimumSize());
        }

        @Override
        public void localizationChanged() {
           updateDimension();
        }

    }

    public static class PathInputField extends JTextField implements Locales.LocalizationChangeListener{
        public void initialize(JComponent parent, JDialog root){
            parent.add(this, BorderLayout.CENTER);
        }

        @Override
        public void localizationChanged() {
            
        }

    }

    public static class NavigateGoBtn extends JButton implements ActionListener, Locales.LocalizationChangeListener{
        private JComponent parentComponent = null;
        public void initialize(JComponent parent, JDialog root){
            parentComponent = parent;
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            updateDimension();
            parentComponent.add(this, BorderLayout.EAST);

            addActionListener(this);
           root.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateDimension();
                }
            });
        }

        private void updateDimension(){
            if (parentComponent == null) return;
            setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
            setText(">");
            setPreferredSize(new Dimension(
                (int)(parentComponent.getPreferredSize().width * .05), 
                parentComponent.getPreferredSize().height
                ));
                
            if (getMinimumSize().width > getPreferredSize().width)
                setPreferredSize(getMinimumSize());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());
        }

        @Override
        public void localizationChanged() {
            updateDimension();   
        }
    }
    


}
