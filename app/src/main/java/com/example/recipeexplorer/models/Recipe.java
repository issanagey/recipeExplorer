package com.example.recipeexplorer.models;

public class Recipe {
    private final String title;
    private final String description;

    public Recipe(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
}
}

