package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GrammarExercise extends AppCompatActivity {
    //todo: if someone has finished all exercises random any avaible
    ArrayList<Button> buttons = new ArrayList<>();
    
    TextView kanjiNum, kanjiOverall;
    Button returnButton, endSessionButton;

    ArrayList<GrammarExerciseObject> grammarExerciseObjects;
    TextView sentenceTextView, titleTextView;
    ArrayList<String> incorrectAnswer = new ArrayList<>();
    ConstraintLayout layout, endSessionLayout;
    String correctAnswer, sentence, title;
    int currentExercise = 0;
    int incorrectlyAnswered = 0, correctlyAnswered = 0;
    protected boolean responded = false;
    boolean answeredEvery = false;
    GrammarObject grammarObject;

    public void retrieveGrammarObject(){
        grammarObject = (GrammarObject) getIntent().getSerializableExtra("grammarObject");
        grammarExerciseObjects = grammarObject.exercises;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grammar_object_exercises);
        getAdditionalData();
        getGUI();
        setButtons();
        setTitle();
        setGUI();
        setRestOfGUI();
//        addGrammarExercises();
    }

//    void addGrammarExercises() {
//        String tense = "～ます";
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("grammar/" + tense);
//        Map<String, Object> tenseData = new HashMap<>();
//        try {
//            tenseData.put("Definition", "Forma teraźniejsza twierdząca ～ます służy do wyrażania działań lub stanów, które mają miejsce w teraźniejszości i są twierdzące.");
//
//            List<Map<String, String>> examplesOfUse = new ArrayList<>();
//            Map<String, String> example1 = new HashMap<>();
//            example1.put("Japanese", "食べます");
//            example1.put("Polish", "Jem.");
//            Map<String, String> example2 = new HashMap<>();
//            example2.put("Japanese", "行きます");
//            example2.put("Polish", "Idę.");
//            examplesOfUse.add(example1);
//            examplesOfUse.add(example2);
//            tenseData.put("ExamplesOfUse", examplesOfUse);
//
//            tenseData.put("Title", "～ます");
//
//            List<Map<String, String>> exercises = new ArrayList<>();
//            Map<String, String> exercise1 = new HashMap<>();
//            exercise1.put("CorrectAnswer", "飲みます");
//            exercise1.put("IncorrectAnswer1", "食べます");
//            exercise1.put("IncorrectAnswer2", "行きます");
//            exercise1.put("IncorrectAnswer3", "泳ぎます");
//            exercise1.put("Sentence", "Piję.");
//
//            Map<String, String> exercise2 = new HashMap<>();
//            exercise2.put("CorrectAnswer", "話します");
//            exercise2.put("IncorrectAnswer1", "見ます");
//            exercise2.put("IncorrectAnswer2", "食べます");
//            exercise2.put("IncorrectAnswer3", "行きます");
//            exercise2.put("Sentence", "Mówię.");
//
//            Map<String, String> exercise3 = new HashMap<>();
//            exercise3.put("CorrectAnswer", "聞きます");
//            exercise3.put("IncorrectAnswer1", "書きます");
//            exercise3.put("IncorrectAnswer2", "読みます");
//            exercise3.put("IncorrectAnswer3", "飲みます");
//            exercise3.put("Sentence", "Słucham.");
//
//            exercises.add(exercise1);
//            exercises.add(exercise2);
//            exercises.add(exercise3);
//
//            tenseData.put("Exercises", exercises);
//            System.out.println(tenseData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        databaseReference.setValue(tenseData);
//    }

    protected void setButtons(){
        setAnswerButtons(buttons.get(0));
        setAnswerButtons(buttons.get(1));
        setAnswerButtons(buttons.get(2));
        setAnswerButtons(buttons.get(3));
    }
    private void setTitle(){
        title = grammarObject.getTitle();
        titleTextView.setText(title);
    }
    private void getAdditionalData(){
        retrieveGrammarObject();
        for(int i = 0 ; i < MainMenu.user.userGrammar.size(); i++){
            if(Objects.equals(MainMenu.user.userGrammar.get(i).getTitle(), grammarObject.hiragana)){
                currentExercise = MainMenu.user.userGrammar.get(i).getAmount();
                if(currentExercise == MainMenu.user.userGrammar.get(i).getOverallAmount()){
                    answeredEvery = true;
                    currentExercise = 0;
                }
                //todo: jesli max to losuj pytania
            }
        }
    }

    protected void setGUI(){
        setSentence();
        incorrectAnswer.clear();
        correctAnswer = grammarExerciseObjects.get(currentExercise).correctAnswer;
        incorrectAnswer.add(grammarExerciseObjects.get(currentExercise).incorrectAnswers.get(0));
        incorrectAnswer.add(grammarExerciseObjects.get(currentExercise).incorrectAnswers.get(1));
        incorrectAnswer.add(grammarExerciseObjects.get(currentExercise).incorrectAnswers.get(2));
        setButtonsText();
    }

    private void setSentence() {
        sentence = grammarExerciseObjects.get(currentExercise).sentence;
        sentenceTextView.setText(sentence);
    }


    protected void setButtonsText(){
        ArrayList<String> answers = new ArrayList<>(incorrectAnswer);
        answers.add(correctAnswer);
        Collections.shuffle(answers);
        buttons.get(0).setText(answers.get(0));
        buttons.get(1).setText(answers.get(1));
        buttons.get(2).setText(answers.get(2));
        buttons.get(3).setText(answers.get(3));
    }
    private void getGUI() {
        kanjiNum = findViewById(R.id.numberOfFinishedKanji);
        kanjiOverall = findViewById(R.id.numberOfOverallKanji);
        endSessionButton = findViewById(R.id.endSessionButton);
        returnButton = findViewById(R.id.returnButton);

        endSessionLayout = findViewById(R.id.endSession);
        layout = findViewById(R.id.background);
        sentenceTextView = findViewById(R.id.sentence);
        titleTextView = findViewById(R.id.title);
        buttons.add(findViewById(R.id.answer_1));
        buttons.add(findViewById(R.id.answer_2));
        buttons.add(findViewById(R.id.answer_3));
        buttons.add(findViewById(R.id.answer_4));
    }
    protected void setRestOfGUI(){
        sentenceTextView.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
        titleTextView.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
        endSessionButton.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                finishSession();
            }
        });
        returnButton.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                endSessionLayout.setVisibility(View.INVISIBLE);
                setEnabled(true);
            }
        });


        layout.setOnClickListener(new ConstraintLayout.OnClickListener(){
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
    }
    protected void setAnswerButtons(Button button){
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button);
            }
        });
    }

    protected void checkAnswer(Button button){
        if(!responded) {
            if (button.getText().equals(correctAnswer)) {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.CorrectAnswer));
                correctlyAnswered += 1;
            } else {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.IncorrectAnswer));
                incorrectlyAnswered += 1;
                for (Button buttonTemp : buttons) {
                    if (buttonTemp.getText().equals(correctAnswer)) {
                        buttonTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.CorrectAnswer));
                        break;
                    }
                }
            }
            responded = true;
        }
        else{
            nextQuestion();
        }
    }
    protected void resetButtons() {
        for (Button button : buttons) {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonColor));
        }
    }

    protected void nextQuestion(){
        if(responded){
            if(grammarExerciseObjects.size() > currentExercise + 1){
                currentExercise += 1;
            }
            else{
                currentExercise += 1;
                finishSession();
                return;
            }
            setGUI();
            resetButtons();
            responded = false;
        }
    }

    protected void finishSession(){

        Intent intent = new Intent(getApplicationContext(), GrammarExerciseSummary.class);

        intent.putExtra("correctlyAnswered", correctlyAnswered);
        intent.putExtra("inCorrectlyAnswered", incorrectlyAnswered);
        intent.putExtra("questionNumber", currentExercise);

        if(answeredEvery){
            intent.putExtra("answeredEvery", true);
        }
        intent.putExtra("hiragana", grammarObject.hiragana);
        intent.putExtra("overallQuestions", grammarExerciseObjects.size());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();

        //todo: implement finish session
        //jakie info poczebne -> ktore aktualnie zadanie, ile dobrze w danej sesji, ile zle
    }

    private void setEnabled(boolean isEnabled){
        for (Button button: buttons) {
            button.setEnabled(isEnabled);
        }
        layout.setEnabled(isEnabled);
        titleTextView.setEnabled(isEnabled);
    }
    protected void showGoBackScreen(){
        if(endSessionLayout.getVisibility() == View.VISIBLE){
            endSessionLayout.setVisibility(View.INVISIBLE);
            setEnabled(true);
            return;
        }
        endSessionLayout.setVisibility(View.VISIBLE);
        setEnabled(false);
        kanjiNum.setText("Aktualne pytanie: " + String.valueOf(currentExercise));
        kanjiOverall.setText("Łączna liczba pytań: " + String.valueOf(grammarExerciseObjects.size()));
    }
    @Override
    public void onBackPressed() {
        showGoBackScreen();
    }
}
