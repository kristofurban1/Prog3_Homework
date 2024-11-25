package com.hq21tl_homework.recipe_book;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.hq21tl_homework.error_dialog.ErrorDialog;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogBehaviour;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogType;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorDialogSettings;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorLevel;

public class RecipeBook implements Serializable{
    private  final ArrayList<RecipeEntry> recipes;

    public RecipeBook(){
        this.recipes = new ArrayList<>();
    }

    public RecipeBook(RecipeEntry[] recipes) {
        this.recipes = new ArrayList<>();
        if (recipes == null) return;
        this.recipes.addAll(Arrays.asList(recipes));
        sortRecipes();
    }

    public static RecipeBook importRecipeBook(String path){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))){
            return (RecipeBook)in.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            ErrorDialogSettings settings = new ErrorDialog.ErrorDialogSettings(
                "RecipeBook import error.", 
                ErrorLevel.FATAL, 
                DialogType.OK,
                DialogBehaviour.BLOCKING_DIALOG, 
                "Failed to deserialize RecipeBook", 
                String.join("\n", Arrays.toString(e.getStackTrace())));
            ErrorDialog dialog = new ErrorDialog(settings);
            dialog.showError();
            System.exit(1);
            return null;
        }
    }

    public static void exportRecipeBook(RecipeBook recipeBook, String path){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))){
            out.writeObject(recipeBook);
        }
        catch (IOException e) {
            ErrorDialogSettings settings = new ErrorDialog.ErrorDialogSettings(
                "RecipeBook export error.", 
                ErrorLevel.FATAL, 
                DialogType.OK,
                DialogBehaviour.BLOCKING_DIALOG, 
                "Failed to serialize RecipeBook", 
                String.join("\n", Arrays.toString(e.getStackTrace())));
            ErrorDialog dialog = new ErrorDialog(settings);
            dialog.showError();
            System.exit(1);
        }
    }


    public RecipeEntry getRecipe(String recipeName){
        for (RecipeEntry recipeEntry : recipes) {
            if (recipeEntry.getName().equals(recipeName)) return recipeEntry;
        }
        return null;
    }
    
    private void sortRecipes(){
        Collections.sort(recipes, (r1, r2) -> r1.getName().compareTo(r2.getName()));
    }

    public boolean addRecipe(RecipeEntry newEntry){
        // Filtering for duplicates
        for (RecipeEntry recipeEntry : recipes) {
            if (recipeEntry.getName().equals(newEntry.getName())){
                return recipeEntry.equals(newEntry); // If it completely equals skip it. (True signals no intervention nessesery by the caller.)
            }
        }

        recipes.add(newEntry);
        sortRecipes();
        return true;
    }

    public boolean updateRecipe(RecipeEntry updated){
        RecipeEntry old = getRecipe(updated.getName());
        if (old == null) return false;
        int index = recipes.indexOf(old);
        recipes.set(index, updated);  // No sorting needed, names match
        return true;
    }

    public void removeRecipe(String recipeName){
        RecipeEntry entry = getRecipe(recipeName);
        if (entry == null) return;
        recipes.remove(entry);
    }

    public RecipeEntry[] filterRecipesByName(String filter){
        // filtered doesnt need to be sorted, iterating over sorted array.
        ArrayList<RecipeEntry> filtered = new ArrayList<>();
        for (RecipeEntry entry : recipes) {
            if (entry.getName().toLowerCase().contains(filter.toLowerCase()))
                filtered.add(entry);
        }
        return filtered.toArray(RecipeEntry[]::new);
    }

    public RecipeEntry[] filterRecipesByCategory(String filter){
        // filtered doesnt need to be sorted, iterating over sorted array.
        ArrayList<RecipeEntry> filtered = new ArrayList<>();
        for (RecipeEntry entry : recipes) {
            if (entry.getCategory().toLowerCase().contains(filter.toLowerCase()))
                filtered.add(entry);
        }
        return filtered.toArray(RecipeEntry[]::new);
    }

    public RecipeEntry[] filterRecipesByAvailableIngredients(List<String> filter){
        ArrayList<RecipeEntry> match = new ArrayList<>(); 
        for (RecipeEntry recipe : recipes) {
            if (recipe.hasIngridients(filter)) match.add(recipe);
        }
        return match.toArray(RecipeEntry[]::new);
    }

    public String[] getIngredients(){
        ArrayList<String> ingredients = new ArrayList<>();
        for (RecipeEntry recipe : recipes) {
            for (String ingredient : recipe.getIngredients()) {
                if (!ingredients.contains(ingredient))
                    ingredients.add(ingredient); 
            }
        }
        Collections.sort(ingredients, Comparable::compareTo);
        return ingredients.toArray(String[]::new);
    }
    public String[] getQuantifyers(){
        ArrayList<String> quantifyers = new ArrayList<>();
        for (RecipeEntry recipe : recipes) {
            for (String quantifyer : recipe.getQuantifyers()) {
                if (!quantifyers.contains(quantifyer))
                    quantifyers.add(quantifyer); 
            }
        }
        Collections.sort(quantifyers, Comparable::compareTo);
        return quantifyers.toArray(String[]::new);
    }

    
}