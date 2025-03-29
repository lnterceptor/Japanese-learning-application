package com.example.pracainzynierska;

import android.os.Bundle;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProfileCheckKanji  extends AppCompatActivity {

    ProfileCheckKanjiAdapter adapter;
    ArrayList<ProfileKanjiObject> kanjiList;
    ExpandableListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_kanji);
        kanjiList = getKanji();
        adapter = new ProfileCheckKanjiAdapter(getApplicationContext(), kanjiList);

        listView = findViewById(R.id.kanji_list);

        listView.setAdapter(adapter);
    }
    protected ArrayList<ProfileKanjiObject> getKanji(){
        return (ArrayList<ProfileKanjiObject>) getIntent().getSerializableExtra("Kanji");
    }

}
