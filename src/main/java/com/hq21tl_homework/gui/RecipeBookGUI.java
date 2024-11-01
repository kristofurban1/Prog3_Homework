package com.hq21tl_homework.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.recipe_book.RecipeBook;

public class RecipeBookGUI extends JFrame{
    Panel.WindowPanel windowPanel = new Panel.WindowPanel();

    private void buildGUI(){
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(650, 500));
        setTitle(Locales.getString("WindowTitle"));
        setLayout(new BorderLayout());
        windowPanel.initialize(null, this);
        Locales.eventHandler.fireLocalizationChanged();
    }

    public RecipeBookGUI(RecipeBook recipeBook){
        StateContainer.EntryCollectionState.setInstance(recipeBook);
        buildGUI();
    }

    public void showGUI(){
        setVisible(true);
    }
}
