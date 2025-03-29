package com.example.pracainzynierska;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProfileFriendsList extends AppCompatActivity {
    ListView listView;
    ProfileFriendsListAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_friends_list);
        setListView();
    }

    void setListView(){
        listView = findViewById(R.id.friends_list);
        ArrayList<FriendObject> friends = MainMenu.user.userFriends;

        adapter = new ProfileFriendsListAdapter(getApplicationContext(),friends);
        listView.setAdapter(adapter);
    }
}
