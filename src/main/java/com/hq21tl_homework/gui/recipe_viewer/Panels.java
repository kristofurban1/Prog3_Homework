package com.hq21tl_homework.gui.recipe_viewer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.guiInitializable;
import com.hq21tl_homework.recipe_book.Recipe;
public class Panels {
    private Panels() {}

    public static class TopPanel extends JPanel implements guiInitializable<RecipeViewer>{
        private final transient JTextField nameField = new JTextField();
        private final transient JTextField categoryField = new JTextField();
        private final transient JTextArea descriptionField = new JTextArea();
        @Override
        public void initialize(JComponent parent, RecipeViewer root) {
            setLayout(new GridBagLayout());
            nameField.setText(root.entry.getName());
            categoryField.setText(root.entry.getCategory());
            descriptionField.setText(root.entry.getDescription());

            nameField.setEditable(false);
            categoryField.setEditable(false);
            descriptionField.setEditable(false);

            GridBagConstraints subConstraints = new GridBagConstraints();
            subConstraints.fill = GridBagConstraints.BOTH;
            subConstraints.gridx = 0;
            subConstraints.gridy = 0;
            subConstraints.weightx = .5;
            subConstraints.weighty = .2;
            subConstraints.gridwidth = 1;
            subConstraints.gridheight = 1;
            subConstraints.insets = new Insets(5, 10, 2, 10);
            JPanel nameBuffer = new JPanel();
            nameBuffer.setLayout(new BorderLayout());
            nameBuffer.add(nameField, BorderLayout.CENTER);
            add(nameBuffer, subConstraints);
            
            subConstraints.gridx = 1;
            JPanel categoryBuffer = new JPanel();
            categoryBuffer.setLayout(new BorderLayout());
            categoryBuffer.add(categoryField, BorderLayout.CENTER);
            add(categoryBuffer, subConstraints);
            
            subConstraints.gridx = 0;
            subConstraints.gridy = 1;
            subConstraints.gridwidth = 2;
            subConstraints.weightx = 1;
            subConstraints.weighty = 0.8;
            subConstraints.insets = new Insets(10, 10, 10, 10);
            JPanel decrBuffer = new JPanel();
            decrBuffer.setLayout(new BorderLayout());
            decrBuffer.add(descriptionField, BorderLayout.CENTER);
            add(new JScrollPane(decrBuffer), subConstraints);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = .25;
            parent.add(this, BorderLayout.NORTH);

            setSize(root.getWidth(), (int)(root.getHeight() * .3));
        }
        

    }
    
    public static class RecipeViewerPanel extends JPanel implements guiInitializable<RecipeViewer>, Locales.LocalizationChangeListener{
        
        private RecipeViewer root;
        private final JTabbedPane tabbedPane = new JTabbedPane();
        @Override
        public void initialize(JComponent parent, RecipeViewer root) {
            this.root = root;
            setLayout(new BorderLayout());
            add(tabbedPane, BorderLayout.CENTER);
            update();

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 1;
            parent.add(this, BorderLayout.CENTER);
        }
        @Override
        public void localizationChanged() {
            update();
        }

        public void update(){
            tabbedPane.removeAll();
            int idx = 0;
            for (Recipe recipe : root.entry.getRecipes()) {
                idx++;
                RecipePanel recipePanel = new RecipePanel();
                recipePanel.setRecipe(recipe);
                recipePanel.initialize(tabbedPane, root);
                tabbedPane.addTab(Locales.getString("RecipeViewer_TabName") + idx, recipePanel);
            }
        }
    }
    
    public static class BottomPanel extends JPanel implements guiInitializable<RecipeViewer>, Locales.LocalizationChangeListener{
        private final JButton closeBtn = new JButton();

        @Override
        public void initialize(JComponent parent, RecipeViewer root) {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            localizationChanged();
            add(closeBtn);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 0.01;
            parent.add(this, BorderLayout.SOUTH);
            Locales.eventHandler.addValueChangeListener(this);

            closeBtn.addActionListener(l ->
                root.dispose()
            );
        }

        @Override
        public void localizationChanged() {
            closeBtn.setText(Locales.getString("RecipeViewer_Close"));
        }

    }
    

    public static class WindowPanel extends JPanel implements guiInitializable<RecipeViewer>{

        private final TopPanel topPanel = new TopPanel();
        private final RecipeViewerPanel editorPanel = new RecipeViewerPanel();
        private final BottomPanel bottomPanel = new BottomPanel();

        @Override
        public void initialize(JComponent parent, RecipeViewer root) {
            removeAll();
            setLayout(new BorderLayout());
            topPanel.initialize(this, root);
            add(new JSeparator(SwingConstants.HORIZONTAL));
            editorPanel.initialize(this, root);
            bottomPanel.initialize(this, root);
            root.add(this, BorderLayout.CENTER);
        }
        
    }
}
