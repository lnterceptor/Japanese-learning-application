package com.example.pracainzynierska;

import java.util.ArrayList;

public class ChooseKanjiHeader {
    String title;
    String titleKanji;
    ArrayList<ChooseKanjiObject> kanjiSet;

    public String getTitle() {
        return title;
    }

    public String getTitleKanji() {
        return titleKanji;
    }

    public ArrayList<ChooseKanjiObject> getKanjiSet() {
        return kanjiSet;
    }

    public ChooseKanjiHeader(String title, String titleKanji, ArrayList<ChooseKanjiObject> kanjiSet) {
        this.title = title;
        this.titleKanji = titleKanji;
        this.kanjiSet = kanjiSet;
    }

}
