package com.hq21tl_homework.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Locales.LocalizationChangeListener;
import com.hq21tl_homework.guiInitializable;

public class SearchBarComponents {

    private SearchBarComponents() {
    }

    public static class SearchField extends JTextField implements guiInitializable<RecipeBookGUI> {

        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 2;
            constraints.gridheight = 1;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.insets = new Insets(10, 10, 5, 2);

            parent.add(this, constraints);
            StateContainer.EntryCollectionState.setInstance(this);
        }

    }

    public static class RadioSearchName extends JRadioButton implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener, ActionListener {
        
    @Override
        public void localizationChanged() {
            setText(Locales.getString("Searchbar_RadioName"));
        }

        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            setHorizontalAlignment(SwingConstants.RIGHT);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.weightx = .9;
            constraints.weighty = .5;
            constraints.insets = new Insets(0, 10, 5, 10);
            constraints.anchor = GridBagConstraints.EAST;

            parent.add(this, constraints);
            Locales.eventHandler.addValueChangeListener(this);
            StateContainer.EntryCollectionState.radioButtonState.setInstance(this);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            StateContainer.EntryCollectionState.radioButtonState.setState(this);
        }
    }

    public static class RadioSearchCategory extends JRadioButton implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener, ActionListener{

        @Override
        public void localizationChanged() {
            setText(Locales.getString("Searchbar_RadioCategory"));
        }

        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            setHorizontalAlignment(SwingConstants.RIGHT);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.weightx = .1;
            constraints.weighty = .5;
            constraints.insets = new Insets(0, 10, 5, 10);
            constraints.anchor = GridBagConstraints.EAST;

            parent.add(this, constraints);
            Locales.eventHandler.addValueChangeListener(this);
            StateContainer.EntryCollectionState.radioButtonState.setInstance(this);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            StateContainer.EntryCollectionState.radioButtonState.setState(this);
        }

    }

    public static class SearchBtn extends JButton implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener, ActionListener{

        @Override
        public void localizationChanged() {
            setText(Locales.getString("Searchbar_Search"));
        }

        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 2;
            constraints.weightx = .05;
            constraints.weighty = 1;
            constraints.insets = new Insets(10, 2, 10, 2);

            parent.add(this, constraints);
            Locales.eventHandler.addValueChangeListener(this);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            StateContainer.EntryCollectionState.performUpdate();
        }

    }

    public static class ClearBtn extends JButton implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener, ActionListener{

        @Override
        public void localizationChanged() {
            setText(Locales.getString("Searchbar_Clear"));
        }

        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 3;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 2;
            constraints.weightx = .05;
            constraints.weighty = 1;
            constraints.insets = new Insets(10, 2, 10, 2);

            parent.add(this, constraints);

            Locales.eventHandler.addValueChangeListener(this);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            StateContainer.EntryCollectionState.setSearchType(0);
            StateContainer.EntryCollectionState.getSearchTerm().setText("");
            StateContainer.EntryCollectionState.radioButtonState.setState(null);
            StateContainer.EntryCollectionState.performUpdate();
        }

    }

    public static class IngridientSearchBtn extends JButton implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener, ActionListener {

        @Override
        public void localizationChanged() {
            setText(Locales.getString("Searchbar_IngredientSearch"));
        }

        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 4;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 2;
            constraints.weightx = .05;
            constraints.weighty = 1;
            constraints.insets = new Insets(10, 2, 10, 2);

            parent.add(this, constraints);

            Locales.eventHandler.addValueChangeListener(this);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] ingredients = 
                StateContainer.EntryCollectionState
                .getRecipeBookInsatnce()
                .getIngredients();
            
            IngredientSelector selector = new IngredientSelector(ingredients);
            StateContainer.EntryCollectionState.setIngredientFilter(selector.showGUI());
            StateContainer.EntryCollectionState.setSearchType(2);
            StateContainer.EntryCollectionState.performUpdate();
        }

    }
}
