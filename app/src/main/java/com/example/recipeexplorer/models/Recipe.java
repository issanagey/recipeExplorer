package com.example.recipeexplorer.model;

public class Recipe {
    private int id;
    private String state;
    private String name;
    private String steps;
    private String description;
    private byte[] image;

    public Recipe(int id, String state, String name, String steps, String description, byte[] image) {
        this.id = id;
        this.state = state;
        this.name = name;
        this.steps = steps;
        this.description = description;
        this.image = image;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

