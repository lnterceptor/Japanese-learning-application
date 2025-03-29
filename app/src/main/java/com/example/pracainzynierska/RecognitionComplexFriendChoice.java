package com.example.pracainzynierska;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecognitionComplexFriendChoice extends AppCompatActivity {
    ArrayList<FriendObject> friendObjects = MainMenu.user.userFriends;
    TextView friendsText;
    ListView listView;
    ProfileFriendsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_friends_list);
        getGUI();
        setGUI();
    }
    void getGUI(){
        friendsText = findViewById(R.id.friendsText);
        listView = findViewById(R.id.friends_list);
    }
    void setGUI(){
        friendsText.setText("Ćwiczenia od znajomych");
        setListView();
    }

    void setListView(){
        listView = findViewById(R.id.friends_list);
        ArrayList<FriendObject> friends = MainMenu.user.userFriends;

        setAdapter(friends);
        listView.setAdapter(adapter);
    }
    protected void setAdapter(ArrayList<FriendObject> friends){
        adapter = new RecognitionComplexFriendsListAdapter(getApplicationContext(),friends);
    }

}
