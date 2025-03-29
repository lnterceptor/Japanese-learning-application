package com.example.pracainzynierska;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moji4j.MojiConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddListeningExercise extends AppCompatActivity {
    private PopupMenu popupMenu;
    EditText japaneseText, kanjiText;
    EditText correctText, incorrectText2, incorrectText3, incorrectText1;
    Button translateKanji, translateToJapanese;
    Button addTask;
    MojiConverter converter = new MojiConverter();
    ArrayList<SimpleKanjiObject> allKanji = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_listening_exercise);
        readAllKanji("jlpt_5");
        readAllKanji("jlpt_4");
        readAllKanji("jlpt_3");
        readAllKanji("jlpt_2");
        readAllKanji("jlpt_1");
        getGUI();
        setGUI();
    }
    void readAllKanji(String lvl){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("kanji");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot jlptSnapshot = dataSnapshot.child(lvl);
                    if(jlptSnapshot.exists()){
                        for (DataSnapshot snap: jlptSnapshot.getChildren()) {
                            String reading_kun = snap.child("readings_kun").child("0").getValue(String.class);
                            String reading_on = snap.child("readings_on").child("0").getValue(String.class);
                            String kanji = snap.getKey();
                            allKanji.add(new SimpleKanjiObject(kanji, reading_on,reading_kun));
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Brak internetu, nie udało się wczytać kanji", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Brak internetu, nie udało się wczytać kanji", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Brak internetu, nie udało się wczytać kanji", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void getGUI(){

        japaneseText = findViewById(R.id.japaneseText);
        kanjiText = findViewById(R.id.translationInput);

        translateKanji = findViewById(R.id.checkKanji);
        translateToJapanese = findViewById(R.id.translateToJapanese);

        correctText = findViewById(R.id.translationInput1);
        incorrectText1 = findViewById(R.id.translationInput2);
        incorrectText2 = findViewById(R.id.translationInput3);
        incorrectText3 = findViewById(R.id.translationInput4);


        addTask = findViewById(R.id.create);
    }
    void setGUI(){
        setTranslateKanjiButton(translateKanji, kanjiText, popupMenu);
        setTranslateSentenceButton(translateToJapanese, japaneseText);
        addTask.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExercise();
            }
        });

    }
    void setTranslateSentenceButton(Button button, TextView textView){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(converter.convertRomajiToHiragana(textView.getText().toString()));
            }
        });
    }
    void setTranslateKanjiButton(Button button, TextView textView, PopupMenu popupMenu){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoicesPopup(view, textView, popupMenu);
            }
        });
    }

    private void showChoicesPopup(View anchorView, TextView textView, PopupMenu popupMenu) {
        if(popupMenu == null) {
            popupMenu = new PopupMenu(this, anchorView);
        }
        else{
            popupMenu.getMenu().clear();
        }
        String hiragana = converter.convertRomajiToHiragana(textView.getText().toString());
        textView.setText(hiragana);

        for (SimpleKanjiObject singleKanji: allKanji) {
            String shouldAddKanji = (singleKanji.getKun_yomi() != null && singleKanji.getKun_yomi().startsWith(hiragana))
                    ? singleKanji.getKanji() : (singleKanji.getOn_yomi() != null && singleKanji.getOn_yomi().startsWith(hiragana)) ? singleKanji.getKanji() : null;
            if (shouldAddKanji != null) {
                popupMenu.getMenu().add(shouldAddKanji);
            }
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                textView.setText(item.getTitle());
                japaneseText.setText(japaneseText.getText().toString() + item.getTitle());
                return true;
            }
        });

        popupMenu.show();
    }
    boolean checkIfCorrect(String correct, String incorrect1, String incorrect2, String incorrect3){
        if(incorrect1.equals("") || incorrect2.equals("") || incorrect3.equals("")){
            Toast.makeText(getApplicationContext(), "Wpisz niepoprawne odpowiedzi", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(correct.equals("")){
            Toast.makeText(getApplicationContext(), "Wpisz poprawną odpowiedź", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    void addExercise() {
        if (japaneseText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Wpisz zdanie po japońsku", Toast.LENGTH_SHORT).show();
            return;
        }
        String correct1 = correctText.getText().toString().trim();
        String incorrect1 = incorrectText1.getText().toString().trim();
        String incorrect2 = incorrectText2.getText().toString().trim();
        String incorrect3 = incorrectText3.getText().toString().trim();
        if(!checkIfCorrect(correct1, incorrect1, incorrect2, incorrect3)) return;

        String nameOfExercise = japaneseText.getText().toString().trim();
        nameOfExercise = Character.toUpperCase(nameOfExercise.charAt(0)) + nameOfExercise.substring(1);;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("exercises/listening_exercises/"+MainMenu.user.nick+"/"+nameOfExercise);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("Correct",correct1);
        dataMap.put("Incorrect1", incorrect1);
        dataMap.put("Incorrect2", incorrect2);
        dataMap.put("Incorrect3", incorrect3);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.setValue(dataMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        Toast.makeText(getApplicationContext(), "Dodano cwiczenie", Toast.LENGTH_SHORT).show();
        finish();
    }
}
