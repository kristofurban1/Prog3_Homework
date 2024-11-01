package com.hq21tl_homework.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Locales.LocalizationChangeListener;
import com.hq21tl_homework.guiInitializable;
import com.hq21tl_homework.recipe_book.RecipeEntry;

public class EntryContainer extends JPanel implements guiInitializable<RecipeBookGUI>{

    public static class Entry extends JPanel implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener{

        private transient RecipeEntry recipeEntry;

        private final JLabel nameLabel = new JLabel();
        private final JLabel descriptionLabel = new JLabel();
        private final JButton viewButton = new JButton();
        private final JButton editButton = new JButton();

        private boolean selected = false;

        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
            setLayout(new GridBagLayout());
            nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            nameLabel.setText("nil");
            descriptionLabel.setText("nil");

            nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);


            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill =  GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = .3;
            constraints.insets = new Insets(10, 10, 5, 10);
            add(nameLabel, constraints);
            constraints.gridy = 1;
            constraints.weighty = 1;
            constraints.insets = new Insets(5, 20, 10, 20);
            add(descriptionLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.gridheight = 2;
            constraints.weightx = .01;
            constraints.insets = new Insets(20, 20, 20, 5);
            add(viewButton, constraints);
            constraints.gridx = 2;
            constraints.insets = new Insets(20, 5, 20, 20);
            add(editButton, constraints);
            
            parent.add(this);
            Locales.eventHandler.addValueChangeListener(this);

            localizationChanged();
        }

        @Override
        public void localizationChanged() {
            viewButton.setText(Locales.getString("EntryContainer_Entry_View"));
            editButton.setText(Locales.getString("EntryContainer_Entry_Edit"));
        }

        public void setSelected(boolean sel){
            setBackground(sel 
                    ? new Color(20,20,200,20)
                    : Color.WHITE
                );
            selected = sel;
        }
        public boolean isSelected(){
            return selected;
        }

        public void setRecipeEntry(RecipeEntry entry){
            recipeEntry = entry;
            nameLabel.setText(entry.getName() + " | " + entry.getCategory());
            descriptionLabel.setText(entry.getDescription());
        }
        public RecipeEntry getRecipeEntry(){
            return recipeEntry;
        }

        
    }

    private final JScrollPane scrollPane = new JScrollPane(this);
    @Override
    public void initialize(JComponent parent, RecipeBookGUI root) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        parent.add(scrollPane, BorderLayout.CENTER);
        StateContainer.EntryCollectionState.setInstance(this);
    }


}
