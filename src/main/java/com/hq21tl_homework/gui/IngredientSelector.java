package com.hq21tl_homework.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.hq21tl_homework.Locales;

public class IngredientSelector extends JDialog{

    private String searchTerm = "";
    private final HashMap<String, Boolean> ingredientMap = new HashMap<>();
    private final JPanel ingredientPanel = new JPanel();

    private void buildGUI(){
        removeAll();
        setTitle(Locales.getString("IngredientSelector_Title"));
        setSize(200, 400);
        setMaximumSize(getSize());
        setResizable(false);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new GridBagLayout());

        JPanel searchBar = new JPanel();
        searchBar.setLayout(new GridBagLayout());

        JTextField searchField = new JTextField();
        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.insets = new Insets(5,5,5,2);
            searchBar.add(searchField, constraints);
        }

        JButton searchBtn = new JButton();
        searchBtn.setText(Locales.getString("IngredientSelector_SearchBtn"));
        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.weightx = .001;
            constraints.weighty = 1;
            constraints.insets = new Insets(5,2,5,5);
            searchBar.add(searchBtn, constraints);
        }
        searchBtn.addActionListener(l -> {
            searchTerm = searchField.getText();
            listIngredients();
        });

        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = .02;
            windowPanel.add(searchBar, constraints);
        }

        ingredientPanel.setLayout(new BoxLayout(ingredientPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollpane = new JScrollPane(ingredientPanel);
        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weightx = 1;
            constraints.weighty = 1;
            windowPanel.add(scrollpane, constraints);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        JButton toggle = new JButton();
        toggle.setText(Locales.getString("IngredientSelector_ToggleBtn"));
        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.VERTICAL;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.anchor = GridBagConstraints.WEST;
            buttonPanel.add(toggle, constraints);
        }
        toggle.addActionListener(l->{
            for (String ingredient : ingredientMap.keySet()) {
                ingredientMap.put(ingredient, !ingredientMap.get(ingredient));
            }
            listIngredients();
        });

        JButton okButton = new JButton();
        okButton.setText(Locales.getString("IngredientSelector_OkBtn"));
        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.VERTICAL;
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.anchor = GridBagConstraints.EAST;
            buttonPanel.add(okButton, constraints);
        }
        okButton.addActionListener(l->dispose());
        
        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.weightx = 1;
            constraints.weighty = .001;
            windowPanel.add(buttonPanel, constraints);
        }

        add(windowPanel, BorderLayout.CENTER);
    }

    private void listIngredients(){
        ingredientPanel.removeAll();
        for (Entry<String, Boolean> ingredientEntry : ingredientMap.entrySet()) {
            if (!ingredientEntry.getKey().toLowerCase().contains(searchTerm.toLowerCase()                                                                                                                                   )) continue;

            JCheckBox checkbox = new JCheckBox();
            checkbox.setText(ingredientEntry.getKey());
            checkbox.setSelected(ingredientEntry.getValue());
            checkbox.addActionListener(l -> 
                ingredientMap.put(checkbox.getText(), checkbox.isSelected())
            );
            ingredientPanel.add(checkbox);
        }
        ingredientPanel.revalidate();
        ingredientPanel.repaint();
    }

    public IngredientSelector(String[] ingredients){
        for (String ingredient : ingredients) {
            ingredientMap.put(ingredient, false);
        }
        buildGUI();
        listIngredients();
    }
    public String[] showGUI(){
        setVisible(true);
        final ArrayList<String> selectedIngredients = new ArrayList<>();
        for (String ingredient : ingredientMap.keySet()) {
            if (Boolean.TRUE.equals(ingredientMap.get(ingredient)))
                selectedIngredients.add(ingredient);
        }
        return selectedIngredients.toArray(String[]::new);
    }
}