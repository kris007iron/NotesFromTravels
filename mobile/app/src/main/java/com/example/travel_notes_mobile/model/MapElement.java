package com.example.travel_notes_mobile.model;



public class MapElement {
    private int id;
    private int type;
    private String text;
    private int resourceId;

    public MapElement(int id, int type, String text, int resourceId) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.resourceId = resourceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
