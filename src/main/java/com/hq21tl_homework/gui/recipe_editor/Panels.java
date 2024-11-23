package com.hq21tl_homework.gui.recipe_editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hq21tl_homework.HintTextField;
import com.hq21tl_homework.Locales;
import com.hq21tl_homework.error_dialog.ErrorDialog;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogBehaviour;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogResult;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogType;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorLevel;
import com.hq21tl_homework.gui.StateContainer;
import com.hq21tl_homework.guiInitializable;
import com.hq21tl_homework.recipe_book.Recipe;
import com.hq21tl_homework.recipe_book.Recipe.RecipeBuilder;
public class Panels {
    private Panels() {}

    public static class TopPanel extends JPanel implements guiInitializable<RecipeEditor>, Locales.LocalizationChangeListener{
        private final transient HintTextField<JTextField> nameField = new HintTextField<>(Locales.getString("RecipeEditor_NameHint"), new JTextField());
        private final transient HintTextField<JTextField> categoryField = new HintTextField<>(Locales.getString("RecipeEditor_CategoryHint"), new JTextField());
        private final transient HintTextField<JTextArea> descriptionField = new HintTextField<>(Locales.getString("RecipeEditor_DescriptionHint"), new JTextArea());
        @Override
        public void initialize(JComponent parent, RecipeEditor root) {
            setLayout(new GridBagLayout());
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
            subConstraints.insets = new Insets(5, 10, 2, 10);
            JPanel nameBuffer = new JPanel();
            nameBuffer.setLayout(new BorderLayout());
            nameBuffer.add(nameField.base, BorderLayout.CENTER);
            add(nameBuffer, subConstraints);
            
            subConstraints.gridx = 1;
            JPanel categoryBuffer = new JPanel();
            categoryBuffer.setLayout(new BorderLayout());
            categoryBuffer.add(categoryField.base, BorderLayout.CENTER);
            add(categoryBuffer, subConstraints);
            
            subConstraints.gridx = 0;
            subConstraints.gridy = 1;
            subConstraints.gridwidth = 2;
            subConstraints.weightx = 1;
            subConstraints.weighty = 0.8;
            subConstraints.insets = new Insets(10, 10, 10, 10);
            JPanel decrBuffer = new JPanel();
            decrBuffer.setLayout(new BorderLayout());
            decrBuffer.add(descriptionField.base, BorderLayout.CENTER);
            add(new JScrollPane(decrBuffer), subConstraints);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = .25;
            parent.add(this, BorderLayout.NORTH);
            Locales.eventHandler.addValueChangeListener(this);

            nameField.setText(root.entryBuilder.name);
            categoryField.setText(root.entryBuilder.category);
            descriptionField.setText(root.entryBuilder.description);


            nameField.base.getDocument().addDocumentListener(new EditorEvents.NameChanged(nameField, root));
            categoryField.base.getDocument().addDocumentListener(new EditorEvents.CategoryChanged(categoryField, root));
            descriptionField.base.getDocument().addDocumentListener(new EditorEvents.DescriptionChanged(descriptionField, root));

            setSize(root.getWidth(), (int)(root.getHeight() * .3));
        }
        @Override
        public void localizationChanged() {
            nameField.setHint(Locales.getString("RecipeEditor_NameHint"));
            categoryField.setHint(Locales.getString("RecipeEditor_CategoryHint"));
            descriptionField.setHint(Locales.getString("RecipeEditor_DescriptionHint"));
        }
        

    }
    
    public static class RecipeEditorPanel extends JPanel implements guiInitializable<RecipeEditor>, Locales.LocalizationChangeListener{
        private RecipeEditor root;
        private final JTabbedPane tabbedPane = new JTabbedPane();
        @Override
        public void initialize(JComponent parent, RecipeEditor root) {
            this.root = root;
            setLayout(new BorderLayout());
            add(tabbedPane, BorderLayout.CENTER);
            update(-1);

            //setBackground(Color.GREEN);
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
            update(-1);
        }

