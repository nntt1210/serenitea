package com.example.serenitea;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsActivity extends Fragment {

    GridView friendGrid;
    ArrayList<Friend> listFriend;
    private EditText searchBar;
    private ImageButton btSearch;
    private String inSearch;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ;
    private DatabaseReference userRef;
    private String curId = mAuth.getCurrentUser().getUid(), curEmail = mAuth.getCurrentUser().getEmail();
    private String name = "", cot = "", dob = "", email = "", avatar = "", gender = "", id = "";
    private ArrayList<Friend> list = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_friends, container, false);

        userRef = FirebaseDatabase.getInstance().getReference();

//        Toolbar toolbar = view.findViewById(R.id.friend_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
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
//        listFriend.add(new Friend("Mint", "avatar_11", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Ziem", "avatar_3", "24/01/2001", 23, "Female"));
//        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Tam", "avatar_4", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Khuyen", "avatar_5", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Huyen", "avatar_6", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Thu", "avatar_7", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Chau", "avatar_8", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));
//        listFriend.add(new Friend("Dic", "avatar_10", "24/01/2001", 23, "Male"));


        friendGrid = (ExpandableHeightGridView) view.findViewById(R.id.grid_view_friend); // init GridView
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

    //
//        public void searchBtn() {
//        inSearch = searchBar.getText().toString();
//        boolean flag = getData();
//        if (flag)
//            Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
//    }
//
//        public boolean getData()
//    {
//        Query q = FirebaseDatabase.getInstance().getReference().child("users");
//        q.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot item : snapshot.getChildren())
//                {
//                    Friend u = item.getValue(Friend.class);
//                    if (u.email.equals(inSearch)){
//                        name=u.nickname;
//                        gender=u.gender;
//                        dob=u.dob;
//                        avatar=u.avatar_id;
//                        cot=Integer.toString(u.cup_of_tea);
//                        id=item.getKey();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {  }
//        });
//        if (name.isEmpty())
//             return false;
//        else
//            return true;
//    }
    public void getData(String email) {
        final String each_user_email;
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        String each_user_email = postSnapshot.child("email").getValue().toString();
                        if (each_user_email.equals(email)) {
                            //get user id
                            Toast.makeText(getActivity(), postSnapshot.getKey(), Toast.LENGTH_LONG).show();
                        }
                    }
                   Toast.makeText(getActivity(),"hello", Toast.LENGTH_LONG).show();
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
}