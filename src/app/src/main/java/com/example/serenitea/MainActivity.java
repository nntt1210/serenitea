package com.example.serenitea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
/* Giao diện chính, chứa thanh navigation đến các trang khác
* Một số hàm chính:
* Chuyển hướng trang khi chọn trên navigation --> UserMenuSelector() (sử dụng switch case, tương ứng với button nào thì chuyển giao diện sang trang đó)
* Các hàm SendUserTo...Activity()
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //nếu user==null-->gọi SendUserToLoginActivity()
        SendUserToLogoutActivity();
    }

    private void UserMenuSelector (MenuItem item){
        //chuyển hướng trang khi chọn trên menu (vd: Đăng kí, đăng nhập, profile...)
    }

    private void SendUserToEmotionActivity (){
        Intent emotionIntent = new Intent(MainActivity.this, EmotionActivity.class);
        emotionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity (emotionIntent);
        finish();
    }
    private void SendUserToLogoutActivity (){
        //chuyển sang trang Logout (trang ban đầu khi vô app)
        Intent logoutIntent =  new Intent(MainActivity.this, LogoutActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
        finish();
    }

}