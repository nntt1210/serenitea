package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendRequestActivity extends AppCompatActivity {

    ArrayList<String> listFriendRequest;
    FriendRequestAdapter friendRequestAdapter;
    ListView listViewFriendRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        listFriendRequest = new ArrayList<>();
        listFriendRequest.add("Mint");
        listFriendRequest.add("Ziem");
        listFriendRequest.add("Dic");
        listFriendRequest.add("Lisa");

        friendRequestAdapter = new FriendRequestAdapter(listFriendRequest);

        listViewFriendRequest = findViewById(R.id.list_friend_request);
        listViewFriendRequest.setAdapter(friendRequestAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}