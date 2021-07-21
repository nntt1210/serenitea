package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EmotionActivity extends Fragment {
/* Trang chọn tâm trạng để generate quote
* Một số hàm:
* Chọn tâm trạng, dựa vào tâm trạng đó chọn ra 1 quote trong database (có thể trả về id của quote)
* Chuyển trang sang QuoteActivity --> SendUserToQuoteActivity()
* */

    private int emotion = 0;
    private ImageButton btnSad;
    private ImageButton btnWorried;
    private ImageButton btnAngry;
    private ImageButton btnNeutral;
    private ImageButton btnHappy;
    private DatabaseReference diaryRef;
    private FirebaseAuth mAuth;
    private String curUser;
    private String curEmo;
    private int update;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_emotion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnSad = (ImageButton) getView().findViewById(R.id.emotion_sad);
        btnWorried = (ImageButton) getView().findViewById(R.id.emotion_worried);
        btnAngry = (ImageButton) getView().findViewById(R.id.emotion_angry);
        btnNeutral = (ImageButton) getView().findViewById(R.id.emotion_neutral);
        btnHappy = (ImageButton) getView().findViewById(R.id.emotion_happy);

        btnSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebtnSad();
            }
        });
        btnWorried.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebtnWorried();
            }
        });
        btnNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebtnNeutral();
            }
        });
        btnAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebtnAngry();
            }
        });
        btnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebtnHappy();
            }
        });

    }

    private void changebtnSad ()
    {
        emotion = 1;
        updateEmo();
        SendUserToQuoteActivity();
    }
    private void changebtnHappy ()
    {
        emotion = 2;
        updateEmo();
        SendUserToQuoteActivity();
    }
    private void changebtnNeutral ()
    {
        emotion = 3;
        updateEmo();
        SendUserToQuoteActivity();
    }
    private void changebtnAngry ()
    {
        emotion = 4;
        updateEmo();
        SendUserToQuoteActivity();
    }
    private void changebtnWorried ()
    {
        emotion = 5;
        updateEmo();
        SendUserToQuoteActivity();
    }

    private void updateEmo ()
    {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        mAuth=FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser().getUid();
        diaryRef = FirebaseDatabase.getInstance().getReference().child("diary").child(curUser);
        diaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(String.valueOf(month)).exists())
                {
                    if (snapshot.child(String.valueOf(month)).child(String.valueOf(day)).exists()) {
                        curEmo = snapshot.child(String.valueOf(month)).child(String.valueOf(day)).getValue().toString();
                        update = Integer.parseInt(curEmo);
                        switch (emotion) {
                            case 1:
                                update = update - 2;
                                break;
                            case 2:
                                update = update + 2;
                                break;
                            case 3:
                                break;
                            case 4:
                                update = update - 1;
                                break;
                            case 5:
                                update = update - 1;
                                break;
                        }
                    }
                }
                else {
                    switch (emotion) {
                        case 1:
                            update = -2;
                            break;
                        case 2:
                            update = 2;
                            break;
                        case 3:
                            update = 0;
                            break;
                        case 4:
                            update = -1;
                            break;
                        case 5:
                            update = -1;
                            break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        diaryRef = FirebaseDatabase.getInstance().getReference().child("diary").child(curUser).child(String.valueOf(month)).child(String.valueOf(day));
        diaryRef.setValue(update);
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