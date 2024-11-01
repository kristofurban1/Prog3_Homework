package com.hq21tl_homework;

import java.io.File;

import com.hq21tl_homework.gui.RecipeBookGUI;
import com.hq21tl_homework.recipe_book.Ingredient;
import com.hq21tl_homework.recipe_book.Recipe;
import com.hq21tl_homework.recipe_book.RecipeBook;
import com.hq21tl_homework.recipe_book.RecipeEntry;

public class Main {
    /* 
     * 
     private static void createAndShowGUI() {
        JFrame frame = new JFrame(Locales.getString("Title"));
        frame.setLayout(new FlowLayout(FlowLayout.LEADING));
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        JTextField tf = new JTextField();
        JLabel label = new JLabel();
        JButton button = new JButton(Locales.getString("Btn"));
        
        
        button.setSize(300, 50);
        tf.setMinimumSize(new Dimension(300, 50));
        tf.setPreferredSize(new Dimension(300, 50));
        label.setSize(50,50);
        
        
        button.addActionListener(e -> {
            String p = tf.getText();
            MyFileDialog fd = new MyFileDialog(frame,Locales.getString("FileDialog_WindowName"));
            File got = fd.openDialog(p);
            if (got != null) label.setText(got.getAbsolutePath());
        });
        
        frame.add(tf);
        frame.add(button);
        frame.add(label);
        
        //Display the window.
        frame.setVisible(true);
    }
    */
 
    public static void main(String[] args) {
        RecipeBook recipeBook = new RecipeBook(new File("null"));
        recipeBook.addRecipe(new RecipeEntry(
            "Test",
            "category",
            "A good lil test",
            new Recipe[] {
                new Recipe(
                    new Ingredient[]{
                        new Ingredient("Ingridient1", 100, "g"),
                        new Ingredient("Ingridient2", 200, "mg"),
                        new Ingredient("Ingridient3", 300, "l"),
                    }, 
                    new String[]{
                        "Step1"
                    }
                )
            }
        ));
        /* 
         * 
         recipeBook.addRecipe(new RecipeEntry("Test2", "null", "A shit ton o description", null));
         recipeBook.addRecipe(new RecipeEntry("Test3", "null", "A shit ton o description", null));
         recipeBook.addRecipe(new RecipeEntry("Test4", "null", "A shit ton o description", null));
         recipeBook.addRecipe(new RecipeEntry("Test5", "null", "A shit ton o description", null));
         recipeBook.addRecipe(new RecipeEntry("Test6", "null", "A shit ton o description", null));
         recipeBook.addRecipe(new RecipeEntry("Test7", "null", "A shit ton o description", null));
         */
        RecipeBookGUI gui = new RecipeBookGUI(recipeBook);
        javax.swing.SwingUtilities.invokeLater(gui::showGUI);
    }
}