package com.example.serenitea;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SendQuoteActivity extends AppCompatActivity {

    ArrayList<Quote> listSendQuote;
    SendQuoteAdapter sendQuoteAdapter;
    ListView listViewSendQuote;
    private FirebaseAuth mAuth;
    private DatabaseReference SendQuoteRef;
    String currentUserId;
    private RecyclerView sendQuoteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_quote);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        SendQuoteRef = FirebaseDatabase.getInstance().getReference().child("favorite").child(currentUserId);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

//        listSendQuote = new ArrayList<>();
//        listSendQuote.add(new Quote(1, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));
//        listSendQuote.add(new Quote(2, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));
//        listSendQuote.add(new Quote(3, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));
//        listSendQuote.add(new Quote(3, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));
//
//
//        sendQuoteAdapter = new SendQuoteAdapter(listSendQuote);
//
//        listViewSendQuote = findViewById(R.id.list_send_quote);
//        listViewSendQuote.setAdapter(sendQuoteAdapter);

        sendQuoteList = (RecyclerView) findViewById(R.id.list_send_quote);
        sendQuoteList.setLayoutManager(new LinearLayoutManager(this));


        DisplayAllSendQuote();
    }

    private void DisplayAllSendQuote() {

        FirebaseRecyclerOptions<Quote> options =
                new FirebaseRecyclerOptions.Builder<Quote>()
                        .setQuery(SendQuoteRef, Quote.class)
                        .build();

        FirebaseRecyclerAdapter<Quote, SendQuoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Quote, SendQuoteViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull SendQuoteActivity.SendQuoteViewHolder holder, int position, Quote model) {
                    holder.contentQuote.setText(model.getContent());
            }

            @NonNull

            @Override
            public SendQuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_quote_view, parent, false);
                SendQuoteViewHolder sendHolder = new SendQuoteViewHolder(view);
                return sendHolder;
            }
        };
        //Toast.makeText(SendQuoteActivity.this, "hello", Toast.LENGTH_LONG).show();
        sendQuoteList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class SendQuoteViewHolder extends RecyclerView.ViewHolder {
        TextView contentQuote;

        public SendQuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            contentQuote = itemView.findViewById(R.id.send_quote_content);
        }

        public void setQuoteID(int id) {
        }

        public void setContent(String content) {
        }

        public void setDate(String date) {
        }
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