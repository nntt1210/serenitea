package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new MessageFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_home);
//        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EmotionActivity()).commit();
                break;
            case R.id.nav_friends:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FriendsActivity()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileActivity()).commit();
                break;
//            case R.id.nav_favorites:
//                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_statistics:
//                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
//                break;
        }
        drawer.closeDrawer(GravityCompat.START);
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
}



//public class MainActivity extends BaseDrawerActivity {
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // to check current activity in the navigation drawer
//        navigationView.getMenu().getItem(0).setChecked(true);
//    }
//
////    @Override
////    protected void onStart() {
////        super.onStart();
////        //nếu user==null-->gọi SendUserToLoginActivity()
////        SendUserToLogoutActivity();
////    }
//
//    private void UserMenuSelector (MenuItem item){
//        //chuyển hướng trang khi chọn trên menu (vd: Đăng kí, đăng nhập, profile...)
//    }
//
//    private void SendUserToLogoutActivity (){
//        //chuyển sang trang Logout (trang ban đầu khi vô app)
//        Intent logoutIntent =  new Intent(MainActivity.this, LogoutActivity.class);
//        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(logoutIntent);
//        finish();
//    }
//}
