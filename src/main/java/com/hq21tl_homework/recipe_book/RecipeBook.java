package com.hq21tl_homework.recipe_book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RecipeBook {
    private final ArrayList<RecipeEntry> recipes;

    public RecipeBook(RecipeEntry[] recipes) {
        this.recipes = new ArrayList<>();
        if (recipes == null) return;
        this.recipes.addAll(Arrays.asList(recipes));
        sortRecipes();
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
            if (recipeEntry.getName().equals(newEntry.getName()))
                return false;
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

    public RecipeEntry[] filterRecipesByAvailableIngredients(String filter){
        //TODO
    }
}