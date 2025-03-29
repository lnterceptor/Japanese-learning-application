package com.example.pracainzynierska;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChooseKanji extends AppCompatActivity {
        private DatabaseReference mDatabase;
        final private int lengthOfSet = 21;
        ArrayList<ChooseKanjiObject> kanjiObjects = new ArrayList<ChooseKanjiObject>();
        ArrayList<ChooseKanjiHeader> kanjiHeaders = new ArrayList<ChooseKanjiHeader>();

        ExpandableListView listView;
        ChooseKanjiAdapter adapter;
        Class<?> nextActivity;
        String level;
        static ArrayList<String> kanjiForNextActivity = new ArrayList<String>();

        Button buttonToNextActivity;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            kanjiForNextActivity.clear();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.kanji_choose_activity);

            getInfoFromPreviousClasses();

            Intent intent = getIntent();
            nextActivity =  (Class<?>) intent.getSerializableExtra("nextActivity");

            buttonToNextActivity = findViewById(R.id.nextActivity);
            setNextActivityButton(buttonToNextActivity);


            listView = findViewById(R.id.list_with_kanji);
            getDataFromDatabase();
        }

    @Override
    protected void onResume() {
        super.onResume();
        kanjiForNextActivity.clear();
    }

    private void setNextActivityButton(Button button){
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(kanjiForNextActivity.size() > 0) {

                    Intent intent = new Intent(getApplicationContext(), nextActivity);

                    intent.putExtra("kanjiArray", kanjiForNextActivity);
                    intent.putExtra("allKanji", kanjiObjects);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Nie wybrano słów do nauki",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void finishOnCreate() {
        populateMap();

        adapter = new ChooseKanjiAdapter(getApplicationContext(), kanjiHeaders);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                ChooseKanjiObject child = (ChooseKanjiObject) adapter.getChild(groupPosition, childPosition);

                child.changeSelected();
                (parent.getExpandableListAdapter()).getChildView(groupPosition, childPosition, false, null, parent);
                ((ChooseKanjiAdapter) parent.getExpandableListAdapter()).notifyDataSetChanged();

                changeArrayOfKanji(child.getKanji(), child.isSelected());

                return true;
            }
        });
    }
    public static void changeArrayOfKanji(String kanji, boolean shouldAdd){
            if(shouldAdd == true){
                kanjiForNextActivity.add(kanji);
            }
            else{
                kanjiForNextActivity.remove(kanji);
            }
    }
    private void populateMap(){

         for(int i = 0; i <= kanjiObjects.size() / lengthOfSet; i++){
            ArrayList<ChooseKanjiObject> set = singleSet(i * lengthOfSet);
            ChooseKanjiHeader header = new ChooseKanjiHeader("Zestaw " + (i + 1), kanjiObjects.get(i * lengthOfSet).kanji, set);
            kanjiHeaders.add(header);
         }
    }
    private ArrayList<ChooseKanjiObject> singleSet(int startingPoint){
        ArrayList<ChooseKanjiObject> set = new ArrayList<ChooseKanjiObject>();
        int endPoint = startingPoint + lengthOfSet;



        if (endPoint == 0) {
            List<ChooseKanjiObject> subSet = kanjiObjects.subList(startingPoint, endPoint);
            set.addAll(subSet);
        }
        else if (endPoint < kanjiObjects.size()) {
            List<ChooseKanjiObject> subSet = kanjiObjects.subList(startingPoint + 1, endPoint);
            set.addAll(subSet);
        }
        else{
            List<ChooseKanjiObject> subSet = kanjiObjects.subList(startingPoint + 1, kanjiObjects.size());
            set.addAll(subSet);
        }

        return set;
    }

    private void getDataFromDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference("/kanji/"+level);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChooseKanjiObject kanjiObject;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                        String childKey = childSnapshot.getKey();

                        String meanings = childSnapshot.child("meanings").getValue() == null ? "": childSnapshot.child("meanings").getValue().toString();
                        String readings_kun = childSnapshot.child("readings_kun").getValue() == null ? "" : childSnapshot.child("readings_kun").getValue().toString();
                        String readings_on = childSnapshot.child("readings_on").getValue() == null ? "" : childSnapshot.child("readings_on").getValue().toString();
                        String level = childSnapshot.child("jlpt_new").getValue() == null ? "other" : childSnapshot.child("jlpt_new").getValue().toString();

                        kanjiObject = new ChooseKanjiObject(readings_kun, readings_on,meanings,childKey, level);
                        kanjiObjects.add(kanjiObject);
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

    private void getInfoFromPreviousClasses() {
        Intent intent = getIntent();
        nextActivity = intent.getParcelableExtra("nextActivity");
        level = intent.getStringExtra("lvl");
    }

}
