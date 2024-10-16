package com.hq21tl_homework.recipe_book;

public class Recipe {
    private final Ingredient[] ingredients;
    private final String[] instructions;
    private final Rating[] ratings;

    public Recipe(Ingredient[] ingredients, String[] instructions, Rating[] ratings){
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.ratings = ratings;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public Rating[] getRatings() {
        return ratings;
    }

    public float getAvarageRating(){
        if (ratings.length == 0) return 0;
        
        int sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getRating();
        }
        return sum / (float)ratings.length;
    }
}
