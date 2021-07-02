package com.example.serenitea;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmotionActivity extends Fragment {
/* Trang chọn tâm trạng để generate quote
* Một số hàm:
* Chọn tâm trạng, dựa vào tâm trạng đó chọn ra 1 quote trong database (có thể trả về id của quote)
* Chuyển trang sang QuoteActivity --> SendUserToQuoteActivity()
* */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_emotion, container, false);
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
}