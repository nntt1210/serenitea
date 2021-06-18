package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

public class RegisterActivity extends AppCompatActivity {
/* Trang Đăng kí
* Một số hàm chính:
* Kiểm tra và lưu dữ liệu user vào database --> CreateNewAccount()
* Chuyển sang trang Profile (SetupActivity) --> SendUserToSetupActivity()
*
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Back");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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


}