package com.example.pracainzynierska;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChooseKanjiAdapter extends BaseExpandableListAdapter {


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

        //OnClickListenersForButtons/Change them into Image views

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
        }
        else{
            convertView.setBackgroundColor(context.getResources().getColor(R.color.notSelectedColor));
        }




        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
