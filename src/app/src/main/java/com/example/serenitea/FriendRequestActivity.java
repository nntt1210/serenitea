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

public class FriendRequestActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, RequestRef, UserRef;
    String currentUserId;
    private RecyclerView requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();
        RequestRef = RootRef.child("friendRequests").child(currentUserId);
        UserRef = RootRef.child("users");

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        requestList = (RecyclerView) findViewById(R.id.list_friend_request);
        requestList.setLayoutManager(new LinearLayoutManager(this));

        DisplayAllRequest();

    }

    private void DisplayAllRequest() {

        FirebaseRecyclerOptions<Notification> options =
                new FirebaseRecyclerOptions.Builder<Notification>()
                        .setQuery(RequestRef, Notification.class)
                        .build();

        FirebaseRecyclerAdapter<Notification, FriendRequestActivity.RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notification, FriendRequestActivity.RequestViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FriendRequestActivity.RequestViewHolder holder, int position, Notification model) {
                String each_sender = getRef(position).getKey();
                DatabaseReference databaseReference = getRef(position).child("type");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.getValue().toString().equals("received")) {
                                //Toast.makeText(FriendRequestActivity.this, each_sender.child("type"), Toast.LENGTH_LONG).show();
                                UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                        if (snapshot2.exists()) {
                                            final String nickname = snapshot2.child(each_sender).child("nickname").getValue().toString();
                                            final String avatar = snapshot2.child(each_sender).child("avatar").getValue().toString();

                                            holder.setNickname(nickname);
                                            holder.setAvatar(avatar);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
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
            public FriendRequestActivity.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_view, parent, false);
                FriendRequestActivity.RequestViewHolder holder = new FriendRequestActivity.RequestViewHolder(view);
                return holder;
            }
        };
        requestList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    public class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageViewAvatar;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.friend_request_nickname);
            imageViewAvatar = itemView.findViewById(R.id.friend_request_avatar);
        }

        public void setNickname(String name) {
            textViewName.setText(name);
        }


        public void setAvatar(String id) {
            int resource = getResources().getIdentifier(id, "drawable", getPackageName());
            imageViewAvatar.setImageResource(resource);
        }
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