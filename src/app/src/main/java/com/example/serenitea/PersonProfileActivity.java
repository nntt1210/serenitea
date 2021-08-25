package com.example.serenitea;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
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
    private Integer view_friend, view_author;
    private String nickname, avatar, dob, gender;
    private Integer cup_of_tea;
    private ImageView image_avatar;
    private TextView txtNickname, txtDob, txtCup;
    private Button btn_send_quote, btn_add_friend, btn_decline;


    private String other_user_id;

    private FirebaseAuth mAuth;
    private String currentUserId, CURRENT_STATE;
    private DatabaseReference RootRef, RequestRef, ReceiveRef, UserRef, FriendsRef;
    private String saveCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        other_user_id = getIntent().getStringExtra("USER_ID");
        RootRef = FirebaseDatabase.getInstance().getReference();
        RequestRef = RootRef.child("friendRequests");
        ReceiveRef = RootRef.child("receiveFriendRequests");
        UserRef = RootRef.child("users");
        FriendsRef = RootRef.child("friends");
//        other_user_id = getIntent().getStringExtra("USER_ID");

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
                    if (CURRENT_STATE.equals("friends")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PersonProfileActivity.this);
                        builder.setMessage("Are you sure you want to remove "+ nickname+" as your friend?").setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UnfriendAnExistingFriend();
                            }
                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btn_add_friend.setEnabled(true);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
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

    private void UnfriendAnExistingFriend() {
        FriendsRef.child(currentUserId).child(other_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FriendsRef.child(other_user_id).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                btn_add_friend.setEnabled(true);
                                CURRENT_STATE = "not_friends";
                                btn_add_friend.setText("Add Friend");

                                btn_send_quote.setVisibility(View.INVISIBLE);
                                btn_send_quote.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });
    }

    private void AcceptFriendRequest() {
        //get date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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

                                                        ReceiveRef.child(currentUserId).child(other_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    btn_add_friend.setEnabled(true);
                                                                    CURRENT_STATE = "friends";
                                                                    btn_add_friend.setText("Unfriend");


                                                                    btn_decline.setVisibility(View.INVISIBLE);
                                                                    btn_decline.setEnabled(false);

                                                                    btn_send_quote.setVisibility(View.VISIBLE);
                                                                    btn_send_quote.setEnabled(true);
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
                                ReceiveRef.child(other_user_id).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            btn_add_friend.setEnabled(true);
                                            CURRENT_STATE = "not_friends";
                                            btn_add_friend.setText("Add Friend");

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

    private void SaveRequestToDatabase() {
        //get date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        saveCurrentDate = currentDate.format(calendar.getTime());

        RequestRef.child(currentUserId).child(other_user_id).child("type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    RequestRef.child(other_user_id).child(currentUserId).child("type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ReceiveRef.child(other_user_id).child(currentUserId).child("date").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            btn_add_friend.setEnabled(true);
                                            CURRENT_STATE = "request_sent";
                                            btn_add_friend.setText("Cancel Request");
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

        view_friend = (Integer)getIntent().getIntExtra("VIEW_FRIEND", 0);
        view_author = (Integer)getIntent().getIntExtra("VIEW_AUTHOR", 0);

        if (view_friend == 1) {
            nickname = getIntent().getStringExtra("FRIEND_NICKNAME");
            avatar = getIntent().getStringExtra("FRIEND_AVATAR");
            dob = getIntent().getStringExtra("FRIEND_DOB");
            gender = getIntent().getStringExtra("FRIEND_GENDER");
            cup_of_tea = (Integer) getIntent().getIntExtra("FRIEND_CUP_OF_TEA", 0);

        }
        else if (view_author == 1) {
            nickname = getIntent().getStringExtra("AUTHOR_NICKNAME");
            avatar = getIntent().getStringExtra("AUTHOR_AVATAR");
            dob = getIntent().getStringExtra("AUTHOR_DOB");
            gender = getIntent().getStringExtra("AUTHOR_GENDER");
            cup_of_tea = (Integer) getIntent().getIntExtra("AUTHOR_CUP_OF_TEA", 0);
        }

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
                sendQuoteIntent.putExtra("OTHER_UID", other_user_id);
                sendQuoteIntent.putExtra("NICKNAME",nickname);
                startActivity(sendQuoteIntent);
            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeclineFriendRequest();
            }
        });

        btn_send_quote.setVisibility(View.INVISIBLE);
        btn_send_quote.setEnabled(false);
        MaintenanceOfButton();
    }

    private void DeclineFriendRequest() {
        RequestRef.child(currentUserId).child(other_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    RequestRef.child(other_user_id).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ReceiveRef.child(currentUserId).child(other_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            btn_add_friend.setEnabled(true);
                                            CURRENT_STATE = "not_friends";
                                            btn_add_friend.setText("Add Friend");

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

    private void MaintenanceOfButton() {
        RequestRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(other_user_id)) {
                    String request_type = snapshot.child(other_user_id).child("type").getValue().toString();

                    if (request_type.equals("sent")) {
                        CURRENT_STATE = "request_sent";
                        btn_add_friend.setText("Cancel Request");
                    } else if (request_type.equals("received")) {
                        CURRENT_STATE = "request_received";
                        btn_add_friend.setText("Accept");

                        btn_decline.setVisibility(View.VISIBLE);
                        btn_decline.setEnabled(true);
                    }
                } else {
                    FriendsRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(other_user_id)) {
                                CURRENT_STATE = "friends";

                                btn_add_friend.setText("Unfriend");
                                btn_send_quote.setVisibility(View.VISIBLE);
                                btn_send_quote.setEnabled(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}