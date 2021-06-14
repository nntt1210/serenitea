package com.example.serenitea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart(){
        super.onStart();

        //nếu user!=null--> gọi SendUserToMainActivity()
    }

    private void AllowingUserToLogin(){
        //nếu Login thành công -> gọi SendUserToMainActivity()
    }
    private void SendUserToRegisterActivity(){
        //chuyển sang trang Đăng kí
    }

    private void SendUserToMainActivity(){
        //chuyển sang trang chủ
    }

}