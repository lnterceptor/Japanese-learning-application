package com.example.pracainzynierska;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;


public class OtherExercisesMenu extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            FirebaseApp.initializeApp(this);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.crossword_menu);
        }
}
