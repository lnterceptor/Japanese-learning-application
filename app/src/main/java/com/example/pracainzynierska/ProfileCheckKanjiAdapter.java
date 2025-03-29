package com.example.pracainzynierska;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;

//public class ProfileCheckKanjiAdapter extends ArrayAdapter<ProfileKanjiObject> {
//    public ProfileCheckKanjiAdapter(@NonNull Context context, List<ProfileKanjiObject> objectList) {
//        super(context, 0, objectList);
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        if (view == null) {
//            view = LayoutInflater.from(getContext()).inflate(R.layout.profile_  grammar_object, parent, false);
//        }
//        TextView progress = view.findViewById(R.id.progressPercentage);
//        TextView title = view.findViewById(R.id.TitleOfKanji);
//        ProgressBar percentDone = view.findViewById(R.id.percentDone);
//
//        ProfileGrammarObject object = getItem(position);
//        int progressToSet = (int) (((float) object.getAmount() / object.getOverallAmount()) * 100);
//        progress.setText(progressToSet + "%");
//        title.setText(object.getTitle());
//        percentDone.setProgress(object.getAmount());
//        percentDone.setMax(object.getOverallAmount());
//
//        return view;
//    }
//}

public class ProfileCheckKanjiAdapter extends BaseExpandableListAdapter {
    private ConstraintLayout layout;
    private Context context;
    private List<List<ProfileKanjiObject>> kanji = new ArrayList<>();
    private int [] numList = {79,166,367,367,1232,4676};

    public ProfileCheckKanjiAdapter(Context context, ArrayList<ProfileKanjiObject> kanjiList) {
        this.context = context;
        this.kanji.add(giveSetOfKanji("N5", kanjiList));
        this.kanji.add(giveSetOfKanji("N4", kanjiList));
        this.kanji.add(giveSetOfKanji("N3", kanjiList));
        this.kanji.add(giveSetOfKanji("N2", kanjiList));
        this.kanji.add(giveSetOfKanji("N1", kanjiList));
    }

    ArrayList<ProfileKanjiObject> giveSetOfKanji(String setLevel, ArrayList<ProfileKanjiObject> kanjiList){
        ArrayList<ProfileKanjiObject> listOfKanji = new ArrayList<>();
        for (ProfileKanjiObject kanji: kanjiList) {
            if(setLevel.equals(kanji.getLevel())){
                listOfKanji.add(kanji);
            }
        }
        return listOfKanji;
    }

    @Override
    public int getGroupCount() {
        return kanji.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return kanji.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return kanji.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return kanji.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.check_kanji_header, null);
        }

        TextView item = convertView.findViewById(R.id.TitleOfKanji);
        item.setText("N"+String.valueOf(5 - groupPosition));

        TextView progressPercentage = convertView.findViewById(R.id.progressPercentage);
        ProgressBar progressBar = convertView.findViewById(R.id.percentDone);

        progressBar.setProgress(kanji.get(groupPosition).size());

        progressBar.setMax(numList[groupPosition]);
        progressPercentage.setText(String.valueOf((int)((float)kanji.get(groupPosition).size() / (float)numList[groupPosition] * 100))+"%");

        layout = convertView.findViewById(R.id.backGroundHeader);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                else {
                    for(int i = 0; i < getGroupCount(); i++){
                        ((ExpandableListView) parent).collapseGroup(i);
                    }
                    ((ExpandableListView) parent).expandGroup(groupPosition, true);
                }
            }
        });

        //OnClickListenersForButtons/Change them into Image views

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ProfileKanjiObject kanjiObject = (ProfileKanjiObject) getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.check_kanji_object, null);
        }

        TextView titleOfKanji = convertView.findViewById(R.id.TitleOfKanji);
        TextView kunReading = convertView.findViewById(R.id.kunReading);
        TextView onReading = convertView.findViewById(R.id.onReading);
        TextView correctlyDone = convertView.findViewById(R.id.correctlyDone);
        TextView incorrectlyDone = convertView.findViewById(R.id.incorrectlyDone);

        titleOfKanji.setText(kanjiObject.getKanji());
        kunReading.setText(kanjiObject.getKun_readings());
        onReading.setText(kanjiObject.getOn_readings());
        correctlyDone.setText(String.valueOf(kanjiObject.getCorrect()));
        incorrectlyDone.setText(String.valueOf(kanjiObject.getIncorrect()));


        ProgressBar doneWell = convertView.findViewById(R.id.percentCorrrect);
        ProgressBar doneBad = convertView.findViewById(R.id.percentIncorrect);

        doneWell.setProgress(kanjiObject.getCorrect());
        doneWell.setMax(kanjiObject.getCorrect() + kanjiObject.getIncorrect());
        doneBad.setProgress(kanjiObject.getIncorrect());
        doneBad.setMax(kanjiObject.getCorrect() + kanjiObject.getIncorrect());


//        if(kanjiObject.isSelected()){
//            convertView.setBackgroundColor(context.getResources().getColor(R.color.selectedColor));
//            colorGroup(groupPosition);
//        }
//        else{
//            convertView.setBackgroundColor(context.getResources().getColor(R.color.notSelectedColor));
//        }


        return convertView;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
