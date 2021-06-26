package com.example.serenitea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EmotionActivity extends AppCompatActivity {
/* Trang chọn tâm trạng để generate quote
* Một số hàm:
* Chọn tâm trạng, dựa vào tâm trạng đó chọn ra 1 quote trong database (có thể trả về id của quote)
* Chuyển trang sang QuoteActivity --> SendUserToQuoteActivity()
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}