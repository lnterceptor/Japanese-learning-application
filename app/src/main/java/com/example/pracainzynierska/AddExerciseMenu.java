package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class AddExerciseMenu extends AppCompatActivity {

        LinearLayout createGrammarExercise, createOtherExercise;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_exercise_menu);
            setGUI();
        }
        void setGUI(){
            createGrammarExercise = findViewById(R.id.recognition_button_menu);
            createOtherExercise = findViewById(R.id.drawing_button_menu);
            createGrammarExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),AddWritingExercise.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            createOtherExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),AddListeningExercise.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
}
