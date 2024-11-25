package com.hq21tl_homework.gui.recipe_viewer;

import java.awt.BorderLayout;
import java.awt.Dialog;

import javax.swing.JDialog;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.gui.recipe_viewer.Panels.WindowPanel;
import com.hq21tl_homework.recipe_book.RecipeEntry;

public class RecipeViewer extends JDialog {
    private final Panels.WindowPanel windowPanel = new WindowPanel();
    public final transient RecipeEntry entry;
    private void buildGUI(){ 
        setTitle(Locales.getString("RecipeEditor_Title"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        windowPanel.initialize(null, this);
    }
    public RecipeViewer(RecipeEntry recipeEntry){
        entry = recipeEntry;
        if (entry == null) return;
        buildGUI();
        setVisible(true);
    }
}
