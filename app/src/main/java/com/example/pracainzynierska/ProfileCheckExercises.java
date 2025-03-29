package com.example.pracainzynierska;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileCheckExercises extends AppCompatActivity {
    ListView friends_list;
    TextView friendsText;
    ProfileWrittenExerciseAdapter adapter;
    String nick;
    ArrayList<RecognitionComplexObject> listOfWords = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_friends_list);
        getGUI();
        getNick();
        getDataFromDatabase();
    }
    void getNick(){
        nick = getIntent().getStringExtra("user");
    }
    protected void getGUI(){
        friendsText = findViewById(R.id.friendsText);
        friendsText.setText("Lista stworzonych wyrazów");
        friends_list = findViewById(R.id.friends_list);
    }
    protected void setListView(){
        adapter = new ProfileWrittenExerciseAdapter(getApplicationContext(),listOfWords);
        friends_list.setAdapter(adapter);
    }
    protected DatabaseReference getDataBaseRefference(){
        return FirebaseDatabase.getInstance().getReference("exercises/written_exercises");
    }
    protected void getDataFromDatabase(){
        DatabaseReference mDatabase = getDataBaseRefference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        if(Objects.equals(user.getKey(), nick)) {
                            for (DataSnapshot words : user.getChildren()) {
                                if (words.exists()) {
                                    String polishSentence = words.getKey();
                                    String correct = words.child("Correct").getValue(String.class);
                                    String incorrect1 = words.child("Incorrect1").getValue(String.class);
                                    String incorrect2 = words.child("Incorrect2").getValue(String.class);
                                    String incorrect3 = words.child("Incorrect3").getValue(String.class);
                                    listOfWords.add(new RecognitionComplexObject(polishSentence, correct, incorrect1, incorrect2, incorrect3, user.getKey()));
                                }
                            }
                        }
                    }
                }
                setListView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
