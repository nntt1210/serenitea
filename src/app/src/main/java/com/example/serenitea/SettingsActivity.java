package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//    private DrawerLayout drawer;
    private TextView txtInfo, txtDob, txtCot;
    private ImageView avatar;
    private ImageButton backBtn;
    private String curUser;
    private String name,dob,cot,gender, avatar_id;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.theme));

        NavigationView navigationView = (NavigationView)findViewById(R.id.settings_nav_view);
        View headerView = navigationView.getHeaderView(0);
        avatar = headerView.findViewById(R.id.image_avatar);
        txtInfo= headerView.findViewById(R.id.txt_info);
        txtDob= headerView.findViewById(R.id.txt_dob);
        txtCot= headerView.findViewById((R.id.txt_cup_of_tea));

        mAuth = FirebaseAuth.getInstance();
        navigationView.setNavigationItemSelectedListener(this);


        backBtn = (ImageButton)navigationView.getHeaderView(0).findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    protected void setProfile()
    {
        curUser= mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(curUser);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    name = snapshot.child("nickname").getValue().toString();
                    gender=snapshot.child("gender").getValue().toString();
                    dob=snapshot.child("dob").getValue().toString();
                    cot=snapshot.child("tea").getValue().toString();
                    avatar_id=snapshot.child("avatar").getValue().toString();
                    if (avatar_id == null) {
                        avatar.setImageResource(R.drawable.avatar_2);
                    }
                    else {
                        int resourceId = getResources().getIdentifier(avatar_id, "drawable", getPackageName());
                        avatar.setImageResource(resourceId);
                    }
                    txtInfo.setText(name);
                    txtDob.setText(dob);
                    txtCot.setText(cot);


                    if  (gender.equals("Female")){
                        Drawable f = getResources().getDrawable(R.drawable.ic_women);
                        txtInfo.setCompoundDrawablesWithIntrinsicBounds(f,null,null,null);
                    }
                    else if (gender.equals("Male")){
                        Drawable m=getResources().getDrawable(R.drawable.ic_men);
                        txtInfo.setCompoundDrawablesWithIntrinsicBounds(m,null,null,null);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setProfile();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_edit:
                Intent editProfileIntent = new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(editProfileIntent);
                break;
            case R.id.nav_change_password:
                Intent changePasswordIntent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(changePasswordIntent);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}