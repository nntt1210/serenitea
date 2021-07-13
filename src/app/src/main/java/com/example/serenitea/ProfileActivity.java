package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends Fragment {
/* Trang Profile của user
- Trang này chỉ để show info, nếu muốn update info thì gọi hàm SendUserToSetupActivity() để chuyển sang trang Setup
* Hàm chính:
- Lấy từ database, show ra màn hình các info user
- Một số hàm SendUserTo...Activity()
* */
    private TextView txtInfo, txtDob, txtCot;
    //private ImageView imgAva;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private String curUser;
    private String name,dob,cot;
    private ArrayList<String> data;

    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.nav_header, container, false);
        passData("TAM NGUYEN");
//        setProfile();
        return view;
    }

    protected ArrayList<String> getData() {
        data = new ArrayList<>();
        data.add("TAM NGUYEN");
        data.add("12/10/2001");
        data.add("15");
//        setProfile();
        return data;
    }

    protected void setProfile()
    {
        mAuth=FirebaseAuth.getInstance();
        curUser=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(curUser);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    name = snapshot.child("nickname").getValue().toString();
                    //String gender=snapshot.child("gender").getValue().toString();
                    dob=snapshot.child("dob").getValue().toString();
                    cot=snapshot.child("tea").getValue().toString();

                    data.add(name);
                    data.add(dob);
                    data.add(cot);
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    public interface OnDataPass {
        public void onDataPass(String data);
    }
}
