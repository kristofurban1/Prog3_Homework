package com.hq21tl_homework.gui.recipe_editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Locales.LocalizationChangeListener;
import com.hq21tl_homework.gui.StateContainer;
import com.hq21tl_homework.gui.recipe_editor.Panels.RecipeEditorPanel;
import com.hq21tl_homework.guiInitializable;
import com.hq21tl_homework.recipe_book.Ingredient.IngredientBuilder;
import com.hq21tl_homework.recipe_book.Recipe.RecipeBuilder;

public class RecipePanel extends JPanel implements guiInitializable<RecipeEditor>, LocalizationChangeListener{
    private transient RecipeBuilder recipe;
    private JComponent parentComponent;

    private NameChangeListener nameChange = new NameChangeListener();
    private AmountChangedListener amountChange = new AmountChangedListener();
    private QuantityChangedListener quantityChange = new QuantityChangedListener();

    private final JLabel ingredientLabel = new JLabel();
    private final JLabel instructionLabel = new JLabel();
    private final JButton delBtn = new JButton();

    private final JPanel ingredientPanel = new JPanel();
    private final JPanel instructionPanel = new JPanel();

    @Override
    public void initialize(JComponent parent, RecipeEditor root) {
        this.parentComponent = parent;
        
        setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        add(panel1, BorderLayout.WEST);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        add(panel2, BorderLayout.CENTER);

        JPanel panel1Top = new JPanel();
        panel1Top.setLayout(new BorderLayout());
        panel1Top.add(ingredientLabel, BorderLayout.WEST);
        panel1Top.setBorder(new EmptyBorder(5,0,5,0));
        panel1.add(panel1Top, BorderLayout.NORTH);

        JScrollPane ingredentScrollPane = new JScrollPane(ingredientPanel);
        ingredentScrollPane.setHorizontalScrollBar(null);
        panel1.add(ingredentScrollPane, BorderLayout.CENTER);
        
        JPanel panel2Top = new JPanel();
        panel2Top.setLayout(new BorderLayout());
        panel2Top.add(instructionLabel, BorderLayout.WEST);
        panel2Top.add(delBtn, BorderLayout.EAST);
        panel2.add(panel2Top, BorderLayout.NORTH);
        panel2.add(new JScrollPane(instructionPanel), BorderLayout.CENTER);

        delBtn.addActionListener(l->{
            root.entryBuilder.recipes.remove(recipe);
            ((RecipeEditorPanel)(parent.getParent())).update(-1);
        });

        update();
        Locales.eventHandler.addValueChangeListener(this);
        localizationChanged();
        revalidate();
        repaint();
    }

    public void setRecipe(RecipeBuilder recipe) {
        this.recipe = recipe;
        nameChange.setRecipe(recipe);
        amountChange.setRecipe(recipe);
        quantityChange.setRecipe(recipe);
    }

    public void update(){
        
        ingredientPanel.removeAll();
        ingredientPanel.setLayout(new BoxLayout(ingredientPanel, BoxLayout.Y_AXIS));
        for (IngredientBuilder ingredient : recipe.ingredients) {
            JPanel entry = new JPanel();
            entry.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            JComboBox<String> name = new JComboBox<>();
            name.setEditable(true);
            name.setModel(new DefaultComboBoxModel<>(StateContainer.EntryCollectionState.getRecipeBookInsatnce().getIngredients()));
            name.setSelectedItem(ingredient.name);
            name.setBorder(new EmptyBorder(3,4,2,0));
            name.setPrototypeDisplayValue("                    ");
            name.setName(""+recipe.ingredients.indexOf(ingredient));
            entry.add(name);
            name.addItemListener(nameChange);

            JTextField amount = new JTextField(""+ingredient.amount);
            amount.setBorder(new BevelBorder(BevelBorder.RAISED, Color.GRAY.brighter(), Color.GRAY.darker()));
            amount.setColumns(5);
            Document doc = amount.getDocument();
            doc.putProperty("index", recipe.ingredients.indexOf(ingredient));
            doc.addDocumentListener(amountChange);
            entry.add(amount);

            JComboBox<String> quantifyer = new JComboBox<>();
            quantifyer.setEditable(true);
            quantifyer.setModel(new DefaultComboBoxModel<>(StateContainer.EntryCollectionState.getRecipeBookInsatnce().getQuantifyers()));
            quantifyer.setSelectedItem(ingredient.quantifyer);
            quantifyer.setBorder(new EmptyBorder(3,4,2,4));
            quantifyer.setPreferredSize(new Dimension(60,30));
            quantifyer.setName(""+recipe.ingredients.indexOf(ingredient));
            entry.add(quantifyer);
            quantifyer.addItemListener(quantityChange);

            JButton delete = new JButton("X");
            delete.setBorder(new EmptyBorder(3,4,2,5));
            delete.setPreferredSize(new Dimension(50,30));
            entry.add(delete);
            delete.setName(""+recipe.ingredients.indexOf(ingredient));
            delete.addActionListener(l->{
                recipe.ingredients.remove(Integer.parseInt(delete.getName()));
                update();                              
            });

            ingredientPanel.add(entry);
        }

        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(new EmptyBorder(10,10,10,10));
        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("+");
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));
        btnPanel.add(addBtn);
        ingredientPanel.add(btnPanel);
        addBtn.addActionListener(l-> {
            recipe.ingredients.add(new IngredientBuilder());
            update();
        });
        revalidate();
        repaint();
    }

    private static class NameChangeListener implements ItemListener {
        RecipeBuilder recipe;
        public void setRecipe(RecipeBuilder recipe) {
            this.recipe = recipe;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox<String> name = (JComboBox<String>)e.getSource();
            int index = Integer.parseInt(name.getName());
            recipe.ingredients.get(index).name = (String)(name.getSelectedItem());
        }
    }
    private static class AmountChangedListener implements DocumentListener {
        RecipeBuilder recipe;
        public void setRecipe(RecipeBuilder recipe) {
            this.recipe = recipe;
        }

        void update(DocumentEvent e){
            Document doc = e.getDocument();
            int index = (int)doc.getProperty("index");
            Double amount;
            try {
                String amountString = doc.getText(0, doc.getLength());
                amount = Double.valueOf(amountString);
            } catch (NumberFormatException | BadLocationException ex) {
                amount = 0.0;
            }
            recipe.ingredients.get(index).amount = amount;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update(e);
        }
    }
    private static class QuantityChangedListener implements ItemListener {
        RecipeBuilder recipe;
        public void setRecipe(RecipeBuilder recipe) {
            this.recipe = recipe;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox<String> quantity = (JComboBox<String>)e.getSource();
            int index = Integer.parseInt(quantity.getName());
            recipe.ingredients.get(index).quantifyer = (String)(quantity.getSelectedItem());
        }
    }
    
    @Override
    public void localizationChanged() {
        ingredientLabel.setText(Locales.getString("RecipeEditor_Ingredients"));
        instructionLabel.setText(Locales.getString("RecipeEditor_Instructions"));
        delBtn.setText(Locales.getString("RecipeEditor_DeleteRecipe"));
    }
}

