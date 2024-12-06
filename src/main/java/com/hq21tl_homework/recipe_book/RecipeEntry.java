package com.hq21tl_homework.recipe_book;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hq21tl_homework.recipe_book.Recipe.RecipeBuilder;

public class RecipeEntry implements Serializable {

    /**
     * Allows temporary modification of a RecipeEntry.
     * After modification is complete it can be built into an RecipeEntry.
     */
    public static class RecipeEntryBuilder {
        public String name = "";
        public String category = "";
        public String description = "";
        public final List<RecipeBuilder> recipes;

        public RecipeEntryBuilder(){
            recipes = new ArrayList<>();
        }
        public RecipeEntryBuilder(RecipeEntry baseEntry){
            name = baseEntry.getName();
            category = baseEntry.getCategory();
            description = baseEntry.getDescription();
            recipes = new ArrayList<>();
            for (Recipe r : baseEntry.getRecipes()) {
                recipes.add(new RecipeBuilder(r));
            }
        }

        /**
         * @return A RecipeEntry object, containing all information set in Builder
         */
        public RecipeEntry build(){
            List<Recipe> builtRecipe = new ArrayList<>();
            for (RecipeBuilder recipe : recipes) {
                Recipe built = recipe.build();
                if (builtRecipe.contains(built)) continue;
                builtRecipe.add(built);
            }
            return new RecipeEntry(name, category, description, builtRecipe.toArray(Recipe[]::new));
        }
        
        /**
         * Removes recipes that are invalid (RecipeBuilder::cleanup)
         */
        public void cleanup() {
            recipes.removeIf(r -> r.cleanup());
        }
    }
    private final String name;
    private final String category;
    private final String description;

    private final Recipe[] recipes;
    
    public RecipeEntry(String name, String category, String description, Recipe[] recipes){
        this.name = name;
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

    /**
     * @param filter List of strings that are ingredient names
     * @return Returns true if has at least one recipe which satisfyes condition. (Recipe::hasIngridients)
     */
    public boolean hasIngridients(List<String> filter){
        for (Recipe recipe : recipes) {
            if (recipe.hasIngridients(filter)) return true;
        }
        return false;
    }

    /**
     * @return Distinct list of all ingredients used in this recipeEntry.
     */
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

    /**
     * @return Distinct list of all quantifyers used in this recipeEntry.
     */
    public String[] getQuantifyers(){
        ArrayList<String> quantifyers = new ArrayList<>();
        for (Recipe recipe : recipes) {
            for (String quantifier : recipe.getQuantifyers()) {
                if (!quantifyers.contains(quantifier))
                    quantifyers.add(quantifier);
            }
        }
        return quantifyers.toArray(String[]::new);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecipeEntry other){
            if (name.equals(other.name) &&
                category.equals(other.category) &&
                description.equals(other.description)){
                // Basic stuff correct
                int hasMatch = 0;
                Outer: //NOSONAR no.
                for (Recipe recipe : recipes){
                    for (Recipe otherRecipe : other.recipes){
                        if (recipe.equals(otherRecipe)){
                            hasMatch++;
                            continue Outer;
                        }
                    }
                }
                return hasMatch == recipes.length;
            }

        }
        return false;
    }

    
}
