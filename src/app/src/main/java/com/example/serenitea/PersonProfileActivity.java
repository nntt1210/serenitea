package com.example.serenitea;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class PersonProfileActivity extends AppCompatActivity {
/* Trang Profile của người khác (không phải của bản thân)
Một số hàm:
- Gửi lời mời kết bạn
- Đồng ý/ Từ chối (nếu người đó gửi lời mời)
- Gửi cup of tea (nếu là bạn bè)
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}