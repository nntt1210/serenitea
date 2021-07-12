package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileActivity extends Fragment {
/* Trang Profile của user
- Trang này chỉ để show info, nếu muốn update info thì gọi hàm SendUserToSetupActivity() để chuyển sang trang Setup
* Hàm chính:
- Lấy từ database, show ra màn hình các info user
- Một số hàm SendUserTo...Activity()
* */
    private TextView txtInfo, txtDob, txtCot;
    //private ImageView imgAva;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String curUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.nav_header, container, false);   cái này của Tam này
       View view=inflater.inflate(R.layout.activity_profile, container, false);
       txtInfo= view.findViewById(R.id.txt_info);
       txtDob= view.findViewById(R.id.txt_dob);
       txtCot= view.findViewById((R.id.txt_cup_of_tea));
       //imgAva=view.findViewById(R.id.image_avatar);

       mDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot snapshot) {
               if (snapshot.exists())
               {
                   String name = snapshot.child("nickname").getValue().toString();
                   String gender=snapshot.child("gender").getValue().toString();
                   String dob=snapshot.child("dob").getValue().toString();
                   String cot=snapshot.child("tea").getValue().toString();


                   txtInfo.setText(name);
                   txtDob.setText(dob);
                   txtCot.setText(cot);// cups of tea
               }
           }

           @Override
           public void onCancelled(DatabaseError error) {

           }
       });
        return view;
    }





}