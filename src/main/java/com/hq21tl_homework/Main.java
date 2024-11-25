package com.hq21tl_homework;

import java.io.File;

import javax.swing.SwingUtilities;

import com.hq21tl_homework.gui.RecipeBookGUI;
import com.hq21tl_homework.recipe_book.RecipeBook;

public class Main {
    public static final String PERSISTENT_PATH = "persistent.ser";
    public static void main(String[] args) {
        File persistent = new File(PERSISTENT_PATH);
        if (!persistent.exists()){
            RecipeBook.exportRecipeBook(new RecipeBook(), PERSISTENT_PATH);
        }
        RecipeBook recipeBook = RecipeBook.importRecipeBook(PERSISTENT_PATH);
        
        RecipeBookGUI gui = new RecipeBookGUI(recipeBook);
        SwingUtilities.invokeLater(gui::showGUI);
    }
}
