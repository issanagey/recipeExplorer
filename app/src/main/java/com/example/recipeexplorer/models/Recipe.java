package com.example.recipeexplorer.models;

public class Recipe {
    private int id;
    private final String state;
    private final String title;
    private final String description;
    private String steps;
    private byte[] image;

    // Constructor for initializing with all fields
    public Recipe(int id, String state, String title, String description, String steps, byte[] image) {
        this.id = id;
        this.state = state;
        this.title = title;
        this.description = description;
        this.steps = steps;
        this.image = image;
    }

    // Constructor for initializing without id (e.g., for new entries)
    public Recipe(String state, String title, String description, String steps, byte[] image) {
        this.state = state;
        this.title = title;
        this.description = description;
        this.steps = steps;
        this.image = image;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSteps() {
        return steps;
    }

    public byte[] getImage() {
        return image;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
