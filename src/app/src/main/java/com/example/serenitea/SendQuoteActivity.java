package com.example.serenitea;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SendQuoteActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, FavoriteQuoteRef, QuoteRef;
    String currentUserId;
    private RecyclerView sendQuoteList;
    private String receiverID = "dOY7tj1STpXCJ0DJ6ArwTNoI0052", quoteID;
    private String saveCurrentDate, saveCurrentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_quote);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();
        FavoriteQuoteRef = RootRef.child("favorite").child(currentUserId);
        QuoteRef = RootRef.child("quotes");

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        sendQuoteList = (RecyclerView) findViewById(R.id.list_send_quote);
        sendQuoteList.setLayoutManager(new LinearLayoutManager(this));

        DisplayAllSendQuote();
    }

    private void DisplayAllSendQuote() {

        FirebaseRecyclerOptions<Quote> options =
                new FirebaseRecyclerOptions.Builder<Quote>()
                        .setQuery(FavoriteQuoteRef, Quote.class)
                        .build();

        FirebaseRecyclerAdapter<Quote, SendQuoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Quote, SendQuoteViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull SendQuoteActivity.SendQuoteViewHolder holder, int position, Quote model) {
                String each_quote_id = getRef(position).getKey();
                QuoteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            final String content_each_quote = snapshot.child(each_quote_id).child("content").getValue().toString();
                            holder.setContent(content_each_quote);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //click event on each quote
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(SendQuoteActivity.this, each_quote_id, Toast.LENGTH_LONG).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(SendQuoteActivity.this);
                        builder.setMessage("Send this quote to FRIEND_NAME?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SaveSentQuoteToDatabase(each_quote_id);
                            }
                        }).setNegativeButton("Cancel", null);
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });
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
        TextView textViewContentQuote;

        public SendQuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContentQuote = itemView.findViewById(R.id.send_quote_content);
        }

        public void setQuoteID(int id) {
        }

        public void setContent(String content) {
            textViewContentQuote.setText(content);
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


    private void SaveSentQuoteToDatabase(String quoteID) {
        String data_ref = "notification/" + receiverID;
        DatabaseReference notiKey = RootRef.child("notification").child(receiverID).push();
        String noti_push_id = notiKey.getKey();

        //get date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        saveCurrentDate = currentDate.format(calendar.getTime());

        //get time
//        Calendar time = Calendar.getInstance();
//        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm aa");
//        saveCurrentTime = currentTime.format(time.getTime());

        Map sendMap = new HashMap();
        sendMap.put("date", saveCurrentDate);
//        sendMap.put("time", saveCurrentTime);
        sendMap.put("quote", quoteID);
        sendMap.put("from", currentUserId);

        Map detailSendMap = new HashMap();
        detailSendMap.put(data_ref + "/" + noti_push_id, sendMap);

        RootRef.updateChildren(detailSendMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SendQuoteActivity.this, "This quote has been sent to FRIEND_NAME", Toast.LENGTH_LONG).show();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(SendQuoteActivity.this, "Error" + message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}