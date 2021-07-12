package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonProfileActivity extends AppCompatActivity {
/* Trang Profile của người khác (không phải của bản thân)
Một số hàm:
- Gửi lời mời kết bạn
- Đồng ý/ Từ chối (nếu người đó gửi lời mời)
- Gửi cup of tea (nếu là bạn bè)
* */
    private Integer view_friend;
    private String nickname, avatar, dob, gender;
    private Integer cup_of_tea;
    private ImageView image_avatar;
    private TextView txtNickname, txtDob, txtCup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        image_avatar = (ImageView)findViewById(R.id.image_avatar);
        txtNickname = (TextView)findViewById(R.id.txt_info);
        txtDob = (TextView)findViewById(R.id.txt_dob);
        txtCup = (TextView)findViewById(R.id.txt_cup_of_tea);

        view_friend = (Integer)getIntent().getIntExtra("VIEW_FRIEND", 0);
        if (view_friend == 0) {
            nickname = getIntent().getStringExtra("FRIEND_NICKNAME");
            avatar = getIntent().getStringExtra("FRIEND_AVATAR");
            dob = getIntent().getStringExtra("FRIEND_DOB");
            gender = getIntent().getStringExtra("FRIEND_GENDER");
            cup_of_tea = (Integer)getIntent().getIntExtra("FRIEND_CUP_OF_TEA", 0);

            Resources resources = getApplicationContext().getResources();
            final int resourceId = resources.getIdentifier(avatar, "drawable", getApplicationContext().getPackageName());
            image_avatar.setImageResource(resourceId);
            txtNickname.setText(nickname);
            txtDob.setText(dob);
            txtCup.setText(cup_of_tea.toString());
            switch (gender) {
                case "Male":
                    txtNickname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_men, 0, 0, 0);
                    break;
                case "Female":
                    txtNickname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_women, 0, 0, 0);
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}