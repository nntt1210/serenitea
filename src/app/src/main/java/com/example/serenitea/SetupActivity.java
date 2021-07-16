package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {
/* Trang Setup dùng để
- Sau khi user đăng kí xong, hiện trang này để user điền thông tin cá nhân.
- Khi cần chỉnh sửa profile.
* Một số hàm:
- Kiểm tra và lưu info user xuống database --> SaveAccountInformation()
- Hàm update (có thể viết riêng, hoặc viết chung với hàm SaveAccountInformation() phía trên)
- Một số hàm SendUserTo...Activity()
* */
    private ImageButton btnChooseAvatar;
    private Integer avatar;
    private EditText NickName, DoB;
    private Spinner Gender;
    private String gender_value;
    private Button SaveInfoSetupButton;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);



        btnChooseAvatar = (ImageButton)findViewById(R.id.btn_choose_avatar);
        avatar = (Integer)getIntent().getIntExtra("AVATAR", R.drawable.avatar_2);
        btnChooseAvatar.setImageResource(avatar);

        //get text from input
        NickName = (EditText) findViewById(R.id.setup_nickname);
        Gender = (Spinner) findViewById(R.id.setup_gender);
        DoB = (EditText) findViewById(R.id.setup_dob);
        SaveInfoSetupButton = (Button) findViewById(R.id.btn_save);
        loadingBar = new ProgressDialog(this);


        //select gender
        Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender_value = Gender.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //event click Save Button
        SaveInfoSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }
        });


    }

    private void SaveAccountSetupInformation() {
        String avatar_id =  getResources().getResourceEntryName(avatar);
        String nickname = NickName.getText().toString();
        String gender = gender_value;
        String dob = DoB.getText().toString();

        if(TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        else if(gender.equals("Gender")){
            Toast.makeText(this,"Please choose your gender", Toast.LENGTH_SHORT).show();
        }
        // TODO: Validate DoB, nếu được thì nên tạo lịch, bấm chọn trong đó
        else if(TextUtils.isEmpty(dob)){
            Toast.makeText(this,"Please enter Date of birth", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait a moment...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("avatar", avatar_id);
            userMap.put("nickname",nickname);
            userMap.put("gender",gender);
            userMap.put("dob",dob);
            userMap.put("tea",5);
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        SendUserToEmotionActivity();
                        Toast.makeText(SetupActivity.this,"Your information is saved successfully",Toast.LENGTH_LONG).show();
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this,"Error: " + message,Toast.LENGTH_LONG).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void SendUserToEmotionActivity(){
        Intent emotionIntent = new Intent(SetupActivity.this, MainActivity.class);
        emotionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emotionIntent);
        finish();
    }

}