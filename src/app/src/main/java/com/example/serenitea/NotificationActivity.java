package com.example.serenitea;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, NotiRef, UserRef;
    String currentUserId;
    private RecyclerView notiList;
    private final List<Notification> notificationList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();
        NotiRef = RootRef.child("notification").child(currentUserId);
        UserRef = RootRef.child("users");

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        DisplayAllNotification();

        FetchNotification();
    }

    private void FetchNotification() {
        NotiRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    notificationList.add(notification);
                    notificationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DisplayAllNotification() {
        notificationAdapter = new NotificationAdapter(notificationList, getApplicationContext());
        notiList = (RecyclerView) findViewById(R.id.list_notification);
        linearLayoutManager = new LinearLayoutManager(this);
        notiList.setHasFixedSize(true);
        notiList.setLayoutManager(linearLayoutManager);
        notiList.setAdapter(notificationAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}