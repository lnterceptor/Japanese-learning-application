package com.example.pracainzynierska;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GrammarTheory extends AppCompatActivity {
    TextView title, exampleOfUse, definition;
    GrammarObject grammarObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveGrammarObject();
        setContentView(R.layout.grammar_object_theory);
        getGUI();
        setGUI();
    }

    protected void retrieveGrammarObject(){
        grammarObject = (GrammarObject) getIntent().getSerializableExtra("grammarObject");
    }
    void getGUI(){
        title = findViewById(R.id.title);
        exampleOfUse = findViewById(R.id.examplesOfUse);
        definition = findViewById(R.id.definition);
    }
    void setGUI(){
        title.setText(grammarObject.getTitle());
        String examplesOfUse = "Przykładowe użycia:\n";
        for (GrammarTranslationPairObject text: grammarObject.examplesOfUse) {
            examplesOfUse += text.polishMeaning + "\n" + text.japaneseMeaning + "\n";
        }
        exampleOfUse.setText(examplesOfUse);
        String definitionText = "Definicja:\n";
        definitionText += grammarObject.definition;
        definition.setText(definitionText);
    }

}
