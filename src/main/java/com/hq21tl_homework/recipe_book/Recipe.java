package com.hq21tl_homework.recipe_book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hq21tl_homework.recipe_book.Ingredient.IngredientBuilder;

public class Recipe {
    public static class RecipeBuilder {

        
        public final List<IngredientBuilder> ingredients;
        public final List<String> instructions;

        public RecipeBuilder(){
            ingredients = new ArrayList<>();
            instructions = new ArrayList<>();
        }
        public RecipeBuilder(Recipe baseEntry){
            ingredients = new ArrayList<>();
            for (Ingredient i : baseEntry.getIngredients()) {
                ingredients.add(new IngredientBuilder(i));
            }
            instructions = new ArrayList<>();
            instructions.addAll(Arrays.asList(baseEntry.getInstructions()));
        }

        public Recipe build(){
            List<Ingredient> builtIngredient = new ArrayList<>();
            for (IngredientBuilder i : ingredients) {
                builtIngredient.add(i.build());
            }
            return new Recipe(builtIngredient.toArray(Ingredient[]::new), instructions.toArray(String[]::new));
        }

        public boolean cleanup() {
            ingredients.removeIf(i -> i.cleanup());
            instructions.removeIf(String::isBlank);

            return ingredients.isEmpty() && instructions.isEmpty();
        }
    }
    private final Ingredient[] ingredients;
    private final String[] instructions;

    public Recipe(Ingredient[] ingredients, String[] instructions){
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }
    public String[] getQuantifyers() {
        ArrayList<String> quantifyers = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            quantifyers.add(ingredient.getQuantifyer());
        }
        return quantifyers.toArray(String[]::new);
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
