package com.example.pracainzynierska;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.List;

public class ListeningSentenceFriendsListAdapter extends RecognitionComplexFriendsListAdapter{
    public ListeningSentenceFriendsListAdapter(@NonNull Context context, List<FriendObject> objectList) {
        super(context, objectList);
    }
    @Override
    protected Intent setIntent(){
        return new Intent(context, ListeningSentenceRecognition.class);
    }
}
