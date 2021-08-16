package com.example.serenitea;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FriendsActivity extends Fragment {

    GridView friendGrid;
    ArrayList<Friend> listFriend;
    private EditText searchBar;
    private ImageButton btSearch;
    private String inSearch;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference userRef, fr_listRef;
    private String curId = mAuth.getCurrentUser().getUid();
    private String id;
    private FriendAdapter friendAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_friends, container, false);

        userRef = FirebaseDatabase.getInstance().getReference();
        friendGrid = (ExpandableHeightGridView) view.findViewById(R.id.grid_view_friend); // init GridView
        searchBar = (EditText) view.findViewById(R.id.search_friend_bar);
        btSearch = (ImageButton) view.findViewById(R.id.search);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //            searchBtn();
                getData(searchBar.getText().toString());
            }
        });

        listFriend = new ArrayList<>();
        fr_listRef = FirebaseDatabase.getInstance().getReference().child("friends").child(curId);

        friendGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PersonProfileActivity.class);
                intent.putExtra("USER_ID", listFriend.get(position).id);
                intent.putExtra("VIEW_FRIEND", 0);
                intent.putExtra("FRIEND_AVATAR", listFriend.get(position).avatar_id);
                intent.putExtra("FRIEND_NICKNAME", listFriend.get(position).nickname);
                intent.putExtra("FRIEND_DOB", listFriend.get(position).dob);
                intent.putExtra("FRIEND_CUP_OF_TEA", listFriend.get(position).cup_of_tea);
                intent.putExtra("FRIEND_GENDER", listFriend.get(position).gender);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchFriends();
        displayAllFriends();
    }

    public void fetchFriends() {
        listFriend.clear();
        fr_listRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String key = snapshot.getKey();
                    userRef = FirebaseDatabase.getInstance().getReference().child("users").child(key);
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot sn) {
                            if (sn.exists()) {
                                String n = sn.child("nickname").getValue().toString(),
                                        ava = sn.child("avatar").getValue().toString(),
                                        d = sn.child("dob").getValue().toString(),
                                        g = sn.child("gender").getValue().toString(),
                                        i = sn.getKey().toString();

                                int cup = Integer.parseInt(sn.child("tea").getValue().toString());
                                listFriend.add(new Friend(n, ava, d, cup, g, i));
                                friendAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            String message = error.getMessage();
                            Toast.makeText(getActivity(), "error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String key = snapshot.getKey();
                    for (Friend friend : listFriend) {
                        if (friend.getID().equals(key)) {
                            listFriend.remove(friend);
                            friendAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void displayAllFriends() {
        friendAdapter = new FriendAdapter(getActivity().getApplicationContext(), listFriend);
        friendGrid.setAdapter(friendAdapter);
    }

    public void getData(String email) {
        final String each_user_email;
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //
                if (snapshot.exists()) {
                    boolean flag= false;
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        String each_user_email = postSnapshot.child("email").getValue().toString();
                        if (each_user_email.equals(email)) {
                            flag=true;
                            id = postSnapshot.getKey();
                            Intent intent = new Intent(getActivity(), PersonProfileActivity.class);
                            intent.putExtra("USER_ID", id);
                            sendData(id, intent);
                        }
                    }
                    if (flag==false)
                        Toast.makeText(getActivity(), "This user does not exist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "not exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String message = error.getMessage();
                Toast.makeText(getActivity(), "error: " + message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendData(String id, Intent i) {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("users").child(id);
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uname, udob, uava, ugender;
                    int ucot;
                    uname = snapshot.child("nickname").getValue().toString();
                    udob = snapshot.child("dob").getValue().toString();
                    uava = snapshot.child("avatar").getValue().toString();
                    ugender = snapshot.child("gender").getValue().toString();
                    ucot = Integer.parseInt(snapshot.child("tea").getValue().toString());

                    i.putExtra("VIEW_FRIEND", 0);
                    i.putExtra("FRIEND_AVATAR", uava);
                    i.putExtra("FRIEND_NICKNAME", uname);
                    i.putExtra("FRIEND_DOB", udob);
                    i.putExtra("FRIEND_CUP_OF_TEA", ucot);
                    i.putExtra("FRIEND_GENDER", ugender);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}