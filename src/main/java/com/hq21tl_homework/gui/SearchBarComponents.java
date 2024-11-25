package com.hq21tl_homework.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.hq21tl_homework.HintTextField;
import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Locales.LocalizationChangeListener;
import com.hq21tl_homework.guiInitializable;

public class SearchBarComponents {

    private SearchBarComponents() {
    }

    public static class SearchField extends HintTextField<JTextField> implements guiInitializable<RecipeBookGUI> {

        public SearchField(){
            super("Search...", new JTextField());
        }
        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            super.initialize();
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 2;
            constraints.gridheight = 1;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.insets = new Insets(10, 10, 5, 2);

            parent.add(this.base, constraints);
            StateContainer.EntryCollectionState.setInstance(this);
        }

    }

    public static class SortAndSearchTypePanel extends JPanel implements guiInitializable<RecipeBookGUI>{
        
        private final SortComboBox sortComboBox = new SortComboBox();
        private final OrderComboBox orderComboBox = new OrderComboBox();
        private final RadioSearchName radioSearchName = new RadioSearchName();
        private final RadioSearchCategory radioSearchCategory = new RadioSearchCategory();
        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            sortComboBox.initialize(this, root);
            orderComboBox.initialize(this, root);
            radioSearchName.initialize(this, root);
            radioSearchCategory.initialize(this, root);
            
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
        }
        
    }

    public static class SortComboBox extends JComboBox<String> implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener{
        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            addActionListener(l -> 
                StateContainer.EntryCollectionState.performUpdate()
            );
            
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.weightx = .1;
            constraints.weighty =  1;
            constraints.insets = new Insets(0, 10, 5, 10);
            constraints.anchor = GridBagConstraints.EAST;

            parent.add(this, constraints);
            StateContainer.EntryCollectionState.setInstance(this);
            Locales.eventHandler.addValueChangeListener(this);
        }

        @Override
        public void localizationChanged() {
            setModel(new DefaultComboBoxModel<>(new String[]{
                Locales.getString("Searchbar_SortCombo_Name"),
                Locales.getString("Searchbar_SortCombo_Category"),
            }));
        }
    }

    public static class OrderComboBox extends JComboBox<String> implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener{
        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            addActionListener(l -> 
                StateContainer.EntryCollectionState.performUpdate()
            );

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.weightx = .1;
            constraints.weighty =  1;
            constraints.insets = new Insets(0, 10, 5, 10);
            constraints.anchor = GridBagConstraints.WEST;

            parent.add(this, constraints);
            StateContainer.EntryCollectionState.setInstance(this);
            Locales.eventHandler.addValueChangeListener(this);
        }
        @Override
        public void localizationChanged() {
            setModel(new DefaultComboBoxModel<>(new String[]{
                Locales.getString("Searchbar_OrderCombo_Ascending"),
                Locales.getString("Searchbar_OrderCombo_Descending"),
            }));
        }
    }

    public static class RadioSearchName extends JRadioButton implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener, ActionListener {
        
    @Override
        public void localizationChanged() {
            
            setText(Locales.getString("Searchbar_RadioName"));
        }

        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.weightx =  1;
            constraints.weighty =  1;
            constraints.insets = new Insets(0, 10, 5, 10);
            constraints.anchor = GridBagConstraints.WEST;

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
            constraints.gridx = 3;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.weightx = .1;
            constraints.weighty =  1;
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
                .getRecipeBookInstance()
                .getIngredients();
            
            IngredientSelector selector = new IngredientSelector(ingredients);
            StateContainer.EntryCollectionState.setIngredientFilter(selector.showGUI());
            StateContainer.EntryCollectionState.setSearchType(2);
            StateContainer.EntryCollectionState.performUpdate();
        }

    }
}
