package com.hq21tl_homework.gui.recipe_viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Locales.LocalizationChangeListener;
import com.hq21tl_homework.guiInitializable;
import com.hq21tl_homework.recipe_book.Ingredient;
import com.hq21tl_homework.recipe_book.Recipe;

public class RecipePanel extends JPanel implements guiInitializable<RecipeViewer>, LocalizationChangeListener{
    private transient Recipe recipe;
    private double multiplyer = 1;

    private final JLabel ingredientLabel = new JLabel();
    private final JLabel ingredientMultiplyerLabel = new JLabel();
    private final JTextField ingredientMultiplyer = new JTextField(multiplyer+"");
    private final JLabel instructionLabel = new JLabel();
    private final JPanel ingredientPanel = new JPanel();
    private final JPanel instructionPanel = new JPanel();

    @Override
    public void initialize(JComponent parent, RecipeViewer root) {     
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
        JPanel multipyerPanel = new JPanel();
        multipyerPanel.add(ingredientMultiplyerLabel);
        ingredientMultiplyer.setColumns(4);
        ingredientMultiplyer.getDocument().addDocumentListener(new DocumentListener() {

            private void updated(){
                try {
                    multiplyer = Double.parseDouble(ingredientMultiplyer.getText());
                } catch (NumberFormatException e) {
                    multiplyer = 1;
                }
                updateIngridientsPanel();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {updated();}
            @Override
            public void removeUpdate(DocumentEvent e) {updated();}
            @Override
            public void changedUpdate(DocumentEvent e) {/* Unnesesery */}
        });

        multipyerPanel.add(ingredientMultiplyer);
        panel1Top.add(multipyerPanel, BorderLayout.EAST);
        panel1Top.setBorder(new EmptyBorder(5,0,5,0));
        panel1.add(panel1Top, BorderLayout.NORTH);

        JScrollPane ingredentScrollPane = new JScrollPane(ingredientPanel);
        ingredentScrollPane.setHorizontalScrollBar(null);
        panel1.add(ingredentScrollPane, BorderLayout.CENTER);
        
        JPanel panel2Top = new JPanel();
        panel2Top.setLayout(new BorderLayout());
        panel2Top.add(instructionLabel, BorderLayout.WEST);
        panel2.add(panel2Top, BorderLayout.NORTH);
        panel2.add(new JScrollPane(instructionPanel), BorderLayout.CENTER);

        updateIngridientsPanel();
        updateInstructionPanel();
        Locales.eventHandler.addValueChangeListener(this);
        localizationChanged();
        revalidate();
        repaint();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void updateInstructionPanel(){
        instructionPanel.removeAll();
        instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
        
        for (int i = 0; i < recipe.getInstructions().length; i++) {
            JPanel entryPanel = new JPanel(new BorderLayout());
            int num = i+1;
            JPanel side = new JPanel();
            String padding = ""; 
            for(int j = 2; j > num / 10; j--) padding += ' '; //NOSONAR // Stringbuilder for max 3 appends?
            JLabel label = new JLabel(padding + num + ". ");
            side.add(label);
            entryPanel.add(side, BorderLayout.WEST);
            JTextArea instructionArea = new JTextArea();
            instructionArea.setText(recipe.getInstructions()[i]);
            instructionArea.setEditable(false);
            instructionArea.setBorder(new BevelBorder(BevelBorder.RAISED, Color.GRAY.brighter(), Color.GRAY.darker()));
            entryPanel.add(instructionArea, BorderLayout.CENTER);
            instructionPanel.add(entryPanel);
        }
        
        revalidate();
        repaint();
    }
    public void updateIngridientsPanel(){
        
        ingredientPanel.removeAll();
        ingredientPanel.setLayout(new BoxLayout(ingredientPanel, BoxLayout.Y_AXIS));
        for (Ingredient ingredient : recipe.getIngredients()) {
            JPanel entry = new JPanel();
            entry.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            JTextField name = new JTextField(ingredient.getName());
            name.setEditable(false);
            name.setBorder(new EmptyBorder(3,4,2,0));
            name.setColumns(12);
            entry.add(name);

            JTextField amount = new JTextField(""+(ingredient.getAmount() * multiplyer));
            amount.setEditable(false);
            amount.setBorder(new BevelBorder(BevelBorder.RAISED, Color.GRAY.brighter(), Color.GRAY.darker()));
            amount.setColumns(4);
            entry.add(amount);

            JTextField quantifyer = new JTextField(ingredient.getQuantifyer());
            quantifyer.setEditable(false);
            quantifyer.setColumns(4);
            quantifyer.setBorder(new EmptyBorder(3,4,2,4));
            entry.add(quantifyer);

            ingredientPanel.add(entry);
        }
        revalidate();
        repaint();
    }

    @Override
    public void localizationChanged() {
        ingredientLabel.setText(Locales.getString("RecipeViewer_Ingredients"));
        instructionLabel.setText(Locales.getString("RecipeViewer_Instructions"));
        ingredientMultiplyerLabel.setText(Locales.getString("RecipeViewer_Multiplyer"));
    }
}

