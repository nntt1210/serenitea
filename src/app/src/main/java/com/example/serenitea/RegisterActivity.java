package com.example.serenitea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //kiểm tra nếu hiện tại user đã login rồi --> gọi SendUserToMainActivity()
    }

    private void CreateNewAccount(){
        //tạo tài khoản lưu trên FireBase
    }

    private void SendUserToSetupActivity(){
        //chuyển sang trang Profile

    }

    private void SendUserToMainActivity(){
        //chuyển sang trang chủ
    }

}