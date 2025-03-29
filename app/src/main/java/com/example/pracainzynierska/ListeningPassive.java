package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ListeningPassive extends AppCompatActivity {
    ArrayList<ListeningPassiveObject> usableKanji = new ArrayList<>();
    TextView recognitionKanjiImage, amountOfSaidKanji, readingsOfKanji, examplesOfUseInWords, meaningInPolish;
    Button nextKanji, startListeningButton;
    TextView numberOfFinishedKanji, numberOfOverallKanji;
    Button returnButton, endSessionButton, previousKanji;
    TextToSpeech textToSpeech;
    int amountOfHowManyTimesShouldRepeatSingleKanji = 3;
    ArrayList<ChooseKanjiObject> kanjiObjects;
    ArrayList<String> kanjiForRepetition;
    int curIndex = 0;
    int repeatedSoFar = 0;
    boolean isPaused = true;
    boolean shouldSpeak = true;
    ConstraintLayout endSessionScreen, layout;
    ArrayList<ProfileKanjiObject> respondedKanji;
    HashMap<String, String> words;
    private DatabaseReference mDatabase;
    @Override
    public void onBackPressed(){
        if(endSessionScreen.getVisibility() == View.VISIBLE) {
            returnSession();
            return;
        }
        textToSpeech.stop();
        endSessionScreen.setVisibility(View.VISIBLE);
        layout.setEnabled(false);
        nextKanji.setEnabled(false);
        previousKanji.setEnabled(false);
        startListeningButton.setEnabled(false);
    }

    private void returnSession() {
        endSessionScreen.setVisibility(View.INVISIBLE);
        layout.setEnabled(true);
        nextKanji.setEnabled(true);
        previousKanji.setEnabled(true);
        startListeningButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passive_listening);
        initializeTextToSpeech();
        respondedKanji = new ArrayList<>();
        words = new HashMap<>();
        getExtraValues();
        getWordsFromDB();
        getGUI();
        setGUI();
        speakingStarted();
    }

    protected void start(){
        setTextToSpeech(usableKanji.get(curIndex).getReadings(), usableKanji.get(curIndex).getPolishMeaning());
        isPaused = !isPaused;
        startListeningButton.setBackgroundResource(R.drawable.baseline_pause_24);
    }

    public void speakingDone(){
        boolean addNewKanji = true;

        for (ProfileKanjiObject kanji: respondedKanji) {
            if(kanji.getKanji().equals(usableKanji.get(curIndex).getKanji())){
                addNewKanji = false;
                kanji.setCorrect(kanji.getCorrect() + 1);
            }
        }
        if(addNewKanji) {
            ProfileKanjiObject kanji = new ProfileKanjiObject(1, 0, usableKanji.get(curIndex).getKunReading(),
                    usableKanji.get(curIndex).getOnReading(), usableKanji.get(curIndex).getPolishMeaning(),
                    usableKanji.get(curIndex).getLevel(), usableKanji.get(curIndex).getKanji());
            respondedKanji.add(kanji);
        }

        if(repeatedSoFar + 1 < amountOfHowManyTimesShouldRepeatSingleKanji){
            repeatedSoFar += 1;
            setTextToSpeech(usableKanji.get(curIndex).getReadings(), usableKanji.get(curIndex).getPolishMeaning());
        }
        else{
            if(curIndex + 1 == usableKanji.size() && repeatedSoFar + 1 == amountOfHowManyTimesShouldRepeatSingleKanji){
                endSession();
            }
            curIndex += 1;
            repeatedSoFar = 0;
            setTextToSpeech(usableKanji.get(curIndex).getReadings(), usableKanji.get(curIndex).getPolishMeaning());
        }
    }
    private void speakingStarted() {
        setNumberDisplay(repeatedSoFar);
        if (usableKanji.size() > 0) {
            setDisplays(usableKanji.get(curIndex));
        }
        else{
            setWaitForDBConnection();
        }
    }

    private void setWaitForDBConnection(){
        recognitionKanjiImage.setText("");
        readingsOfKanji.setText("");
        meaningInPolish.setText("");
        examplesOfUseInWords.setText("");
    }

    private void setNumberDisplay(int amount){
        String text = (amount + 1) + "/" + (amountOfHowManyTimesShouldRepeatSingleKanji);
        amountOfSaidKanji.setText(text);
    }

    private void setDisplays(ListeningPassiveObject object){
        recognitionKanjiImage.setText(object.getKanji());
        readingsOfKanji.setText(object.getReadings());
        meaningInPolish.setText(object.getPolishMeaning());
        examplesOfUseInWords.setText(getWordsContainingThisKanji(object));
    }

    private String getWordsContainingThisKanji(ListeningPassiveObject object) {
        StringBuilder wordsWithMeanings = new StringBuilder();
        for (String word: object.getWordsContainingThisKanji()) {
            wordsWithMeanings.append(word);
        }
        return wordsWithMeanings.toString();
    }

    private void setTextToSpeech(String kanjiToSpeak, String translation) {
        if (shouldSpeak) {
            String textToSpeak = kanjiToSpeak;
            textToSpeech.setLanguage(Locale.JAPANESE);
            textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_ADD, null, "Japanese");
            textToSpeak = translation;
            textToSpeech.setLanguage(new Locale("pl_PL"));
            textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_ADD, null, "Polish");
        }
    }
    private void getExtraValues(){
        kanjiObjects = (ArrayList<ChooseKanjiObject>) getIntent().getSerializableExtra("allKanji");
        kanjiForRepetition = getIntent().getStringArrayListExtra("kanjiArray");
        //todo: amountOfHowManyTimesShouldRepeatSingleKanji = getIntent().getIntExtra("kanjiAmount", 1);

    }
    private void getWordsWithKanji(){
        for (ChooseKanjiObject singleKanji:kanjiObjects) {
            for (String kanji : kanjiForRepetition) {
                if (singleKanji.getKanji().equals(kanji)) {
                    String meaning = singleKanji.getMeanings();
                    String readings = singleKanji.getMostPopularKunReading()+", " + singleKanji.getMostPopularOnReading();
                    usableKanji.add(new ListeningPassiveObject(meaning,kanji,singleKanji.getLevel(), 0,singleKanji.getMostPopularKunReading(), singleKanji.getMostPopularOnReading(),readings, checkForWordsInDB(kanji)));
                    break;
                }
            }
        }

    }
    private ArrayList<String> checkForWordsInDB(String kanji){
        ArrayList<String> temp = new ArrayList<>();

        Iterator<HashMap.Entry<String, String>> iterator = words.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, String> entry = iterator.next();
            if(entry.getValue().contains(kanji)){
                temp.add(entry.getValue() + " - " + entry.getKey());
                if(temp.size() == 3){
                    break;
                }
            }
        }
        if(temp.size() == 0) {
            temp.add("Brak słów z podanym kanji w bazie");
        }
        return temp;
    }
    private void getWordsFromDB(){

        mDatabase = FirebaseDatabase.getInstance().getReference("exercises/written_exercises");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        if(childSnapshot.exists()){
                            for(DataSnapshot exercises : childSnapshot.getChildren()) {
                                if (exercises.exists()) {
                                    String childKey = exercises.getKey();
                                    String correct = exercises.child("Correct").getValue(String.class) == null? "brak" : exercises.child("Correct").getValue(String.class);
                                    words.put(childKey, correct);
                                }
                            }
                        }
                        
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
    
    private void finishOnCreate(){
        getWordsWithKanji();
        speakingStarted();
    }

    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (textToSpeech.getEngines().size() == 0) {
                    System.out.println("brak silnika");
                } else {
                    if (status == TextToSpeech.SUCCESS) {
                        textToSpeechInitialized();
                    }
                }
            }
        });
    }

    private void textToSpeechInitialized() {
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {

            @Override
            public void onStart(String s) {
                if(Objects.equals(s, "Japanese")) {
                    speakingStarted();
                }
            }
            @Override
            public void onDone(String s) {
                if(Objects.equals(s, "Polish")) {
                    speakingDone();
                }
            }
            @Override
            public void onError(String s) {
            }
            @Override
            public void onStop(String utteranceId, boolean interrupted) {
            }
        });
        textToSpeech.setLanguage(Locale.JAPANESE);
    }

    private void getGUI() {
        recognitionKanjiImage = findViewById(R.id.RecognitionKanjiImage);
        amountOfSaidKanji = findViewById(R.id.amount);
        readingsOfKanji = findViewById(R.id.readingsOfKanji);
        examplesOfUseInWords = findViewById(R.id.examplesOfUseInWords);
        meaningInPolish = findViewById(R.id.meaningInPolish);
        numberOfFinishedKanji = findViewById(R.id.numberOfFinishedKanji);
        numberOfOverallKanji = findViewById(R.id.numberOfOverallKanji);
        previousKanji = findViewById(R.id.previousKanji);

        nextKanji = findViewById(R.id.nextKanji);
        startListeningButton = findViewById(R.id.startListening);
        returnButton = findViewById(R.id.returnButton);
        endSessionButton = findViewById(R.id.endSessionButton);

        endSessionScreen = findViewById(R.id.endSession);
        layout = findViewById(R.id.recognition_background);
    }
    private void setGUI(){
        nextKanji.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                textToSpeech.stop();
                nextKanji();
                start();
                speakingStarted();
            }
        });
        previousKanji.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                textToSpeech.stop();
                previousKanji();
                start();
                speakingStarted();
            }
        });
        startListeningButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isPaused) {
                    start();

                }
                else{
                    pause();

                }
            }
        });

        returnButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                returnSession();
            }
        });
        endSessionButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                endSession();
            }
        });
    }
    private void pause(){
        if(!isPaused) {
            textToSpeech.stop();
        }
        isPaused = !isPaused;
        startListeningButton.setBackgroundResource(R.drawable.baseline_arrow_right_24);
    }
    private void previousKanji(){
        if(curIndex > 0) {
            curIndex -= 1;
            repeatedSoFar = 0;
        }
    }
    private void nextKanji(){
        if(curIndex + 1 < kanjiForRepetition.size()) {
            curIndex += 1;
            repeatedSoFar = 0;
        }
        else{
            endSession();
        }
    }
    private void endSession(){
        shouldSpeak = false;
        textToSpeech.stop();
        Intent intent = new Intent(getApplicationContext(),KanjiExerciseSummary.class);
        intent.putExtra("Menu", ListeningMenu.class);
        intent.putExtra("passiveListening", true);

        intent.putExtra("AllKanji", respondedKanji);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
