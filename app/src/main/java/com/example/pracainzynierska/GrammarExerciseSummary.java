package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class GrammarExerciseSummary extends AppCompatActivity {

    String hiragana;
    Button quitButton;
    TextView textView;
    int overallAmount, currentAnswer, correctlyAnswered, notCorrectlyAnswered;
    boolean hasAnsweredEveryQuestion = false;

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
        hasAnsweredEveryQuestion = getIntent().getBooleanExtra("answeredEvery", false);
        hiragana = getIntent().getStringExtra("hiragana");
        overallAmount = getIntent().getIntExtra("overallQuestions", 0);
        currentAnswer = getIntent().getIntExtra("questionNumber", 0);
        correctlyAnswered = getIntent().getIntExtra("correctlyAnswered", 0);
        notCorrectlyAnswered = getIntent().getIntExtra("inCorrectlyAnswered", 0);
    }
    void getGUI(){
        textView = findViewById(R.id.summary);
        quitButton = findViewById(R.id.quitButton);
    }
    void setGUI(){
        textView.setText("Poprawnie udzielone odpowiedzi: " + correctlyAnswered + "\nNiepoprawnie udzielone odpowiedzi: " + notCorrectlyAnswered + "\nAktualne pytanie: " + currentAnswer + "\nŁączna liczba pytań: " + overallAmount);
        quitButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GrammarMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), GrammarMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }
    protected void saveToDatabase() {
        if (!hasAnsweredEveryQuestion) {
            MainMenu.user.saveGrammar(hiragana, currentAnswer, overallAmount);
        }
    }


}
