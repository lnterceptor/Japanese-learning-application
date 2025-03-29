package com.example.pracainzynierska;

import java.util.ArrayList;

public class ListeningSentenceRecognitionFriendChoice extends RecognitionComplexFriendChoice{
    @Override
    protected void setAdapter(ArrayList<FriendObject> friends){
        adapter = new ListeningSentenceFriendsListAdapter(getApplicationContext(),friends);
    }
}
