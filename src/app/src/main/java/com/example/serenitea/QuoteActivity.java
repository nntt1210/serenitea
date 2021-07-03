package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    private String Emotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        QuoteView = (TextView)findViewById(R.id.textView);
        GetEmotion();
        GenerateQuote();
    }
    //id dissatisfied: 21 - 40, happy: 1 - 10; neutral: 41 - 50; satisfied: 61 - 70; very satisfied: 81 - 90

    private void GetEmotion ()
    {
        int emo = getIntent().getIntExtra("emotion", 0);
        int qid;
        switch (emo)
        {
            case 1: // dissatisfied - sad
                qid = (int)(Math.random() * ((30 - 21) + 1)) + 21;
                QuoteID = "00" + String.valueOf(qid);
                Emotion = "dissatisfied";
                break;
            case 2: // happy
                qid = (int)(Math.random() * ((10 - 1) + 1)) + 1;
                if (qid == 10)
                    QuoteID = "00" + String.valueOf(qid);
                else
                    QuoteID = "000" + String.valueOf(qid);
                Emotion = "happy";
                break;
            case 3: // neutral
                qid = (int)(Math.random() * ((50 - 41) + 1)) + 41;
                QuoteID = "00" + String.valueOf(qid);
                Emotion = "neutral";
                break;
            case 4: // angry
                qid = (int)(Math.random() * ((70 - 61) + 1)) + 61;
                QuoteID = "00" + String.valueOf(qid);
                Emotion = "angry";
                break;
            case 5: // nervous
                qid = (int)(Math.random() * ((90 - 81) + 1)) + 81;
                QuoteID = "00" + String.valueOf(qid);
                Emotion = "nervous";
                break;
        }
    }
    private void GenerateQuote ()
    {
        mQuote = FirebaseDatabase.getInstance().getReference().child("quotes").child(Emotion).child(QuoteID);
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