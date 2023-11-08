package com.example.pracainzynierska;

public class JLPTObject extends GrammarOptionObject {

    private int imageResourceId;
    public JLPTObject(String title, String description, int image) {
        super(title, description);
        this.imageResourceId = image;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
