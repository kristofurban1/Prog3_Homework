package com.hq21tl_homework.file_dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.hq21tl_homework.Locales;

public class TopPanelObjects {
    private TopPanelObjects() {}

    public static class NavigateBackBtn extends JButton implements ActionListener, Locales.LocalizationChangeListener{
        private JComponent parentComponent = null;
        public void initialize(JComponent parent, JDialog root){
            parentComponent = parent;

            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 0.01;
            constraints.weighty = 1;
            
            setText("<");
            
            parent.add(this, constraints);
            addActionListener(this);

            /* 
             * 
             root.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateDimension();
                }
            });
            */
        } 

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());
        }
        /*
         * 
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
            
            */
        @Override
        public void localizationChanged() {
           //updateDimension();
        }

    }

    public static class PathPanel extends JPanel implements Locales.LocalizationChangeListener{
        private final JLabel pathLabel = new JLabel();
        private final JTextField inputField = new JTextField();

        public void initialize(JComponent parent, JDialog root){
            setLayout(new BorderLayout());
            setBackground(Color.YELLOW.darker());
            setBorder(new EmptyBorder(8,5,8,5));
            
            add(pathLabel, BorderLayout.WEST);
            add(inputField, BorderLayout.CENTER);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            
            parent.add(this, constraints);
            
            localizationChanged();
        }
        @Override
        public void localizationChanged() {
            pathLabel.setText(Locales.getString("FileDialog_PathLabel"));
        }
    }

    public static class NavigateGoBtn extends JButton implements ActionListener, Locales.LocalizationChangeListener{
        private JComponent parentComponent = null;
        public void initialize(JComponent parent, JDialog root){
            parentComponent = parent;
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            //updateDimension();
            
            setText(">");

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.weightx = 0.01;
            constraints.weighty = 1;
            
            parent.add(this, constraints);

            addActionListener(this);
            /*
             * 
             root.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateDimension();
                }
            });
            */
        }
        /*
         * 
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
            */

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());
        }


        @Override
        public void localizationChanged() {
            //updateDimension();   
        }
    }
    


}
