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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private Button btn_send_quote, btn_add_friend, btn_decline;

    private String other_user_id = "9tQSyAT9ylNvZVtLFXTpCxGJALw2";

    private FirebaseAuth mAuth;
    private String currentUserId, CURRENT_STATE;
    private DatabaseReference RootRef, RequestRef, UserRef, FriendsRef;
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
        FriendsRef = RootRef.child("friends");

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        InitializeFields();
        UpdateInfo();
        if (!currentUserId.equals(other_user_id)) {
            btn_add_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_add_friend.setEnabled(false);
                    if (CURRENT_STATE.equals("not_friends")) {
                        SaveRequestToDatabase();
                    }
                    if (CURRENT_STATE.equals("request_sent")) {
                        CancelFriendRequest();
                    }
                    if (CURRENT_STATE.equals("request_received")) {
                        AcceptFriendRequest();
                    }

                }
            });
        } else {
            btn_decline.setVisibility(View.INVISIBLE);
            btn_decline.setEnabled(false);
            btn_add_friend.setVisibility(View.INVISIBLE);
            btn_add_friend.setEnabled(false);
        }

    }

    private void AcceptFriendRequest() {
        //get date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/mm/yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        FriendsRef.child(currentUserId).child(other_user_id).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //add friend in DB
                    FriendsRef.child(other_user_id).child(currentUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //remove request in DB
                                RequestRef.child(currentUserId).child(other_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            RequestRef.child(other_user_id).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        btn_add_friend.setEnabled(true);
                                                        CURRENT_STATE = "friends";
                                                        btn_add_friend.setText("Unfriend");

                                                        btn_decline.setVisibility(View.INVISIBLE);
                                                        btn_decline.setEnabled(false);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    private void CancelFriendRequest() {
        RequestRef.child(currentUserId).child(other_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    RequestRef.child(other_user_id).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                Toast.makeText(PersonProfileActivity.this, "request has been sent", Toast.LENGTH_LONG).show();
                                btn_add_friend.setEnabled(true);
                                CURRENT_STATE = "not_friends";
                                btn_add_friend.setText("Add Friend");
                            }
                        }
                    });
                }
            }
        });
    }

    private void SaveRequestToDatabase() {
        RequestRef.child(currentUserId).child(other_user_id).child("type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    RequestRef.child(other_user_id).child(currentUserId).child("type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                Toast.makeText(PersonProfileActivity.this, "request has been sent", Toast.LENGTH_LONG).show();
                                btn_add_friend.setEnabled(true);
                                CURRENT_STATE = "request_sent";
                                btn_add_friend.setText("Cancel Friend Request");
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
        btn_send_quote = (Button) findViewById(R.id.btn_send);
        btn_add_friend = (Button) findViewById(R.id.btn_add);
        btn_decline = (Button) findViewById(R.id.btn_decline);

        CURRENT_STATE = "not_friends";

        btn_send_quote.setVisibility(View.INVISIBLE);
        btn_send_quote.setEnabled(false);
        btn_decline.setVisibility(View.INVISIBLE);
        btn_decline.setEnabled(false);
    }

    private void UpdateInfo() {

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
            btn_send_quote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendQuoteIntent = new Intent(PersonProfileActivity.this, SendQuoteActivity.class);
                    startActivity(sendQuoteIntent);
                }
            });

        }

        btn_send_quote.setVisibility(View.INVISIBLE);
        btn_send_quote.setEnabled(false);
        MaintenanceOfButton();
    }

    private void MaintenanceOfButton() {
        RequestRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(other_user_id)) {
                    String request_type = snapshot.child(other_user_id).child("type").getValue().toString();

                    if (request_type.equals("sent")) {
                        CURRENT_STATE = "request_sent";
                        btn_add_friend.setText("Cancel Friend Request");
                    } else if (request_type.equals("received")) {
                        CURRENT_STATE = "request_received";
                        btn_add_friend.setText("Accept Friend Request");

                        btn_decline.setVisibility(View.VISIBLE);
                        btn_decline.setEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}