package com.example.pracainzynierska;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moji4j.MojiConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddWritingExercise extends AppCompatActivity {
    private PopupMenu popupMenu, popupMenu2, popupMenu3, popupMenu4, popupMenu5, popupMenu6, popupMenu7, popupMenu8;
    EditText polishText;
    EditText correctText1, correctText2;
    EditText incorrectText1, incorrectText2, incorrectText3, incorrectText4, incorrectText5, incorrectText6;
    Button correctButton1, correctButton2;
    Button incorrectButton1, incorrectButton2, incorrectButton3, incorrectButton4, incorrectButton5, incorrectButton6;
    Button addTask;
    MojiConverter converter = new MojiConverter();
    ArrayList<SimpleKanjiObject> allKanji = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_writting_exercise);
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
        polishText = findViewById(R.id.polishText);
        correctText1 = findViewById(R.id.translationInput);
        correctText2 = findViewById(R.id.translationInput2);
        incorrectText1 = findViewById(R.id.translationInput3);
        incorrectText2 = findViewById(R.id.translationInput4);
        incorrectText3 = findViewById(R.id.translationInput5);
        incorrectText4 = findViewById(R.id.translationInput6);
        incorrectText5 = findViewById(R.id.translationInput7);
        incorrectText6 = findViewById(R.id.translationInput8);
        addTask = findViewById(R.id.create);
        correctButton1 = findViewById(R.id.checkKanji);
        correctButton2 = findViewById(R.id.checkKanji2);
        incorrectButton1 = findViewById(R.id.checkKanji3);
        incorrectButton2 = findViewById(R.id.checkKanji4);
        incorrectButton3 = findViewById(R.id.checkKanji5);
        incorrectButton4 = findViewById(R.id.checkKanji6);
        incorrectButton5 = findViewById(R.id.checkKanji7);
        incorrectButton6 = findViewById(R.id.checkKanji8);
    }
    void setGUI(){
        setButtons(correctButton1, correctText1, popupMenu);
        setButtons(correctButton2, correctText2, popupMenu2);
        setButtons(incorrectButton1, incorrectText1, popupMenu3);
        setButtons(incorrectButton2, incorrectText2, popupMenu4);
        setButtons(incorrectButton3, incorrectText3, popupMenu5);
        setButtons(incorrectButton4, incorrectText4, popupMenu6);
        setButtons(incorrectButton5, incorrectText5, popupMenu7);
        setButtons(incorrectButton6, incorrectText6, popupMenu8);
        addTask.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExercise();
            }
        });

    }

    void setButtons(Button button, TextView textView, PopupMenu popupMenu){
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
            String shouldAddKanji = (singleKanji.getKun_yomi() != null && singleKanji.getKun_yomi().startsWith(hiragana)) ? singleKanji.getKanji() : (singleKanji.getOn_yomi() != null && singleKanji.getOn_yomi().startsWith(hiragana)) ? singleKanji.getKanji() : null;
            if (shouldAddKanji != null) {
                popupMenu.getMenu().add(shouldAddKanji);
            }
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                textView.setText(item.getTitle());
                return true;
            }
        });

        popupMenu.show();
    }
    void addExercise() {
        if (polishText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Wpisz zdanie po polsku", Toast.LENGTH_SHORT).show();
            return;
        }
        String correct1 = correctText1.getText().toString().trim();
        String correct2 = correctText2.getText().toString().trim();
        String incorrect1 = incorrectText1.getText().toString().trim();
        String incorrect2 = incorrectText2.getText().toString().trim();
        String incorrect3 = incorrectText3.getText().toString().trim();
        String incorrect4 = incorrectText4.getText().toString().trim();
        String incorrect5 = incorrectText5.getText().toString().trim();
        String incorrect6 = incorrectText6.getText().toString().trim();

        if(incorrect1.equals("") || incorrect2.equals("") || incorrect3.equals("") || incorrect4.equals("") || incorrect5.equals("") || incorrect6.equals("")){
            Toast.makeText(getApplicationContext(), "Wpisz niepoprawne odpowiedzi", Toast.LENGTH_SHORT).show();
            return;
        }
        if(correct1.equals("") || correct2.equals("")){
            Toast.makeText(getApplicationContext(), "Wpisz poprawne odpowiedzi", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast.makeText(getApplicationContext(), "Dodawanie cwiczenia nie zaimplementowane", Toast.LENGTH_SHORT).show();

        String nameOfExercise = polishText.getText().toString().trim();
        nameOfExercise = Character.toUpperCase(nameOfExercise.charAt(0)) + nameOfExercise.substring(1);;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("exercises/written_exercises/"+MainMenu.user.nick+"/"+nameOfExercise);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("Correct",correct1 + correct2);
        dataMap.put("Incorrect1", incorrect1+incorrect2);
        dataMap.put("Incorrect2", incorrect3 + incorrect4);
        dataMap.put("Incorrect3", incorrect5 + incorrect6);


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
