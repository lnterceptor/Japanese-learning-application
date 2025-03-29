package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RecognitionComplex extends AppCompatActivity {
    protected ArrayList<RecognitionComplexObject> kanjiQuestions = new ArrayList<>();
    ConstraintLayout layout;
    String friendsName = "";
    Button button;
    Random random = new Random();

    ArrayList<Button> buttons = new ArrayList<>();
    TextView userProfileText;

    TextView numberOfFinishedKanji, numberOfOverallKanji;
    Button returnButton, endSessionButton, endSessionButton2;
    ConstraintLayout endSessionScreen, endSessionScreen2;
    ArrayList<String> incorrectAnswer = new ArrayList<>();
    String correctAnswer, sentence = "";
    int currentExercise = 0;
    boolean responded = false;
    int correctlyAnswered = 0, incorrectlyAnswered = 0;
    TextToSpeech textToSpeech;


    void getDataFromDatabase(){
        friendsName = getIntent().getStringExtra("friendsName");

        DatabaseReference mDatabase = getDatabaseReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        if(Objects.equals(user.getKey(), friendsName) || Objects.equals(friendsName, null)) {
                            for (DataSnapshot words : user.getChildren()) {
                                if (words.exists()) {
                                    String polishSentence = words.getKey();
                                    String correct = words.child("Correct").getValue(String.class);
                                    String incorrect1 = words.child("Incorrect1").getValue(String.class);
                                    String incorrect2 = words.child("Incorrect2").getValue(String.class);
                                    String incorrect3 = words.child("Incorrect3").getValue(String.class);
                                    kanjiQuestions.add(new RecognitionComplexObject(polishSentence, correct, incorrect1, incorrect2, incorrect3, user.getKey()));
                                }
                            }
                        }
                    }
                }
                setGUI();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    protected DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference("exercises/written_exercises");
    }

    @Override
    public void onBackPressed(){
        if(endSessionScreen2.getVisibility() == View.VISIBLE){
            endSession();
        }
        if(endSessionScreen.getVisibility() == View.VISIBLE) {
            for(Button button: buttons){
                button.setEnabled(true);
            }
            layout.setEnabled(true);
            button.setEnabled(true);

            endSessionScreen.setVisibility(View.INVISIBLE);
            return;
        }
        endSessionScreen.setVisibility(View.VISIBLE);
        numberOfFinishedKanji.setText("Aktualne pytanie: " + String.valueOf(1 + correctlyAnswered + incorrectlyAnswered));
        numberOfOverallKanji.setText("Udzielono poprawnej odpowiedzi na: \n" +String.valueOf(correctlyAnswered) +" pytań\n Niepoprawnie odpowiedziano na: \n" + String.valueOf(incorrectlyAnswered)+" pytań");
        for(Button button: buttons){
            button.setEnabled(false);
        }
        layout.setEnabled(false);
        button.setEnabled(false);
    }

    void returnBack(){
        endSessionScreen.setVisibility(View.INVISIBLE);
        for(Button button: buttons){
            button.setEnabled(true);
        }
        layout.setEnabled(true);
        button.setEnabled(true);
    }

    void setOtherButtons(){
        numberOfFinishedKanji = findViewById(R.id.numberOfFinishedKanji);
        numberOfOverallKanji = findViewById(R.id.numberOfOverallKanji);
        returnButton = findViewById(R.id.returnButton);
        endSessionButton2 = findViewById(R.id.endSessionButton2);

        endSessionButton = findViewById(R.id.endSessionButton);
        returnButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnBack();
            }
        });

        endSessionButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession();
            }
        });
        endSessionButton2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession();
            }
        });
    }

    protected void endSession(){
        Intent intent = new Intent(getApplicationContext(),WritingMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    protected void setContentView(){
        setContentView(R.layout.complex_recognition);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initializeTextToSpeech();
        getDataFromDatabase();
        getGUI();
        setButtons();
        setOtherButtons();
        setLayout();
    }
    protected void setLayout(){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
    }
    protected void initializeTextToSpeech(){
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.JAPANESE);
                    utterFirstWord();
                }
            }
        });
    }


    protected void utterFirstWord() {
        textToSpeech.speak(correctAnswer, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    protected void setTextToSpeech(){
        String textToSpeak = correctAnswer;
        textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    protected void getGUI(){
        endSessionScreen2 = findViewById(R.id.endSession2);
        endSessionScreen = findViewById(R.id.endSession);
        button = findViewById(R.id.RecognitionKanjiImage);
        buttons.add(findViewById(R.id.buttonResponce1));
        buttons.add(findViewById(R.id.buttonResponce2));
        buttons.add(findViewById(R.id.buttonResponce3));
        buttons.add(findViewById(R.id.buttonResponce4));
        layout = findViewById(R.id.recognition_background);
        userProfileText = findViewById(R.id.userProfileText);
    }

    protected void setTextToSpeechOnButtonPress(){
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                setTextToSpeech();
                nextQuestion();
            }
        });
    }

    protected void setSentence() {
        sentence = kanjiQuestions.get(currentExercise).getPolishSentence();
        if(textToSpeech != null) {
            textToSpeech.speak(correctAnswer, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        button.setText(sentence);
        userProfileText.setText("Ćwiczenie użytkownika: " + kanjiQuestions.get(currentExercise).getUser());
    }
    protected void setGUI(){
        if(kanjiQuestions.size() == 0){
            endSessionScreen2.setVisibility(View.VISIBLE);
            for(Button button: buttons){
                button.setEnabled(false);
            }
            layout.setEnabled(false);
            button.setEnabled(false);
            return;
        }
        currentExercise = random.nextInt(kanjiQuestions.size());
        setSentence();
        incorrectAnswer.clear();
        correctAnswer = kanjiQuestions.get(currentExercise).getCorrectSentence();
        incorrectAnswer.add(kanjiQuestions.get(currentExercise).getIncorrectSentence1());
        incorrectAnswer.add(kanjiQuestions.get(currentExercise).getIncorrectSentence2());
        incorrectAnswer.add(kanjiQuestions.get(currentExercise).getIncorrectSentence3());
        setButtonsText();
    }
    protected void setButtons(){
        setTextToSpeechOnButtonPress();
        setAnswerButtons(buttons.get(0));
        setAnswerButtons(buttons.get(1));
        setAnswerButtons(buttons.get(2));
        setAnswerButtons(buttons.get(3));
    }
    protected void setAnswerButtons(Button button){
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button);
            }
        });
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
    protected void nextQuestion(){
        if(responded){
            setGUI();
            resetButtons();
            responded = false;
            utterKanji();
        }
    }

    protected void utterKanji(){
        textToSpeech.speak(correctAnswer, TextToSpeech.QUEUE_FLUSH, null, null);
    }
    protected void resetButtons() {
        for (Button button : buttons) {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonColor));
        }
    }



}
