package com.example.serenitea;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    private Button btn_send, btn_add;

    private String other_user_id = "9tQSyAT9ylNvZVtLFXTpCxGJALw2";

    private FirebaseAuth mAuth;
    private String currentUserId, CURRENT_STATE;
    private DatabaseReference RootRef, RequestRef, UserRef;
    private String saveCurrentDate, saveCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();
        RequestRef = RootRef.child("friendRequests");
        UserRef = RootRef.child("users");

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        InitializeFields();

        view_friend = (Integer) getIntent().getIntExtra("VIEW_FRIEND", 0);
        if (view_friend == 0) {
            nickname = getIntent().getStringExtra("FRIEND_NICKNAME");
            avatar = getIntent().getStringExtra("FRIEND_AVATAR");
            dob = getIntent().getStringExtra("FRIEND_DOB");
            gender = getIntent().getStringExtra("FRIEND_GENDER");
            cup_of_tea = (Integer) getIntent().getIntExtra("FRIEND_CUP_OF_TEA", 0);

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
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendQuoteIntent = new Intent(PersonProfileActivity.this, SendQuoteActivity.class);
                    startActivity(sendQuoteIntent);
                }
            });

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SaveRequestToDatabase();
                }
            });
        }

        btn_send.setVisibility(View.INVISIBLE);
        btn_send.setEnabled(false);

        if (!currentUserId.equals(other_user_id)) {

        } else {
            btn_send.setVisibility(View.INVISIBLE);
            btn_send.setEnabled(false);
            btn_add.setVisibility(View.INVISIBLE);
            btn_add.setEnabled(false);
        }

    }

    private void SaveRequestToDatabase() {
        String send_ref = "friendRequests/" + currentUserId;
        String receive_ref = "friendRequests/" + other_user_id;

        //get date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/mm/yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        //get time
        Calendar time = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm aa");
        saveCurrentTime = currentTime.format(time.getTime());

        RequestRef.child(currentUserId).child(other_user_id).child("type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    RequestRef.child(other_user_id).child(currentUserId).child("type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PersonProfileActivity.this, "request has been sent", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
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

    private void InitializeFields() {
        image_avatar = (ImageView) findViewById(R.id.image_avatar);
        txtNickname = (TextView) findViewById(R.id.txt_info);
        txtDob = (TextView) findViewById(R.id.txt_dob);
        txtCup = (TextView) findViewById(R.id.txt_cup_of_tea);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_add = (Button) findViewById(R.id.btn_add);

        CURRENT_STATE = "not_friends";

    }
}