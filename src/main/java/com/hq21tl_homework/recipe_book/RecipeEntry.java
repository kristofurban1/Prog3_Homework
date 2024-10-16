package com.hq21tl_homework.recipe_book;

import java.util.Comparator;

public class RecipeEntry {
    public static class NameComparator<T, U extends RecipeEntry> implements Comparator<U>{
        @Override
        public int compare(RecipeEntry o1, RecipeEntry o2) {
            return o1.getName().compareTo(o2.getName());
        }
        
    }

    private final String name;
    private final String category;
    private final String description;

    private final Recipe[] recipes;
    
    public RecipeEntry(String name, String category, String description, Recipe[] recipes){
        this.name = name ;
        this.category = category;
        this.description = description;
        this.recipes = recipes;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Recipe[] getRecipes() {
        return recipes;
    }
}
