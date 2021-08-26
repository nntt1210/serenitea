package com.example.serenitea;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public DrawerLayout drawer;
    private ImageButton closeBtn, friendRequestBtn, notiBtn;
    private TextView txtInfo, txtDob, txtCot;
    private ImageView avatar;
    private String curUser;
    private String name,dob,cot,gender, avatar_id;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, userRef, RootRef, NotiRef, ReceiveRef;
    private boolean hasNewNotification = false;
    private final List<Notification> notificationList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        avatar = headerView.findViewById(R.id.image_avatar);
        txtInfo= headerView.findViewById(R.id.nav_txt_info);
        txtDob= headerView.findViewById(R.id.nav_txt_dob);
        txtCot= headerView.findViewById((R.id.nav_txt_cup_of_tea));

        mAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("users");

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.theme));

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

        friendRequestBtn = (ImageButton)findViewById(R.id.btn_group_add);
        friendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friendRequestIntent = new Intent(MainActivity.this, FriendRequestActivity.class);
                startActivity(friendRequestIntent);
            }
        });

        notiBtn = (ImageButton)findViewById(R.id.btn_notification);
        notiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notiIntent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(notiIntent);
            }
        });

        RootRef = FirebaseDatabase.getInstance().getReference();
        NotiRef = RootRef.child("notification").child(mAuth.getCurrentUser().getUid());

        ReceiveRef = RootRef.child("receiveFriendRequests").child(mAuth.getCurrentUser().getUid());

        FetchNotification();

    }

    public void checkNewFriendRequest() {
        ReceiveRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    friendRequestBtn.setImageResource(R.drawable.ic_group_add_active);
                    return;
                }
                friendRequestBtn.setImageResource(R.drawable.ic_group_add);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNewFriendRequest();
        FetchNotification();
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
                    SendUserToAvatarActivity();
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
            case R.id.nav_forum:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostForumActivity()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_create_quote:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CreateQuoteActivity()).commit();
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
            case R.id.nav_statistics:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StatisticsActivity()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_settings:
                SendUserToSettingsActivity();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                SendUserToLogoutActivity();
                break;
        }

        return true;
    }

    private void FetchNotification() {
        hasNewNotification = false;
        NotiRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String key = snapshot.getKey();
                    Notification notification = snapshot.getValue(Notification.class);
                    notification.setKey(key);
                    notificationList.add(notification);
                    if (notification.getStatus().equals("sent")) {
                        notiBtn.setImageResource(R.drawable.ic_noti_active);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    for (Notification item : notificationList) {
                        if (item.getStatus().equals("sent")) {
                            hasNewNotification = true;
                            return;
                        }
                    }
                }
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

        if (!hasNewNotification) {
            notiBtn.setImageResource(R.drawable.ic_notification);
        }
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

    private void SendUserToSettingsActivity() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
    private void SendUserToLogoutActivity(){
        //chuyển sang trang Logout (trang ban đầu khi vô app)
        Intent logoutIntent =  new Intent(MainActivity.this, LogoutActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
        finish();
    }

    private void SendUserToAvatarActivity(){
        //chuyển sang chọn avatar
        Intent avatarIntent = new Intent(MainActivity.this, AvatarActivity.class);
        avatarIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(avatarIntent);
        finish();
    }
}

