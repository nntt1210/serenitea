package com.example.serenitea;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationActivity extends AppCompatActivity {
    
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, NotiRef, UserRef;
    String currentUserId;
    private RecyclerView notiList;

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

        notiList = (RecyclerView) findViewById(R.id.list_notification);
        notiList.setLayoutManager(new LinearLayoutManager(this));

        DisplayAllNotification();
    }

    private void DisplayAllNotification() {

        FirebaseRecyclerOptions<Quote> options =
                new FirebaseRecyclerOptions.Builder<Quote>()
                        .setQuery(NotiRef, Quote.class)
                        .build();

        FirebaseRecyclerAdapter<Quote, NotificationActivity.NotiViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Quote, NotificationActivity.NotiViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull NotificationActivity.NotiViewHolder holder, int position, Quote model) {
                String noti_id = getRef(position).getKey();

                //each user
                NotiRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String sender_id = snapshot.child(noti_id).child("from").getValue().toString();
//                            Toast.makeText(NotificationActivity.this, snapshot.child(noti_id).child("quote").getValue().toString(), Toast.LENGTH_LONG).show();
                            UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                    if (snapshot2.exists()) {
                                        final String sender_name = snapshot2.child(sender_id).child("nickname").getValue().toString();
                                        final String avatar = snapshot2.child(sender_id).child("avatar").getValue().toString();
                                        holder.setContent(sender_name);
                                        holder.setAvatar(avatar);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
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

                    }
                });
            }

            @NonNull
            @Override
            public NotificationActivity.NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_view, parent, false);
                NotificationActivity.NotiViewHolder notiHolder = new NotificationActivity.NotiViewHolder(view);
                return notiHolder;
            }
        };

        notiList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatar;
        TextView textViewNotification;

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.notification_avatar);
            textViewNotification = itemView.findViewById(R.id.notification_content);
        }
        public void setAvatar(String id) {
            int resource = getResources().getIdentifier(id, "drawable", getPackageName());
            imageViewAvatar.setImageResource(resource);
        }
        public void setContent(String content) {
            textViewNotification.setText(content + " sent you a quote.");
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