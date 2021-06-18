package com.example.serenitea;

import androidx.appcompat.app.AppCompatActivity;

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

    }

    private void UserMenuSelector (MenuItem item){
        //chuyển hướng trang khi chọn trên menu (vd: Đăng kí, đăng nhập, profile...)
    }

    private void SendUserToLoginActivity (){
        //chuyển sang trang Login
    }

}