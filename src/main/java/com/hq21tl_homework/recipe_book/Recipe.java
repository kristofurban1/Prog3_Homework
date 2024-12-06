package com.hq21tl_homework.recipe_book;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hq21tl_homework.recipe_book.Ingredient.IngredientBuilder;

public class Recipe implements Serializable {

    /**
     * Allows temporary modification of a Recipe.
     * After modification is complete it can be built into an Recipe.
     */
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

        /**
         * @return A Recipe object, containing all information set in Builder
         */
        public Recipe build(){
            List<Ingredient> builtIngredient = new ArrayList<>();
            for (IngredientBuilder i : ingredients) {
                builtIngredient.add(i.build());
            }
            return new Recipe(builtIngredient.toArray(Ingredient[]::new), instructions.toArray(String[]::new));
        }

        /**
         * Cleans up recipes that are invalid.
         * Also removes ingredients that are invalid (IngredientBuilder::cleanup), and blank Instructions
         * @return True if this builder is invalid (has no ingredients nor instructions).
         */
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

    /**
     * @return Distinct list of all quantifyers used in this recipe.
     */
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

    /**
     * @param filter List of strings that are ingredient names.
     * @return True if this recipe's ingredients are all in the filter. (Filter contains all ingredients)
     */
    public boolean hasIngridients(List<String> filter){
        for (Ingredient ingredient : ingredients) {
            if (!filter.contains(ingredient.getName())) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Recipe other){
            int hasMatchIngredient = 0;
            Outer: //NOSONAR no.
            for (Ingredient ingr : ingredients){
                for (Ingredient otherIngr : other.ingredients){
                    if (ingr.preciseEquals(otherIngr)){
                        hasMatchIngredient++;
                        continue Outer;
                    }
                }
            }

            int hasMatchInstruction = 0;
            Outer2: //NOSONAR no.
            for (String instr : instructions){
                for (String otherInstr : other.instructions){
                    if (instr.equals(otherInstr)){
                        hasMatchInstruction++;
                        continue Outer2;
                    }
                }
            }
            return hasMatchIngredient == ingredients.length && hasMatchInstruction == instructions.length;
        }
        return false;
    }

    
}
