package com.example.pracainzynierska;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileKanjiObject implements Serializable {
    int correct, incorrect;

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    String kun_readings, on_readings, meanings, kanji;
    String level;

    public void setKun_readings(String kun_readings) {
        this.kun_readings = kun_readings;
    }

    public void setOn_readings(String on_readings) {
        this.on_readings = on_readings;
    }

    public void setMeanings(String meanings) {
        this.meanings = meanings;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ProfileKanjiObject(int correct, int incorrect, String kun_reading, String on_reading, String meaning, String level, String kanji) {
        this.correct = correct;
        this.incorrect = incorrect;
        kun_readings = kun_reading;
        on_readings = on_reading;
        this.meanings = meaning;
        this.level = level;
        this.kanji = kanji;
    }

    public int getCorrect() {
        return correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public String getKun_readings() {
        return kun_readings;
    }

    public String getOn_readings() {
        return on_readings;
    }

    public String getMeanings() {
        return meanings;
    }

    public String getLevel() {
        return level;
    }
    public String getKanji() {return kanji; }
}
