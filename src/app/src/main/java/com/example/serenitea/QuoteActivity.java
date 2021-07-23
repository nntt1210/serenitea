package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.Math;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuoteActivity extends AppCompatActivity {
/* Trang in quote ra màn hình
* Một số hàm:
* - Dựa vào id trả về trong EmotionActivity, vào database generate quote đó ra màn hình
* - Tạo phần like bài viết, khi like thì lưu quote vào database --> setLikeButtonQuote()
* - Các hàm SendUserTo...Activity()
* */
    private DatabaseReference mQuote;
    private TextView QuoteView;
    private String QuoteID;
    private String Quote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        QuoteView = (TextView)findViewById(R.id.text_quote);
        GetEmotion();
        GenerateQuote();
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
    //id dissatisfied: 21 - 30, happy: 1 - 10; nervous: 41 - 60; angry: 61 - 71; neutral: 81 - 90

    private void GetEmotion ()
    {
        int emo = getIntent().getIntExtra("emotion", 0);
        int qid;
        switch (emo)
        {
            case 1: // dissatisfied - sad
                qid = (int)(Math.random() * ((30 - 21) + 1)) + 21;
                QuoteID = "00" + String.valueOf(qid);
                break;
            case 2: // happy
                qid = (int)(Math.random() * ((10 - 1) + 1)) + 1;
                if (qid == 10)
                    QuoteID = "00" + String.valueOf(qid);
                else
                    QuoteID = "000" + String.valueOf(qid);
                break;
            case 3: // neutral
                qid = (int)(Math.random() * ((90 - 81) + 1)) + 81;
                QuoteID = "00" + String.valueOf(qid);
                break;
            case 4: // angry
                qid = (int)(Math.random() * ((71 - 61) + 1)) + 61;
                QuoteID = "00" + String.valueOf(qid);
                break;
            case 5: // nervous
                qid = (int)(Math.random() * ((60 - 41) + 1)) + 41;
                QuoteID = "00" + String.valueOf(qid);
                break;
        }
    }
    private void GenerateQuote ()
    {
        mQuote = FirebaseDatabase.getInstance().getReference().child("quotes").child(QuoteID);
        mQuote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                Quote = datasnapshot.child("content").getValue().toString();
                QuoteView.setText(Quote);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void setLikeButtonQuote(){
        
    }
}