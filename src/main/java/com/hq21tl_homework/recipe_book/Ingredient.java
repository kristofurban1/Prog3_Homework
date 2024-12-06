package com.hq21tl_homework.recipe_book;

import java.io.Serializable;

public class Ingredient implements Serializable {

    /**
     * Allows temporary modification of an Ingredient.
     * After modification is complete it can be built into an Ingredient.
     */
    public static class IngredientBuilder {
        public String name = "";
        public double amount = 0;
        public String quantifyer = "";

        public IngredientBuilder(){}
        public IngredientBuilder(Ingredient baseEntry){
            name = baseEntry.getName();
            amount = baseEntry.getAmount();
            quantifyer = baseEntry.getQuantifyer();
        }

        /**
         * @return An Ingredient object, containing all information set in Builder
         */
        public Ingredient build(){
            return new Ingredient(name, amount, quantifyer);
        }
        
        /**
         * Cleans up ingredients that are invalid.
         * @return True if this builder is invalid (name is blank).
         */
        public boolean cleanup() {
            return name.isBlank();
        }
    }
    private final String name;
    private final double amount;
    private final String quantifyer;

    public Ingredient(String name, double amount, String quantifyer){
        this.name = name;
        this.amount = amount;
        this.quantifyer = quantifyer;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getQuantifyer() {
        return quantifyer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ingredient ingr)
            return name.equals(ingr.getName());
        else return false;
    }

    public boolean preciseEquals(Ingredient other){
        return 
            name.equals(other.name) &&
            Double.compare(amount, other.amount) == 0 &&
            quantifyer.equals(other.quantifyer);
    }

    @Override
    public String toString() {
        return name + ": " + amount + quantifyer;
    }
    
    
}
