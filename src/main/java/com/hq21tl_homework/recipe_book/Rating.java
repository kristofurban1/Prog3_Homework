package com.hq21tl_homework.recipe_book;

public class Rating {
    private final String name;
    private final int value;
    private final String comment;

    public Rating(String name, int value, String comment){
        this.name = name;
        this.value = value;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return value;
    }

    public String getComment() {
        return comment;
    }
}
