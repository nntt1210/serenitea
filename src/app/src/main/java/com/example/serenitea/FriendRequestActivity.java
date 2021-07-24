package com.example.serenitea;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

public class FriendRequestActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, ReceiveRef, UserRef, FriendsRef, RequestRef;
    String currentUserId;
    private RecyclerView requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();
        ReceiveRef = RootRef.child("receiveFriendRequests").child(currentUserId);
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
                        .setQuery(ReceiveRef, Notification.class)
                        .build();

        FirebaseRecyclerAdapter<Notification, FriendRequestActivity.RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notification, FriendRequestActivity.RequestViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FriendRequestActivity.RequestViewHolder holder, int position, Notification model) {
                String each_sender = getRef(position).getKey();

                ReceiveRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                    if (snapshot2.exists()) {
                                        final String nickname = snapshot2.child(each_sender).child("nickname").getValue().toString();
                                        final String avatar = snapshot2.child(each_sender).child("avatar").getValue().toString();

                                        holder.setNickname(nickname);
                                        holder.setAvatar(avatar);
                                        holder.accept.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AcceptFriendRequest(each_sender);
                                            }
                                        });
                                        holder.decline.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                DeclineFriendRequest(each_sender);
                                            }
                                        });
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
        Button accept, decline;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.friend_request_nickname);
            imageViewAvatar = itemView.findViewById(R.id.friend_request_avatar);
            accept = itemView.findViewById(R.id.btn_accept_friend_request);
            decline = itemView.findViewById(R.id.btn_delete_friend_request);
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

    private void DeclineFriendRequest(String other_user_id) {
        RequestRef.child(currentUserId).child(other_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    RequestRef.child(other_user_id).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ReceiveRef.child(currentUserId).child(other_user_id).removeValue();
                            }
                        }
                    });
                }
            }
        });
    }

    private void AcceptFriendRequest(String other_user_id) {
        FriendsRef.child(currentUserId).child(other_user_id).child("type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //add friend in DB
                    FriendsRef.child(other_user_id).child(currentUserId).child("type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //remove request in DB
                                RequestRef.child(currentUserId).child(other_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            RequestRef.child(other_user_id).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        ReceiveRef.child(currentUserId).child(other_user_id).removeValue();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

}