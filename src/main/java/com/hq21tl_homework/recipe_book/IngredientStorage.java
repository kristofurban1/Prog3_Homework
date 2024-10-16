package com.hq21tl_homework.recipe_book;

import java.util.ArrayList;

public class IngredientStorage {
    private final ArrayList<String> ingredients = new ArrayList<>();

    public void newIngredient(String name){
        if (!ingredients.contains(name)) ingredients.add(name);
    }
}
