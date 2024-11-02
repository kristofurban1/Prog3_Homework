package com.hq21tl_homework.gui.recipe_editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hq21tl_homework.HintTextField;
import com.hq21tl_homework.Locales;
import com.hq21tl_homework.guiInitializable;
public class Panels {
    private Panels() {}

    public static class TopPanel extends JPanel implements guiInitializable<RecipeEditor>, Locales.LocalizationChangeListener{
        HintTextField<JTextField> nameField = new HintTextField<>(Locales.getString("RecipeEditor_NameHint"), new JTextField());
        HintTextField<JTextField> categoryField = new HintTextField<>(Locales.getString("RecipeEditor_CategoryHint"), new JTextField());
        HintTextField<JTextArea> descriptionField = new HintTextField<>(Locales.getString("RecipeEditor_DescriptionHint"), new JTextArea());
        @Override
        public void initialize(JComponent parent, RecipeEditor root) {
            setLayout(new GridBagLayout());
            setBackground(Color.BLUE);
            nameField.initialize();
            categoryField.initialize();
            descriptionField.initialize();

            GridBagConstraints subConstraints = new GridBagConstraints();
            subConstraints.fill = GridBagConstraints.BOTH;
            subConstraints.gridx = 0;
            subConstraints.gridy = 0;
            subConstraints.weightx = .5;
            subConstraints.weighty = .2;
            subConstraints.gridwidth = 1;
            subConstraints.gridheight = 1;
            subConstraints.insets = new Insets(10, 10, 10, 10);
            add(nameField.base, subConstraints);
            
            subConstraints.gridx = 1;
            add(categoryField.base, subConstraints);
            
            subConstraints.gridx = 0;
            subConstraints.gridy = 1;
            subConstraints.gridwidth = 2;
            subConstraints.weightx = 1;
            subConstraints.weighty = 1;
            add(descriptionField.base, subConstraints);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = .3;
            parent.add(this, constraints);
            Locales.eventHandler.addValueChangeListener(this);
        }
        @Override
        public void localizationChanged() {
            nameField.setHint(Locales.getString("RecipeEditor_NameHint"));
            categoryField.setHint(Locales.getString("RecipeEditor_CategoryHint"));
            descriptionField.setHint(Locales.getString("RecipeEditor_DescriptionHint"));
        }
        

    }
    
    public static class RecipeEditorPanel extends JPanel implements guiInitializable<RecipeEditor> {

        @Override
        public void initialize(JComponent parent, RecipeEditor root) {
            setBackground(Color.GREEN);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 1;
            parent.add(this, constraints);
        }

    }
    
    public static class BottomPanel extends JPanel implements guiInitializable<RecipeEditor> {

        @Override
        public void initialize(JComponent parent, RecipeEditor root) {
            setBackground(Color.yellow);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 0.01;
            parent.add(this, constraints);
        }

    }
    

    public static class WindowPanel extends JPanel implements guiInitializable<RecipeEditor>{

        private static final TopPanel topPanel = new TopPanel();
        private static final RecipeEditorPanel editorPanel = new RecipeEditorPanel();
        private static final BottomPanel bottomPanel = new BottomPanel();

        @Override
        public void initialize(JComponent parent, RecipeEditor root) {
            setLayout(new GridBagLayout());
            topPanel.initialize(this, root);
            editorPanel.initialize(this, root);
            bottomPanel.initialize(this, root);
            root.add(this, BorderLayout.CENTER);
        }
        
    }
}
