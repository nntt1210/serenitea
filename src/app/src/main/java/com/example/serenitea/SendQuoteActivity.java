package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class SendQuoteActivity extends AppCompatActivity {

    ArrayList<Quote> listSendQuote;
    SendQuoteAdapter sendQuoteAdapter;
    ListView listViewSendQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_quote);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        listSendQuote = new ArrayList<>();
        listSendQuote.add(new Quote(1, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));
        listSendQuote.add(new Quote(2, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));
        listSendQuote.add(new Quote(3, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));
        listSendQuote.add(new Quote(3, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));

        sendQuoteAdapter = new SendQuoteAdapter(listSendQuote);

        listViewSendQuote = findViewById(R.id.list_send_quote);
        listViewSendQuote.setAdapter(sendQuoteAdapter);
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