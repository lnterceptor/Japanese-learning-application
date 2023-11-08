package com.example.pracainzynierska;

import java.io.Serializable;
import java.util.ArrayList;

public class ChooseKanjiObject implements Serializable {

    String readings_kun;
    String readings_on;
    String meanings;
    //Somehow get Image? Or set as Image String?
    String kanji;

    public boolean isSelected() {
        return isSelected;
    }

    public void changeSelected() {
        isSelected = !isSelected;
    }

    boolean isSelected = false;

    public ChooseKanjiObject(String readings_kun, String readings_on, String meanings, String kanji) {
        this.readings_kun = readings_kun;
        this.readings_on = readings_on;
        this.meanings = meanings;
        this.kanji = kanji;
    }

    public String getReadings_kun() {
        return readings_kun;
    }

    public String getReadings_on() {
        return readings_on;
    }

    public String getMeanings() {
        return meanings;
    }

    public String getKanji() {
        return kanji;
    }
}
