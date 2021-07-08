package com.example.serenitea;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class EmotionActivity extends Fragment {
/* Trang chọn tâm trạng để generate quote
* Một số hàm:
* Chọn tâm trạng, dựa vào tâm trạng đó chọn ra 1 quote trong database (có thể trả về id của quote)
* Chuyển trang sang QuoteActivity --> SendUserToQuoteActivity()
* */

    private int emotion = 0;
    private ImageButton btnDissatisfied;
    private ImageButton btnNeutral;
    private ImageButton btnSatisfied;
    private ImageButton btnHappy;
    private ImageButton btnVerysatisfied;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_emotion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnDissatisfied = (ImageButton) getView().findViewById(R.id.emotion_dissatisfied);
        btnHappy = (ImageButton) getView().findViewById(R.id.emotion_happy);
        btnNeutral = (ImageButton) getView().findViewById(R.id.emotion_neutral);
        btnSatisfied = (ImageButton) getView().findViewById(R.id.emotion_satisfied);
        btnVerysatisfied = (ImageButton) getView().findViewById(R.id.emotion_very_satisfied);

        btnDissatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebtnDissatisfied();
            }
        });
        btnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebtnHappy();
            }
        });
        btnNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebtnNeutral();
            }
        });
        btnSatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebtnSatisfied();
            }
        });
        btnVerysatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebtnVerysatisfied();
            }
        });

    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_emotion);
//
//        btnDissatisfied = (ImageButton)findViewById(R.id.emotion_dissatisfied);
//        btnHappy = (ImageButton)findViewById(R.id.emotion_happy);
//        btnNeutral = (ImageButton)findViewById(R.id.emotion_neutral);
//        btnSatisfied = (ImageButton)findViewById(R.id.emotion_satisfied);
//        btnVerysatisfied = (ImageButton)findViewById(R.id.emotion_very_satisfied);
//
//        btnDissatisfied.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changebtnDissatisfied();
//            }
//        });
//        btnHappy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changebtnHappy();
//            }
//        });
//        btnNeutral.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changebtnNeutral();
//            }
//        });
//        btnSatisfied.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changebtnSatisfied();
//            }
//        });
//        btnVerysatisfied.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changebtnVerysatisfied();
//            }
//        });
//    }

    private void changebtnDissatisfied ()
    {
        emotion = 1;
        SendUserToQuoteActivity();
    }
    private void changebtnHappy ()
    {
        emotion = 2;
        SendUserToQuoteActivity();
    }
    private void changebtnNeutral ()
    {
        emotion = 3;
        SendUserToQuoteActivity();
    }
    private void changebtnSatisfied ()
    {
        emotion = 4;
        SendUserToQuoteActivity();
    }
    private void changebtnVerysatisfied ()
    {
        emotion = 5;
        SendUserToQuoteActivity();
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
    private void SendUserToQuoteActivity (){
        Intent quoteIntent = new Intent(EmotionActivity.this.getActivity(), QuoteActivity.class);
//        quoteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        quoteIntent.putExtra("emotion", emotion);
        startActivity(quoteIntent);
 //       finish();
    }
}