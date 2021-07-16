package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//    private DrawerLayout drawer;
    private TextView txtInfo, txtDob, txtCot;
    private ImageButton closeBtn, backBtn;
    private String curUser;
    private String name,dob,cot,gender;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        NavigationView navigationView = (NavigationView)findViewById(R.id.settings_nav_view);
        View headerView = navigationView.getHeaderView(0);
        txtInfo= headerView.findViewById(R.id.txt_info);
        txtDob= headerView.findViewById(R.id.txt_dob);
        txtCot= headerView.findViewById((R.id.txt_cup_of_tea));

        mAuth = FirebaseAuth.getInstance();
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
//            drawer.openDrawer(GravityCompat.START);
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_settings_container,
//                    new EditProfileActivity()).commit();
//            navigationView.setCheckedItem(R.id.nav_edit);
        }

        backBtn = (ImageButton)navigationView.getHeaderView(0).findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        closeBtn = (ImageButton)navigationView.getHeaderView(0).findViewById(R.id.btn_settings_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
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