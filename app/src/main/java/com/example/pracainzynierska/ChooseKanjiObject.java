package com.example.pracainzynierska;

import java.io.Serializable;
import java.util.ArrayList;

public class ChooseKanjiObject implements Serializable {

    String readings_kun;
    String readings_on;
    String meanings;
    //Somehow get Image? Or set as Image String?
    String kanji;

    String mostPopularKunReading;
    String mostPopularOnReading;


    public String getMostPopularKunReading() {
        return mostPopularKunReading;
    }

    public String getMostPopularOnReading() {
        return mostPopularOnReading;
    }

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

        setMostPopularReadings();
    }
    protected void setMostPopularReadings(){
        String temp_kun = readings_kun.replaceAll("[\\[\\],]","");
        if(temp_kun.length() > 0) {
            mostPopularKunReading = temp_kun.split(" ")[0];
        }
        else{
            mostPopularKunReading = "";
        }
        String temp_on = readings_on.replaceAll("[\\[\\],]","");
        if(temp_on.length() > 0) {
            mostPopularOnReading = temp_on.split(" ")[0];
        }
        else{
            mostPopularOnReading = "";
        }
    }

    public String getReadings_kun() {
        return readings_kun;
    }

    public String getReadings_on() {
        return readings_on;
    }

    public String getMeanings() {
        return meanings.replaceAll("[\\[\\]]","");
    }

    public String getKanji() {
        return kanji;
    }
}
