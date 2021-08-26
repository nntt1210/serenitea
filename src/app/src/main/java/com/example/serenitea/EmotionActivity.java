package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    private int update = 1;
    private String curCup;
    private int cup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_emotion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnSad = getView().findViewById(R.id.emotion_sad);
        btnWorried = getView().findViewById(R.id.emotion_worried);
        btnAngry = getView().findViewById(R.id.emotion_angry);
        btnNeutral = getView().findViewById(R.id.emotion_neutral);
        btnHappy = getView().findViewById(R.id.emotion_happy);

        btnSad.setOnClickListener(v -> changebtnSad());
        btnWorried.setOnClickListener(v -> changebtnWorried());
        btnNeutral.setOnClickListener(v -> changebtnNeutral());
        btnAngry.setOnClickListener(v -> changebtnAngry());
        btnHappy.setOnClickListener(v -> changebtnHappy());
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
        updateCup();
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
    private void updateCup ()
    {
        DatabaseReference cupRef = FirebaseDatabase.getInstance().getReference().child("users/"+curUser);
        cupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                curCup = snapshot.child("tea").getValue().toString();
                cup = Integer.parseInt(curCup);
                cup = cup + 1;
                cupRef.child("tea").setValue(cup);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateEmo ()
    {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String m = String.valueOf(month);
        String d = String.valueOf(day);
        mAuth=FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser().getUid();
        diaryRef = FirebaseDatabase.getInstance().getReference().child("diary/"+curUser+"/"+m+"/"+d);
        diaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if (snapshot.hasChild(String.valueOf(emotion))) {
                        curEmo = snapshot.child(String.valueOf(emotion)).getValue().toString();
                        update = Integer.parseInt(curEmo);
                        update = update + 1;
                    }
                    else {
                        update = 1;
                    }
                    diaryRef.child(String.valueOf(emotion)).setValue(update);
                }
                else
                {
                    for(int i = 0; i<5; i++)
                    {
                        update = 0;
                        diaryRef.child(String.valueOf(i+1)).setValue(update);
                    }
                    update = 1;
                    diaryRef.child(String.valueOf(emotion)).setValue(update);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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