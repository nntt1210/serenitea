package com.example.serenitea;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileActivity extends Fragment {
/* Trang Profile của user
- Trang này chỉ để show info, nếu muốn update info thì gọi hàm SendUserToSetupActivity() để chuyển sang trang Setup
* Hàm chính:
- Lấy từ database, show ra màn hình các info user
- Một số hàm SendUserTo...Activity()
* */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }
}