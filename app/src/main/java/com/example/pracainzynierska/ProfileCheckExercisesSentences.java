package com.example.pracainzynierska;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileCheckExercisesSentences extends ProfileCheckExercises{

    @Override
    protected DatabaseReference getDataBaseRefference(){
         return FirebaseDatabase.getInstance().getReference("exercises/listening_exercises");
    }

    @Override
    protected void getGUI(){
        friendsText = findViewById(R.id.friendsText);
        friendsText.setText("Lista stworzonych zdań");
        friends_list = findViewById(R.id.friends_list);
    }
    @Override
    protected void setListView(){
        adapter = new ProfileWrittenSentencesExerciseAdapter(getApplicationContext(),listOfWords);
        friends_list.setAdapter(adapter);
    }
}
