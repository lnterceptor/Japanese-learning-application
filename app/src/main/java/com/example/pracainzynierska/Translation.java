package com.example.pracainzynierska;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Translation extends AppCompatActivity {
    //todo: Currently kanji is copied, instead u need to copy it's meaning from previous activity
    //todo: Create another activity? That will copy meanings instead of Kanji
    //todo: Somehow i need to either change written text into hiragana/katakana or create some kind of custom keyboard for that purpose
    //todo: Possibly instead of changing text into hiragana, i might change hiragana into romaji
    private DatabaseReference mDatabase;
    Context context;

    Button checkAnswer, image;
    TextInputEditText answerInput;
    ArrayList<String> kanjiForRepetition; //todo: shuffle them somewhere
    ArrayList<ChooseKanjiObject> kanjiObjects;
    String currentAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.translation);


        kanjiObjects = (ArrayList<ChooseKanjiObject>) getIntent().getSerializableExtra("allKanji");
        kanjiForRepetition = getIntent().getStringArrayListExtra("kanjiArray");

        currentAnswer = kanjiForRepetition.get(0);

        checkAnswer = findViewById(R.id.checkAnswer);
        setCheckButton(checkAnswer);

        image = findViewById(R.id.TranslationKanjiImage);
        answerInput = findViewById(R.id.translationInput);
        setImage();

    }

    protected void setCheckButton(Button button){
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    void setImage(){
        image.setText(currentAnswer);
    }
    void checkAnswer(){

        //todo: someone might write in as kunyomi, or onyomi, so check both options
        if(checkAnswer.getText().equals(currentAnswer)){
            //todo: Idk how to communicate whether provided answer is correct or not
            checkAnswer.setBackgroundColor(ContextCompat.getColor(this,R.color.CorrectAnswer));
            answerInput.setText("");
        }
        else{
            checkAnswer.setBackgroundColor(ContextCompat.getColor(this,R.color.IncorrectAnswer));
        }
    }


}