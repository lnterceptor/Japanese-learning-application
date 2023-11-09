package com.example.pracainzynierska;

public class JLPTObject extends GrammarOptionObject {

    private int imageResourceId;
    String kanji;
    public JLPTObject(String title, String description, String kanji, int image) {
        super(title, description);
        this.imageResourceId = image;
        this.kanji = kanji;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
    public String getKanji(){return kanji;}
}
