package com.example.pracainzynierska;

import java.io.Serializable;
import java.util.ArrayList;

public class GrammarObject implements Serializable {
    String hiragana;
    String title, definition;
    ArrayList<GrammarTranslationPairObject> examplesOfUse;
    ArrayList<GrammarExerciseObject> exercises;

    public GrammarObject(String title, String definition, ArrayList<GrammarTranslationPairObject> examplesOfUse, ArrayList<GrammarExerciseObject> exercises, String hiragana) {
        this.title = title;
        this.definition = definition;
        this.examplesOfUse = examplesOfUse;
        this.exercises = exercises;
        this.hiragana = hiragana;
    }

    public String getTitle() {
        return title;
    }
    public String getHiragana() {
        return hiragana;
    }

    public String getDefinition() {
        return definition;
    }

    public ArrayList<GrammarTranslationPairObject> getExamplesOfUse() {
        return examplesOfUse;
    }

    public ArrayList<GrammarExerciseObject> getExercises() {
        return exercises;
    }
}
