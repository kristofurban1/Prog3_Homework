package com.hq21tl_homework.recipe_book;

import java.util.List;

public class Recipe {
    private final Ingredient[] ingredients;
    private final String[] instructions;

    public Recipe(Ingredient[] ingredients, String[] instructions){
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public boolean hasIngridients(List<String> filter){
        for (Ingredient ingredient : ingredients) {
            if (!filter.contains(ingredient.getName())) return false;
        }
        return true;
    }
}
