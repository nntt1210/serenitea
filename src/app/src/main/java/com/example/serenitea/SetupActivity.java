package com.example.serenitea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SetupActivity extends AppCompatActivity {
/* Trang Setup dùng để
- Sau khi user đăng kí xong, hiện trang này để user điền thông tin cá nhân.
- Khi cần chỉnh sửa profile.
* Một số hàm:
- Kiểm tra và lưu info user xuống database --> SaveAccountInformation()
- Hàm update (có thể viết riêng, hoặc viết chung với hàm SaveAccountInformation() phía trên)
- Một số hàm SendUserTo...Activity()
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    private void SaveAccountInformation(){
        //lưu thông tin user vào database
    }

    private void ValidateAccountInfo(){
        //check validate data trước khi update
    }

}