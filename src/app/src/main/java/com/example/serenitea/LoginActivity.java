package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

public class LoginActivity extends AppCompatActivity {
//màn hình login
/* Một số hàm chính:
- check user có login thành công --> AllowingUserToLogin()
- chuyển sang giao diện chính (EmotionActivity) --> SendUserToEmotionActivity()
- chuyển sang trang ResetPasswordActivity --> SendUserToResetPasswordActivity()
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
    protected void onStart(){
        super.onStart();

        //nếu user!=null--> gọi SendUserToMainActivity()
    }

    private void AllowingUserToLogin(){
        //nếu Login thành công -> gọi SendUserToMainActivity()
    }

    private void SendUserToEmotionActivity(){
        //chuyển sang trang chủ
    }

}