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
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.util.ArrayList;

public class FriendsActivity extends Fragment {

    GridView friendGrid;
    ArrayList<Friend> listFriend;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_friends, container, false);

        listFriend = new ArrayList<>();
        listFriend.add(new Friend("Mint", "avatar_11", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Ziem", "avatar_3", "24/01/2001", 23, "Female"));
        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Tam", "avatar_4", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Khuyen", "avatar_5", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Huyen", "avatar_6", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Thu", "avatar_7", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Chau", "avatar_8", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));




        friendGrid = (ExpandableHeightGridView)view.findViewById(R.id.grid_view_friend); // init GridView
//        friendGrid.setExpanded(true);
        // Create an object FriendAdapter and set Adapter to GirdView
        FriendAdapter friendAdapter = new FriendAdapter(getActivity().getApplicationContext(), listFriend);
        friendGrid.setAdapter(friendAdapter);

        friendGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PersonProfileActivity.class);
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


}