package com.hq21tl_homework.recipe_book;

import java.util.ArrayList;
import java.util.List;

public class RecipeEntry {
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

    public boolean hasIngridients(List<String> filter){
        for (Recipe recipe : recipes) {
            if (recipe.hasIngridients(filter)) return true;
        }
        return false;
    }
    public String[] getIngredients(){
        ArrayList<String> ingredients = new ArrayList<>();
        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (!ingredients.contains(ingredient.getName()))
                    ingredients.add(ingredient.getName());
            }
        }
        return ingredients.toArray(String[]::new);
    }
}
