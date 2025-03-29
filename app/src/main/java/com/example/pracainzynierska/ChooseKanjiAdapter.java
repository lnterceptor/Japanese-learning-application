package com.example.pracainzynierska;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class ChooseKanjiAdapter extends BaseExpandableListAdapter {
    private ConstraintLayout layout;
    private Context context;
    private List<ChooseKanjiHeader> kanji;


    public ChooseKanjiAdapter(Context context, ArrayList<ChooseKanjiHeader> kanjiList) {
        this.context = context;
        this.kanji = kanjiList;
    }

    @Override
    public int getGroupCount() {
        return kanji.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return kanji.get(groupPosition).getKanjiSet().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return kanji.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return kanji.get(groupPosition).getKanjiSet().get(childPosition);
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

        ChooseKanjiHeader kanjiHeader = (ChooseKanjiHeader) getGroup(groupPosition);
        String setName = kanjiHeader.getTitle();
        //String title = getGroup(groupPosition).toString();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.kanji_choose_list_header, null);
        }

        TextView item = convertView.findViewById(R.id.TitleOfSet);
        item.setText(setName);

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

        Button showKanji = convertView.findViewById(R.id.imageOfKanji);
        showKanji.setBackgroundColor(context.getResources().getColor(R.color.partialySelectedButton));
        showKanji.setText(kanjiHeader.getKanjiSet().get(0).getKanji());


        Button chooseAllKanji = convertView.findViewById(R.id.selectAllKanji);
        chooseAllKanji.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kanjiHeader.isChoosen()) {
                    for (int i = 0; i < getChildrenCount(groupPosition); i++) {
                        ChooseKanjiObject child = (ChooseKanjiObject) getChild(groupPosition, i);

                        if(!child.isSelected()) {
                            child.changeSelected();
                            getChildView(groupPosition, i, false, null, parent);
                            notifyDataSetChanged();

                            ChooseKanji.changeArrayOfKanji(child.getKanji(), child.isSelected());
                        }
                    }
                    kanjiHeader.setChoosen(true);
                }
                else{
                    for (int i = 0; i < getChildrenCount(groupPosition); i++) {
                        ChooseKanjiObject child = (ChooseKanjiObject) getChild(groupPosition, i);

                        if(child.isSelected()) {
                            child.changeSelected();
                            getChildView(groupPosition, i, false, null, parent);
                            notifyDataSetChanged();

                            ChooseKanji.changeArrayOfKanji(child.getKanji(), child.isSelected());
                        }
                    }
                    kanjiHeader.setChoosen(false);
                }
            }
        });

        colorGroup(groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChooseKanjiObject kanjiObject = (ChooseKanjiObject) getChild(groupPosition, childPosition);

        String kanji = kanjiObject.getKanji();
        String meanings = kanjiObject.getMeanings();
        String reading_on = kanjiObject.getMostPopularOnReading();
        String reading_kun = kanjiObject.getMostPopularKunReading();
        if(reading_kun.length() > 0 && reading_on.length() > 0){
            reading_on += ", ";
        }
        String readings = reading_on + reading_kun;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.kanji_choose_list_object, null);


        }

        TextView titleView = convertView.findViewById(R.id.TitleOfKanjiChoose);
        TextView descriptionView = convertView.findViewById(R.id.DescriptionOfKanjiChoose);
        TextView kanjiView = convertView.findViewById(R.id.kanjiText);

        titleView.setText(meanings);
        descriptionView.setText(readings);
        kanjiView.setText(kanji);

        if(kanjiObject.isSelected()){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.selectedColor));
            colorGroup(groupPosition);
        }
        else{
            convertView.setBackgroundColor(context.getResources().getColor(R.color.notSelectedColor));
        }


        return convertView;
    }

    public void colorGroup(int groupPosition){
        int countSelected = 0;
        for (int i = 0; i < getChildrenCount(groupPosition); i++) {
            ChooseKanjiObject child = (ChooseKanjiObject) getChild(groupPosition, i);
            if(child.isSelected()) {
                countSelected += 1;
            }
        }
        if(countSelected == 0){
            layout.setBackgroundColor(context.getResources().getColor(R.color.notSelectedColor));
            ChooseKanjiHeader kanjiHeader = (ChooseKanjiHeader) getGroup(groupPosition);
            kanjiHeader.setChoosen(false);
        }
        else if(countSelected < getChildrenCount(groupPosition)){
            layout.setBackgroundColor(context.getResources().getColor(R.color.partialySelectedButton));
        }
        else{
            layout.setBackgroundColor(context.getResources().getColor(R.color.selectedColor));
            ChooseKanjiHeader kanjiHeader = (ChooseKanjiHeader) getGroup(groupPosition);
            kanjiHeader.setChoosen(true);
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
