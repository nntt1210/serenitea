package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public DrawerLayout drawer;
    private ImageButton closeBtn;
    private TextView txtInfo, txtDob, txtCot;
    private String curUser;
    private String name,dob,cot,gender;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        txtInfo= headerView.findViewById(R.id.nav_txt_info);
        txtDob= headerView.findViewById(R.id.nav_txt_dob);
        txtCot= headerView.findViewById((R.id.nav_txt_cup_of_tea));

        mAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("users");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getSupportActionBar().setTitle("");

        drawer = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new EmotionActivity()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        closeBtn = (ImageButton)navigationView.getHeaderView(0).findViewById(R.id.btn_close);
        closeBtn.setOnClickListener(v -> drawer.closeDrawer(GravityCompat.START));

    }

    protected void setProfile()
    {
        curUser=mAuth.getCurrentUser().getUid();
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

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            SendUserToLogoutActivity();
        }
        else{
            CheckUserExistence();
            setProfile();
        }
    }

    private void CheckUserExistence() {
        final String currentUserId  = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(currentUserId)){
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EmotionActivity()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_friends:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FriendsActivity()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavoriteActivity()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_settings:
                SendUserToSettingsActivity();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLogoutActivity();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//        //nếu user==null-->gọi SendUserToLoginActivity()
//        SendUserToLogoutActivity();
//    }

    private void UserMenuSelector (MenuItem item){
        //chuyển hướng trang khi chọn trên menu (vd: Đăng kí, đăng nhập, profile...)
    }

    private void SendUserToEmotionActivity(){
        Intent emotionIntent = new Intent(MainActivity.this, EmotionActivity.class);
        emotionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emotionIntent);
        finish();
    }
    private void SendUserToSettingsActivity() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
    private void SendUserToLogoutActivity(){
        //chuyển sang trang Logout (trang ban đầu khi vô app)
        Intent logoutIntent =  new Intent(MainActivity.this, LogoutActivity.class);
        startActivity(logoutIntent);
    }


    private void SendUserToSetupActivity(){
        //chuyển sang trang Logout (trang ban đầu khi vô app)
        Intent intent =  new Intent(MainActivity.this, SetupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}

