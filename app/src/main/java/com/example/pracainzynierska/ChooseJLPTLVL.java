package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class ChooseJLPTLVL extends AppCompatActivity {

    List<JLPTObject> options = new ArrayList<JLPTObject>();
    ListView listView;
    private ArrayAdapter<JLPTObject> adapter ;
    Class<?> nextActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        nextActivity = (Class<?>) intent.getSerializableExtra("nextActivity");

        setContentView(R.layout.grammar_menu);
        listView = findViewById(R.id.grammar_list);

        options.add(new JLPTObject("N5","Początkujący","日", R.drawable.ic_launcher_background));
        options.add(new JLPTObject("N4","Podstawowy","不", R.drawable.ic_launcher_background));
        options.add(new JLPTObject("N3","Średnio Zaawansowany","与", R.drawable.ic_launcher_background));
        options.add(new JLPTObject("N2","Zaawansowany", "並", R.drawable.ic_launcher_background));
        options.add(new JLPTObject("N1","Ekspert","丁", R.drawable.ic_launcher_background));
        options.add(new JLPTObject("Inne","Inne","㐆", R.drawable.ic_launcher_background));

        adapter = new JLPTAdapter(this, options, nextActivity);
        listView.setAdapter(adapter);
    }
}
