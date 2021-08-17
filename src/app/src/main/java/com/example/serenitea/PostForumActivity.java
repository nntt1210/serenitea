package com.example.serenitea;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class PostForumActivity extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, PostRef, UserRef;
    String currentUserId;
    private RecyclerView postRecyclerView;
    private final List<Post> postList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private PostAdapter postAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_forum, container, false);
        postRecyclerView = (RecyclerView)view.findViewById(R.id.list_post);
        postList.add(new Post("vGInau6zh9cIkW4KLeO4GGl2DWg1", "Tam Nguyen", "avatar_1", "17/08/2021"
        , "Time you enjoy wasting is not wasted time", R.color.text_friend, R.drawable.neutral_1, R.font.open_sans_semibold));
        postList.add(new Post("vGInau6zh9cIkW4KLeO4GGl2DWg1", "Tam Nguyen", "avatar_1", "17/08/2021"
                , "Time you enjoy wasting is not wasted time", R.color.text_friend, R.drawable.neutral_1, R.font.open_sans_semibold));

        DisplayAllPost();
        return view;
    }

    private void DisplayAllPost() {
        postAdapter = new PostAdapter(postList, getActivity().getApplicationContext());

        linearLayoutManager = new LinearLayoutManager(getActivity());
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(linearLayoutManager);
        postRecyclerView.setAdapter(postAdapter);
    }
}