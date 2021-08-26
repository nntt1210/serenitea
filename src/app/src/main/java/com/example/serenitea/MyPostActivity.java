package com.example.serenitea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyPostActivity extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, PostRef;
    String currentUserId;
    private RecyclerView postRecyclerView;
    private final List<Post> postList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private PostAdapter postAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_post, container, false);
        postRecyclerView = (RecyclerView) view.findViewById(R.id.my_post);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        PostRef = FirebaseDatabase.getInstance().getReference().child("forum");

        FetchAllPost();
        DisplayAllPost();

        return view;
    }

    private void FetchAllPost() {
        postList.clear();
        PostRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String key = snapshot.getKey();
                    Post p = snapshot.getValue(Post.class);
                    if (p.getUserId().equals(currentUserId)){
                    p.postId = key;
                    postList.add(p);
                    postAdapter.notifyDataSetChanged();
                }
                }
                Collections.sort(postList);
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

    private void DisplayAllPost() {
        postAdapter = new PostAdapter(postList, getActivity().getApplicationContext(), getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity());
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(linearLayoutManager);
        postRecyclerView.setAdapter(postAdapter);
    }
}