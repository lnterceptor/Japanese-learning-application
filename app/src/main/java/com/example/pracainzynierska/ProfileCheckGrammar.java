package com.example.pracainzynierska;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProfileCheckGrammar extends AppCompatActivity {
    ArrayList<ProfileGrammarObject> grammar;
    ProfileCheckGrammarAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_grammar);
        grammar = getGrammar();
        listView = findViewById(R.id.grammar_list);
        setData();
    }

    protected ArrayList<ProfileGrammarObject> getGrammar(){
        return (ArrayList<ProfileGrammarObject>) getIntent().getSerializableExtra("Grammar");
    }


    void setData(){
        adapter = new ProfileCheckGrammarAdapter(getApplicationContext(),grammar);
        listView.setAdapter(adapter);
    }

}
