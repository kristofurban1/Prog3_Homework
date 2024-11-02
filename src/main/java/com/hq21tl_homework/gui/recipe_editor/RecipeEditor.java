package com.hq21tl_homework.gui.recipe_editor;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;

import javax.swing.JDialog;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.gui.recipe_editor.Panels.WindowPanel;
import com.hq21tl_homework.recipe_book.RecipeEntry;

public class RecipeEditor extends JDialog {
    private final Panels.WindowPanel windowPanel = new WindowPanel();
    private final transient RecipeEntry.RecipeEntryBuilder entryBuilder;
    private void buildGUI(){
        setTitle(Locales.getString("RecipeEditor_Title"));
        setMinimumSize(new Dimension(800, 400));
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        windowPanel.initialize(null, this);
    }
    public RecipeEditor(RecipeEntry recipeEntry){
        if (recipeEntry == null)
            this.entryBuilder = new RecipeEntry.RecipeEntryBuilder();
        else 
            this.entryBuilder = new RecipeEntry.RecipeEntryBuilder(recipeEntry);

        buildGUI();
    }
    public RecipeEntry showGUI(){
        setVisible(true);
        return entryBuilder.build();
    }
}
