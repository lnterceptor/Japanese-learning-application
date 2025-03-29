package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;

public class KanjiExerciseSummary extends AppCompatActivity {

    Button quitButton;
    TextView textView;
    boolean hasAnsweredEveryQuestion = false;
    boolean isPassiveListening = false;

    ArrayList<ProfileKanjiObject> kanji;
    Class nextMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grammar_object_exercises_finish_screen);
        getAdditionalData();
        getGUI();
        setGUI();
        saveToDatabase();
    }

    void getAdditionalData(){
        nextMenu = (Class) getIntent().getSerializableExtra("Menu");
        kanji = (ArrayList<ProfileKanjiObject>) getIntent().getSerializableExtra("AllKanji");
        isPassiveListening =  getIntent().getBooleanExtra("passiveListening", false);
    }
    void getGUI(){
        textView = findViewById(R.id.summary);
        quitButton = findViewById(R.id.quitButton);
    }
    String correctKanji(){
        int correct = 0;
        for (ProfileKanjiObject singleKanji: kanji) {
            correct += singleKanji.getCorrect();
        }
        return String.valueOf(correct);
    }
    String incorrectKanji(){
        int correct = 0;
        for (ProfileKanjiObject singleKanji: kanji) {
            correct += singleKanji.getIncorrect();
        }
        return String.valueOf(correct);
    }
    void setGUI(){
        if(!isPassiveListening) {
            textView.setText("Poprawnie udzielone odpowiedzi: " + correctKanji() + "\nNiepoprawnie udzielone odpowiedzi: " + incorrectKanji() + "\nUdzielono odpowiedzi na:\n   " + kanji.size() + " pytań");
        }
        else{
            textView.setText("Liczba odsłuchanych kanji: " + kanji.size() + "\nLiczba odsłuchanych powtórzeń: " + correctKanji());
        }
        quitButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nextMenu);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), nextMenu);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }
    protected void saveToDatabase() {
        if (!hasAnsweredEveryQuestion) {
            MainMenu.user.saveKanji(kanji);
            //MainMenu.user.saveGrammar(hiragana, currentAnswer, overallAmount);
        }
    }


}
