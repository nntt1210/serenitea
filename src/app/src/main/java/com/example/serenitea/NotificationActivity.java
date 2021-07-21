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

public class NotificationActivity extends AppCompatActivity {

    ArrayList<String> listNotification;
    NotificationAdapter notificationAdapter;
    ListView listViewNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        listNotification = new ArrayList<>();
        listNotification.add("Mint");
        listNotification.add("Ziem");
        listNotification.add("Dic");
        listNotification.add("Lisa");

        notificationAdapter = new NotificationAdapter(listNotification);

        listViewNotification = findViewById(R.id.list_notification);
        listViewNotification.setAdapter(notificationAdapter);
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