package com.hq21tl_homework.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Main;
import com.hq21tl_homework.recipe_book.RecipeBook;

public class RecipeBookGUI extends JFrame{
    Panel.WindowPanel windowPanel = new Panel.WindowPanel();

    private void buildGUI(){
        StateContainer.EntryCollectionState.setInstance(this);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(650, 500));
        setTitle(Locales.getString("WindowTitle"));
        setLayout(new BorderLayout());
        windowPanel.initialize(null, this);
        Locales.eventHandler.fireLocalizationChanged();
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {/* Unnesesery */}
            @Override
            public void windowClosing(WindowEvent e) {
                RecipeBook.exportRecipeBook(StateContainer.EntryCollectionState.getRecipeBookInstance(), Main.PERSISTENT_PATH); 
            }

            @Override
            public void windowClosed(WindowEvent e){/* Unnesesery */}
            @Override
            public void windowIconified(WindowEvent e) { /* Unnesesery */}
            @Override
            public void windowDeiconified(WindowEvent e) {/* Unnesesery */}
            @Override
            public void windowActivated(WindowEvent e) {/* Unnesesery */}
            @Override
            public void windowDeactivated(WindowEvent e) {/* Unnesesery */}
            
        });
    }

    public RecipeBookGUI(RecipeBook recipeBook){
        StateContainer.EntryCollectionState.setInstance(recipeBook);
        buildGUI();
    }



    public void showGUI(){
        setVisible(true);
    }
}
