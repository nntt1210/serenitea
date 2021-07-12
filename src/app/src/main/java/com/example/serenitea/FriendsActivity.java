package com.example.serenitea;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

public class FriendsActivity extends Fragment {

    GridView friendGrid;
    ArrayList<Friend> listFriend;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_friends, container, false);

//        Toolbar toolbar = view.findViewById(R.id.friend_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        listFriend = new ArrayList<>();
        listFriend.add(new Friend("Mint", "avatar_11"));
        listFriend.add(new Friend("Ziem", "avatar_3"));
        listFriend.add(new Friend("Dic", "avatar_10"));

        friendGrid = (GridView)view.findViewById(R.id.grid_view_friend); // init GridView
        // Create an object FriendAdapter and set Adapter to GirdView
        FriendAdapter friendAdapter = new FriendAdapter(getActivity().getApplicationContext(), listFriend);
        friendGrid.setAdapter(friendAdapter);
        return view;
    }


}