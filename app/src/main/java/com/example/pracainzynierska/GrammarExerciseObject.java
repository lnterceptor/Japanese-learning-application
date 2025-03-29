package com.example.pracainzynierska;

import java.io.Serializable;
import java.util.ArrayList;

public class GrammarExerciseObject implements Serializable {
    int id;
    String sentence;
    String correctAnswer;
    ArrayList<String> incorrectAnswers;

    public GrammarExerciseObject(int id, String sentence, String correctAnswer, ArrayList<String> incorrectAnswers) {
        this.id = id;
        this.sentence = sentence;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    public int getId() {
        return id;
    }

    public String getSentence() {
        return sentence;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public ArrayList<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }
}
