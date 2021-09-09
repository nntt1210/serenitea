package com.example.serenitea;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Collections;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private ImageButton btnClose;
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

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.theme));

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();
        NotiRef = RootRef.child("notification").child(currentUserId);
        UserRef = RootRef.child("users");

        btnClose = findViewById(R.id.btn_close_noti);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        FetchNotification();
        DisplayAllNotification();
    }

    private void FetchNotification() {
        notificationList.clear();
        NotiRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String key = snapshot.getKey();
                    Notification notification = snapshot.getValue(Notification.class);
                    notification.setKey(key);
                    notificationList.add(notification);
                    notificationAdapter.notifyDataSetChanged();
                }
                Collections.sort(notificationList);
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