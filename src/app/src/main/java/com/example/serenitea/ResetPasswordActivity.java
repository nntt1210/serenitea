package com.example.serenitea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ResetPasswordActivity extends AppCompatActivity {
/* Trong trang Login có phần Forget Password --> nhấn vào sẽ chuyển sang trang này
* Một số hàm:
* - Chủ yếu là các hàm có sẵn của Firebase để gửi password vào mail user.
* */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }
}