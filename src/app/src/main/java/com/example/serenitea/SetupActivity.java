package com.example.serenitea;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
    final Calendar myCalendar = Calendar.getInstance();
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    String currentUserId;

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DoB.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.theme));

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);


        btnChooseAvatar = (ImageButton) findViewById(R.id.btn_choose_avatar);
        avatar = (Integer) getIntent().getIntExtra("AVATAR", R.drawable.avatar_2);
        btnChooseAvatar.setImageResource(avatar);

        //get text from input
        NickName = (EditText) findViewById(R.id.setup_nickname);
        Gender = (Spinner) findViewById(R.id.setup_gender);
        DoB = (EditText) findViewById(R.id.setup_dob);
        SaveInfoSetupButton = (Button) findViewById(R.id.btn_save);
        loadingBar = new ProgressDialog(this);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        DoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SetupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
        String avatar_id = getResources().getResourceEntryName(avatar);
        String nickname = NickName.getText().toString();
        String gender = gender_value;
        String dob = DoB.getText().toString();
        boolean isValidateDate = true;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            if (format.parse(dob).after(date)) {
                isValidateDate = false;
            }

        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String email = mAuth.getCurrentUser().getEmail().toString();
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (gender.equals("Gender")) {
            Toast.makeText(this, "Please choose your gender", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(dob)) {
            Toast.makeText(this, "Please enter Date of birth", Toast.LENGTH_SHORT).show();
        } else if (!isValidateDate) {
            Toast.makeText(this, "Your DoB is not valid", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait a moment...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("avatar", avatar_id);
            userMap.put("nickname", nickname);
            userMap.put("gender", gender);
            userMap.put("dob", dob);
            userMap.put("email", email);
            userMap.put("tea", 0);
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        SendUserToEmotionActivity();
                        Toast.makeText(SetupActivity.this, "Your information is saved successfully", Toast.LENGTH_LONG).show();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
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

    private void SendUserToEmotionActivity() {
        Intent emotionIntent = new Intent(SetupActivity.this, MainActivity.class);
        emotionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emotionIntent);
        finish();
    }

}