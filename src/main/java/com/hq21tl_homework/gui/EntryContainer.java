package com.hq21tl_homework.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Locales.LocalizationChangeListener;
import com.hq21tl_homework.Main;
import com.hq21tl_homework.error_dialog.ErrorDialog;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorLevel;
import com.hq21tl_homework.gui.recipe_editor.RecipeEditor;
import com.hq21tl_homework.gui.recipe_viewer.RecipeViewer;
import com.hq21tl_homework.guiInitializable;
import com.hq21tl_homework.recipe_book.RecipeBook;
import com.hq21tl_homework.recipe_book.RecipeEntry;

public class EntryContainer extends JPanel implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener{

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

            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    setSelected(!isSelected());
                    ((EntryContainer)parent).updateSelectedCheckBox();
                }

                @Override
                public void mousePressed(MouseEvent e) {/* Unnesesery */}
                @Override
                public void mouseReleased(MouseEvent e) {/* Unnesesery */}
                @Override
                public void mouseEntered(MouseEvent e) {/* Unnesesery */}
                @Override
                public void mouseExited(MouseEvent e) {/* Unnesesery */}
                
            });

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

            viewButton.addActionListener(l -> 
                new RecipeViewer(recipeEntry)
            );

            editButton.addActionListener(l -> {
                RecipeEditor editor = new RecipeEditor(recipeEntry);
                RecipeEntry newEntry = editor.showGUI();
                if (newEntry == null){ // Cancelled / Deleted
                    StateContainer.EntryCollectionState.performUpdate(); // In case of deletion
                    return;
                }
                if(recipeEntry.getName().equals(newEntry.getName()))
                    StateContainer.EntryCollectionState.getRecipeBookInstance().updateRecipe(newEntry);
                else{
                    if (!StateContainer.EntryCollectionState.getRecipeBookInstance().addRecipe(newEntry)){
                       ErrorDialog dialog = new ErrorDialog(ErrorLevel.INFO, "Entry already exists.", "Entry with name " + newEntry.getName() + " alreay exists.");
                       dialog.showError();
                       return;
                    }
                    StateContainer.EntryCollectionState.getRecipeBookInstance().removeRecipe(recipeEntry.getName());
                }
                StateContainer.EntryCollectionState.performUpdate();
                RecipeBook.exportRecipeBook(StateContainer.EntryCollectionState.getRecipeBookInstance(), Main.PERSISTENT_PATH); 
            });
            localizationChanged();
        }

        @Override
        public void localizationChanged() {
            viewButton.setText(Locales.getString("EntryContainer_Entry_View"));
            editButton.setText(Locales.getString("EntryContainer_Entry_Edit"));
        }

        public void setSelected(boolean sel){
            setBackground(sel 
                    ?new Color(37, 147, 255)
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
    private final JCheckBox selectCheckBox = new JCheckBox();
    @Override
    public void initialize(JComponent parent, RecipeBookGUI root) {
        JPanel wrapper = new JPanel(new BorderLayout());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        wrapper.add(scrollPane, BorderLayout.CENTER);

        selectCheckBox.setSelected(false);
        selectCheckBox.addActionListener(l->{
            if (selectCheckBox.isSelected()) selectAllEntries();
            else clearSelectedRecipes();
        });
        wrapper.add(new JLabel().add(selectCheckBox), BorderLayout.NORTH);

        parent.add(wrapper, BorderLayout.CENTER);
        StateContainer.EntryCollectionState.setInstance(this);
        Locales.eventHandler.addValueChangeListener(this);
    }

    public RecipeEntry[] getSelectedRecipes(){
        List<RecipeEntry> selectedEntries = new ArrayList<>();
        for (Component component : getComponents()) {
            if (!(component instanceof Entry)) continue;
            Entry entry = (Entry) component;
            if (entry.isSelected()) selectedEntries.add(entry.getRecipeEntry());
        }
        return selectedEntries.toArray(RecipeEntry[]::new);
    }

    public void clearSelectedRecipes(){
        for (Component component : getComponents()) {
            if (!(component instanceof Entry)) continue;
            ((Entry)component).setSelected(false);
        }
        repaint();
    }

    public void selectAllEntries(){
        for (Component component : getComponents()) {
            if (!(component instanceof Entry)) continue;
            ((Entry)component).setSelected(true);
        }
        repaint();
    }

    @Override
    public void localizationChanged() {
        selectCheckBox.setText(Locales.getString("EntryContainer_CkeckAll"));
    }

    public void updateSelectedCheckBox(){
        boolean all = true;
        for (Component component : getComponents()) {
            if (!(component instanceof Entry)) continue;
           if (!((Entry)component).isSelected()) all = false;
        }
        selectCheckBox.setSelected(all);
    }

}
