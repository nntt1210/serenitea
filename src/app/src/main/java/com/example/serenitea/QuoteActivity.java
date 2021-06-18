package com.example.serenitea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class QuoteActivity extends AppCompatActivity {
/* Trang in quote ra màn hình
* Một số hàm:
* - Dựa vào id trả về trong EmotionActivity, vào database generate quote đó ra màn hình
* - Tạo phần like bài viết, khi like thì lưu quote vào database --> setLikeButtonQuote()
* - Các hàm SendUserTo...Activity()
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
    }

    public void setLikeButtonQuote(){
        
    }
}