        public void update(int setTo){
            tabbedPane.removeAll();
            int idx = 0;
            for (Recipe.RecipeBuilder recipe : root.entryBuilder.recipes) {
                idx++;
                RecipePanel recipePanel = new RecipePanel();
                recipePanel.setRecipe(recipe);
                recipePanel.initialize(tabbedPane, root);
                tabbedPane.addTab(Locales.getString("RecipeEditor_TabName") + idx, recipePanel);
            }
            
            tabbedPane.addTab("Add Recipe", new JPanel());
            JLabel addLabel = new JLabel(Locales.getString("RecipeEditor_AddRecipe"));
            addLabel.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    root.entryBuilder.recipes.add(new RecipeBuilder());
                    update(tabbedPane.getTabCount()-1);
                }
                @Override
                public void mousePressed(MouseEvent e) { /* Unnessesery */ }
                @Override
                public void mouseReleased(MouseEvent e) { /* Unnessesery */ }
                @Override
                public void mouseEntered(MouseEvent e) { /* Unnessesery */ }
                @Override
                public void mouseExited(MouseEvent e) { /* Unnessesery */ }
                
            });
            tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, addLabel);
            if (setTo >= 0 && setTo < tabbedPane.getTabCount()) {
                tabbedPane.setSelectedIndex(setTo);
            }
        }
    }
    
    public static class BottomPanel extends JPanel implements guiInitializable<RecipeEditor>, Locales.LocalizationChangeListener{
        private final JButton okBtn = new JButton();
        private final JButton cancelBtn = new JButton();
        private final JButton deleteBtn = new JButton();

        @Override
        public void initialize(JComponent parent, RecipeEditor root) {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            localizationChanged();
            add(okBtn);
            add(cancelBtn);
            if (root.entry != null) add(deleteBtn);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 0.01;
            parent.add(this, BorderLayout.SOUTH);
            Locales.eventHandler.addValueChangeListener(this);

            okBtn.addActionListener(l->{

                root.entryBuilder.cleanup();

                root.cancelled = false;
                root.dispose();
            });
            cancelBtn.addActionListener(l ->
                root.dispose()
            );
            deleteBtn.addActionListener(l -> {
                ErrorDialog dialog = new ErrorDialog(new ErrorDialog.ErrorDialogSettings(
                    Locales.getString("RecipeEditor_DeleteBtn_Title"), 
                    ErrorLevel.INFO, 
                    DialogType.YES_NO, 
                    DialogBehaviour.BLOCKING_DIALOG, 
                    Locales.getString("RecipeEditor_DeleteBtn_Message"), 
                    null));
                ErrorDialog.DialogResult result = dialog.showError();
                if (result == DialogResult.YES){
                    StateContainer.EntryCollectionState
                        .getRecipeBookInsatnce()
                        .removeRecipe(root.entry.getName());
                    root.dispose();
                }
            });
        }

        @Override
        public void localizationChanged() {
            okBtn.setText(Locales.getString("RecipeEditor_OkBtn"));
            cancelBtn.setText(Locales.getString("RecipeEditor_CancelBtn"));
            deleteBtn.setText(Locales.getString("RecipeEditor_DeleteBtn"));
        }

    }
    

    public static class WindowPanel extends JPanel implements guiInitializable<RecipeEditor>{

        private static final TopPanel topPanel = new TopPanel();
        private static final RecipeEditorPanel editorPanel = new RecipeEditorPanel();
        private static final BottomPanel bottomPanel = new BottomPanel();

        @Override
        public void initialize(JComponent parent, RecipeEditor root) {
            setLayout(new BorderLayout());
            topPanel.initialize(this, root);
            add(new JSeparator(JSeparator.HORIZONTAL));
            editorPanel.initialize(this, root);
            bottomPanel.initialize(this, root);
            root.add(this, BorderLayout.CENTER);
        }
        
    }
}
