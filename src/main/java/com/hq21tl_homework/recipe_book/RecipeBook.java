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

    /**
     * Imports an entire recipebook from a java serialized file
     * @param path Path to serialized file.
     * @return Recipebook instance filled with data from serialized object.
     */
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

    /**
     * Exports an entire recipebook to a java serialized file
     * @param recipeBook Instance to serialize
     * @param path Path to serialized file.
     */
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


    /**
     * Gets the recipeEntry by its name
     * @param recipeName Name of the entry.
     * @return Entry instance, or null if there is no entry by that name in the recipebook.
     */
    public RecipeEntry getRecipe(String recipeName){
        for (RecipeEntry recipeEntry : recipes) {
            if (recipeEntry.getName().equals(recipeName)) return recipeEntry;
        }
        return null;
    }
    
    /**
     * Sorts the recipes into an ascending order by name
     */
    private void sortRecipes(){
        Collections.sort(recipes, (r1, r2) -> r1.getName().compareTo(r2.getName()));
    }

    /**
     * Adds a new recipe into recipebook. 
     * This fails if a recipeEntry by the same name is already in the recipebook. 
     * Exact matches are ignored and the operation succeeds.
     * This will also sort the recipeBook.
     * @param newEntry RecipeEntry to add
     * @return True if operation succeedes.
     */
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

    /**
     * Updates an entry in the recipebook to the new provided entry.
     * Update is based on the name of the entry.
     * @param updated Entry to update the one in the recipebook with.
     * @return True if operations succeeds. Operation fails if there is no entry by this name in the recipebook.
     */
    public boolean updateRecipe(RecipeEntry updated){
        RecipeEntry old = getRecipe(updated.getName());
        if (old == null) return false;
        int index = recipes.indexOf(old);
        recipes.set(index, updated);  // No sorting needed, names match
        return true;
    }

    /**
     * Removes a recipeEntry from the recipebook
     * @param recipeName Name of the entry to remove.
     */
    public void removeRecipe(String recipeName){
        RecipeEntry entry = getRecipe(recipeName);
        if (entry == null) return;
        recipes.remove(entry);
    }

    /**
     * @param filter String with a entire/partial Recipe Name. Case insensitive
     * @return Array of all entries in recipebook that has filter in name.
     */
    public RecipeEntry[] filterRecipesByName(String filter){
        // filtered doesnt need to be sorted, iterating over sorted array.
        ArrayList<RecipeEntry> filtered = new ArrayList<>();
        for (RecipeEntry entry : recipes) {
            if (entry.getName().toLowerCase().contains(filter.toLowerCase()))
                filtered.add(entry);
        }
        return filtered.toArray(RecipeEntry[]::new);
    }

    /**
     * @param filter String with a entire/partial Recipe Category. Case insensitive
     * @return Array of all entries in recipebook that has filter in category.
     */
    public RecipeEntry[] filterRecipesByCategory(String filter){
        // filtered doesnt need to be sorted, iterating over sorted array.
        ArrayList<RecipeEntry> filtered = new ArrayList<>();
        for (RecipeEntry entry : recipes) {
            if (entry.getCategory().toLowerCase().contains(filter.toLowerCase()))
                filtered.add(entry);
        }
        return filtered.toArray(RecipeEntry[]::new);
    }

    /**
     * @param filter List of strings that are ingredient names.
     * @return Array of all entries in recipebook that has recipe with matching ingredients (RecipeEntry::hasIngridients).
     */
    public RecipeEntry[] filterRecipesByAvailableIngredients(List<String> filter){
        ArrayList<RecipeEntry> match = new ArrayList<>(); 
        for (RecipeEntry recipe : recipes) {
            if (recipe.hasIngridients(filter)) match.add(recipe);
        }
        return match.toArray(RecipeEntry[]::new);
    }

    /**
     * @return Distinct list of all ingredients used in this recipebook.
     */
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

     /**
     * @return Distinct list of all quantifyers used in this recipebook.
     */
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