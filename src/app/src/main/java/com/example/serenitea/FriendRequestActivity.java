package com.example.serenitea;

import android.app.Person;
import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FriendRequestActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, ReceiveRef, UserRef, FriendsRef, RequestRef;
    String currentUserId;
    private RecyclerView requestList;
    private String saveCurrentDate;
    private ImageButton btnClose;
    private  String name;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();
        ReceiveRef = RootRef.child("receiveFriendRequests").child(currentUserId);
        RequestRef = RootRef.child("friendRequests");
        FriendsRef = RootRef.child("friends");
        UserRef = RootRef.child("users");

        btnClose = findViewById(R.id.btn_close_friend_req);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        requestList = (RecyclerView) findViewById(R.id.list_friend_request);
        requestList.setLayoutManager(linearLayoutManager);

        DisplayAllRequest();

    }

    private void DisplayAllRequest() {
        Query SortFriendRequest = ReceiveRef.orderByChild("date");
        FirebaseRecyclerOptions<FriendRequest> options =
                new FirebaseRecyclerOptions.Builder<FriendRequest>()
                        .setQuery(SortFriendRequest, FriendRequest.class)
                        .build();

        FirebaseRecyclerAdapter<FriendRequest, FriendRequestActivity.RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FriendRequest, FriendRequestActivity.RequestViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FriendRequestActivity.RequestViewHolder holder, int position, FriendRequest model) {
                String each_sender = getRef(position).getKey();

                SortFriendRequest.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        Intent intent = new Intent(FriendRequestActivity.this,PersonProfileActivity.class);
                        String id= getRef(position).getKey().toString();
                        //Toast.makeText(FriendRequestActivity.this,id,Toast.LENGTH_SHORT).show();
                        intent.putExtra("USER_ID",id);
                        intent.putExtra("VIEW_FRIEND", 0);

                        UserRef.child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                {
                                    intent.putExtra("FRIEND_AVATAR", snapshot.child("avatar").getValue().toString());
                                    intent.putExtra("FRIEND_NICKNAME", snapshot.child("nickname").getValue().toString());
                                    intent.putExtra("FRIEND_DOB", snapshot.child("dob").getValue().toString());
                                    intent.putExtra("FRIEND_CUP_OF_TEA",Integer.parseInt(snapshot.child("tea").getValue().toString()));
                                    intent.putExtra("FRIEND_GENDER", snapshot.child("gender").getValue().toString());
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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
    

    private void DeclineFriendRequest(String other_user_id) {
//        Toast.makeText(FriendRequestActivity.this, "decline "+other_user_id, Toast.LENGTH_LONG).show();
        RequestRef.child(currentUserId).child(other_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    RequestRef.child(other_user_id).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ReceiveRef.child(other_user_id).removeValue();
                            }
                        }
                    });
                }
            }
        });
    }

    private void AcceptFriendRequest(String other_user_id) {
        //get date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        saveCurrentDate = currentDate.format(calendar.getTime());


        FriendsRef.child(currentUserId).child(other_user_id).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //add friend in DB
                    FriendsRef.child(other_user_id).child(currentUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                                        ReceiveRef.child(other_user_id).removeValue();
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