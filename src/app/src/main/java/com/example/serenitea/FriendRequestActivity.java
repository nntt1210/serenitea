package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendRequestActivity extends AppCompatActivity {

    ArrayList<String> listFriendRequest;
    FriendRequestAdapter friendRequestAdapter;
    ListView listViewFriendRequest;
    ImageButton btnClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        listFriendRequest = new ArrayList<>();
        listFriendRequest.add("Mint");
        listFriendRequest.add("Ziem");
        listFriendRequest.add("Dic");
        listFriendRequest.add("Lisa");

        friendRequestAdapter = new FriendRequestAdapter(listFriendRequest);

        listViewFriendRequest = findViewById(R.id.list_friend_request);
        listViewFriendRequest.setAdapter(friendRequestAdapter);

        btnClose = findViewById(R.id.btn_close_friend_req);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}