package com.example.pracainzynierska;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GrammarMenu  extends AppCompatActivity {

    List<GrammarObject> options = new ArrayList<GrammarObject>();
    private DatabaseReference mDatabase;
    ListView listView;
    private ArrayAdapter<GrammarObject> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grammar_menu);
        listView = findViewById(R.id.grammar_list);
        setGrammarList();

        //adapter = new GrammarOptionAdapter(this, options);
        //listView.setAdapter(adapter);
    }

    protected void setGrammarList(){
        mDatabase = FirebaseDatabase.getInstance().getReference("/grammar");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                        String kanji = childSnapshot.getKey();
                        String title = childSnapshot.child("Title").getValue().toString();
                        String definition = childSnapshot.child("Definition").getValue().toString();

                        ArrayList<GrammarExerciseObject> exercises = new ArrayList<>();
                        ArrayList<GrammarTranslationPairObject> examplesOfUse = new ArrayList<>();

                        DataSnapshot examplesOfUseReference = childSnapshot.child("ExamplesOfUse");
                        if(examplesOfUseReference.exists()){
                            for(DataSnapshot exampleOfUse: examplesOfUseReference.getChildren()){
                                int id = Integer.parseInt(exampleOfUse.getKey());
                                String japaneseSentence = exampleOfUse.child("Japanese").getValue().toString();
                                String polishSentence = exampleOfUse.child("Polish").getValue().toString();
                                examplesOfUse.add( new GrammarTranslationPairObject(japaneseSentence, polishSentence));
                            }
                        }

                        DataSnapshot exercisesReference = childSnapshot.child("Exercises");
                        if(exercisesReference.exists()){
                            for(DataSnapshot exampleOfUse: exercisesReference.getChildren()){
                                int id = Integer.parseInt(exampleOfUse.getKey());
                                String sentence = exampleOfUse.child("Sentence").getValue().toString();
                                String correctAnswer = exampleOfUse.child("CorrectAnswer").getValue().toString();
                                ArrayList<String> incorrectAnswers = new ArrayList<>();
                                incorrectAnswers.add(exampleOfUse.child("IncorrectAnswer1").getValue().toString());
                                incorrectAnswers.add(exampleOfUse.child("IncorrectAnswer2").getValue().toString());
                                incorrectAnswers.add(exampleOfUse.child("IncorrectAnswer3").getValue().toString());
                                exercises.add( new GrammarExerciseObject(id, sentence, correctAnswer, incorrectAnswers));
                            }
                        }

                        options.add(new GrammarObject(title,definition,examplesOfUse, exercises, kanji));
                    }
                }
                finishOnCreate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                return;
            }

        });
    }


    private void finishOnCreate() {

        adapter = new GrammarOptionAdapter(this, options);
        listView.setAdapter(adapter);

    }
}

