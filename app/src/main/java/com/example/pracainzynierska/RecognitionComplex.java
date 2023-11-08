package com.example.pracainzynierska;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecognitionComplex extends AppCompatActivity {
    private DatabaseReference mDatabase;

    //todo: Create another ChooseKanji (in translation as well), for words created from multiple kanji instead of single ones
    //todo: get a set of those words, or users in addExercise can add new complex words, then set can be simplified

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognition);
    }

}
