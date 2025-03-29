package com.example.pracainzynierska;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseCurrentUserObject {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public String nick;
    public ArrayList<ProfileKanjiObject> userKanji = new ArrayList<>();
    public ArrayList<ProfileGrammarObject> userGrammar = new ArrayList<>();
    public ArrayList<FriendObject> userFriends = new ArrayList<>();
    private String userId;

    public void saveKanji(ArrayList<ProfileKanjiObject> kanjiToSave){
        for (ProfileKanjiObject kanji: kanjiToSave) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/"+userId+"/Kanji/"+kanji.getKanji());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int value1 = dataSnapshot.child("Correct").getValue(Integer.class);
                    int value2 = dataSnapshot.child("Incorrect").getValue(Integer.class);

                    databaseReference.child("Correct").setValue(value1 + kanji.getCorrect());
                    databaseReference.child("Incorrect").setValue(value2 + kanji.getIncorrect());
                } else {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("Correct", kanji.getCorrect());
                    dataMap.put("Incorrect", kanji.getIncorrect());
                    dataMap.put("Kun_reading", kanji.getKun_readings());
                    dataMap.put("On_reading", kanji.getOn_readings());
                    dataMap.put("Level", "N"+kanji.getLevel());
                    dataMap.put("Meaning", kanji.getMeanings());

                    databaseReference.setValue(dataMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
        }
    }

    public void addFriend(String friendName, Context context) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String forToast = "Nie znaleziono użytkownika";
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String tempNick = snapshot.child("Nick").getValue(String.class);
                        if (tempNick != null) {
                            if (tempNick.equals(friendName)) {
                                forToast = "Dodano znajomego";
                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users/" + userId + "/Friends/" + tempNick);
                                databaseReference1.setValue(snapshot.getKey());
                            }
                        }
                    }
                    Toast.makeText(context, forToast,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void saveGrammar(String hiragana, int amount, int overallAmount){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/" + userId + "/Grammar/"+hiragana+"/Amount");
        databaseReference.setValue(amount)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
                databaseReference = FirebaseDatabase.getInstance().getReference("users/" + userId + "/Grammar/"+hiragana+"/OverallAmount");
                databaseReference.setValue(overallAmount)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


    public FirebaseCurrentUserObject(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        setUserId();
        getNick();
        refreshData();
    }

    public void refreshData(){
        getKanji();
        getGrammar();
        getFriends();
    }
    public void setUserId(){
        userId = mAuth.getCurrentUser().getUid();
    }

    private void getNick(){
        DatabaseReference currentUserDb = mDatabase.child(userId);
        currentUserDb.child("Nick").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tempNick = dataSnapshot.getValue(String.class);
                if (tempNick != null) {
                    nick = tempNick;
                } else {
                    nick = "User";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void getKanji(){
        DatabaseReference currentUserDb = mDatabase.child(userId);
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
                    userKanji = kanjiList;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getGrammar(){
        DatabaseReference currentUserDb = mDatabase.child(userId);
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
                    userGrammar = grammarList;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getFriends(){

        DatabaseReference currentUserDb = mDatabase.child(userId);
        currentUserDb.child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<FriendObject> friendsList = new ArrayList<>();
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String friendName = snapshot.getKey();
                        String friendID = snapshot.getValue(String.class);
                        friendsList.add(new FriendObject(friendName,friendID));
                    }
                    userFriends = friendsList;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
