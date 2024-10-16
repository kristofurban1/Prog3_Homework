package com.hq21tl_homework.recipe_book;

public class Ingredient {
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
}
