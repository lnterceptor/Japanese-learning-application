package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileFriend extends AppCompatActivity {


    Button kanjiButton, grammarButton, goBackButton, checkExercisesButton;
    TextView login;
    String friendID;
    String nick;
    ArrayList<ProfileGrammarObject> friendsGrammar = new ArrayList<>();
    ArrayList<ProfileKanjiObject> friendsKanji = new ArrayList<>();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        setContentView(R.layout.profile_friend);
        getAdditionalData();
        getAndSetData();
    }
    void getAdditionalData(){
        friendID = getIntent().getStringExtra("userID");
    }
    protected void setData(){
        setButtons();
        setTextField();
    }
    protected void setTextField(){
        login = findViewById(R.id.Login);
        login.setText("Profil użytkownika: " + nick);
    }

    protected void setButtons(){
        checkExercisesButton = findViewById(R.id.CreatedExercises);
        goBackButton = findViewById(R.id.GoBack);
        kanjiButton = findViewById(R.id.Kanji);
        grammarButton = findViewById(R.id.Grammar);

        goBackButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        kanjiButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                goToKanjiScreen();
            }
        });
        grammarButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                goToGrammarScreen();
            }
        });
        checkExercisesButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                goToExercisesScreen();
            }
        });
    }

    protected void goToKanjiScreen(){
        Intent intent = new Intent(getApplicationContext(), ProfileCheckKanji.class);
        intent.putExtra("Kanji", friendsKanji);
        startActivity(intent);
    }

    protected void goToGrammarScreen(){
        Intent intent = new Intent(getApplicationContext(), ProfileCheckGrammar.class);
        intent.putExtra("Grammar", friendsGrammar);
        startActivity(intent);
    }

    protected void goToExercisesScreen(){
        Intent intent = new Intent(getApplicationContext(), ProfileCheckExercisesMenu.class);
        intent.putExtra("user", nick);
        startActivity(intent);
    }


    private void getAndSetData(){
        DatabaseReference currentUserDb = mDatabase.child(friendID);
        currentUserDb.child("Nick").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tempNick = dataSnapshot.getValue(String.class);
                if (tempNick != null) {
                    nick = tempNick;
                } else {
                    nick = "User";
                }
                getGrammar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void getKanji(){
        DatabaseReference currentUserDb = mDatabase.child(friendID);
        currentUserDb.child("Kanji").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ProfileKanjiObject> kanjiList = new ArrayList<>();
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String kanji = snapshot.getKey();
                        int correct = snapshot.child("Correct").getValue(int.class);
                        int inCorrect = snapshot.child("Incorrect").getValue(int.class);
                        String kunReading = snapshot.child("Kun_reading").getValue(String.class);
                        String onReading = snapshot.child("On_reading").getValue(String.class);
                        String level = snapshot.child("Level").getValue(String.class);
                        String meaning = snapshot.child("Meaning").getValue(String.class);

                        kanjiList.add(new ProfileKanjiObject(correct,inCorrect,kunReading,onReading,meaning,level, kanji));
                    }
                    friendsKanji = kanjiList;
                    setData();
                }
                else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getGrammar(){
        DatabaseReference currentUserDb = mDatabase.child(friendID);
        currentUserDb.child("Grammar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ProfileGrammarObject> grammarList = new ArrayList<>();
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String grammarTitle = snapshot.getKey();

                        int overallAmount = snapshot.child("OverallAmount").getValue(int.class);
                        int grammarAmount = snapshot.child("Amount").getValue(int.class);

                        grammarList.add(new ProfileGrammarObject(grammarTitle, overallAmount, grammarAmount));
                    }
                    friendsGrammar = grammarList;
                    getKanji();
                }
                else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
