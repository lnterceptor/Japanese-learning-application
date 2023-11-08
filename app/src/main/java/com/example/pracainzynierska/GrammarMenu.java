package com.example.pracainzynierska;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class GrammarMenu  extends AppCompatActivity {

    List<GrammarOptionObject> options = new ArrayList<GrammarOptionObject>();
    ListView listView;
    private ArrayAdapter<GrammarOptionObject> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grammar_menu);
        listView = findViewById(R.id.grammar_list);

        options.add(new GrammarOptionObject("1","1"));
        options.add(new GrammarOptionObject("1","1"));
        options.add(new GrammarOptionObject("1","1"));
        options.add(new GrammarOptionObject("1","1"));

        adapter = new GrammarOptionAdapter(this, options);
        listView.setAdapter(adapter);
    }
}

