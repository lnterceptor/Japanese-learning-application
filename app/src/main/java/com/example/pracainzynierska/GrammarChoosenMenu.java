package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GrammarChoosenMenu extends AppCompatActivity {
    Button theory, exercises;
    TextView title;
    GrammarObject grammarObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveGrammarObject();
        setContentView(R.layout.grammar_object);
        getGUI();
        setGUI();
    }


    public void retrieveGrammarObject(){
        grammarObject = (GrammarObject) getIntent().getSerializableExtra("grammarObject");
    }
    public void getGUI(){
        title = findViewById(R.id.title);
        theory = findViewById(R.id.theory);
        exercises = findViewById(R.id.exercises);
    }
    public void setGUI(){
        title.setText(grammarObject.getTitle());
        theory.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theory = new Intent(getApplicationContext(),GrammarTheory.class);
                theory.putExtra("grammarObject", grammarObject);
                startActivity(theory);
            }
        });

        exercises.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent exercise = new Intent(getApplicationContext(),GrammarExercise.class);
                exercise.putExtra("grammarObject", grammarObject);
                startActivity(exercise);

            }
        });
    }
}
