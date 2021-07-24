package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class EditProfileActivity extends AppCompatActivity {

    private ImageButton btnChooseAvatar;
    private Button btnSave;
    private EditText DoB;
    private EditText txtNickName;
    private Spinner spGender;
    private Integer avatar;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference userRef;
    private String name,dob,cot,gender,ava_id;
    private int db_day, db_month, db_year;
    private String curUser;
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        DoB = (EditText) findViewById(R.id.setup_dob);
        txtNickName= (EditText) findViewById(R.id.setup_nickname);
        spGender= (Spinner) findViewById(R.id.spinner);
        btnSave= (Button) findViewById(R.id.btn_save);

        setDefault();

        avatar = (Integer)getIntent().getIntExtra("EDIT_AVATAR", 0);
        btnChooseAvatar = (ImageButton)findViewById(R.id.btn_choose_avatar);
        if (avatar != 0) {
            btnChooseAvatar.setImageResource(avatar);
        }
        btnChooseAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, AvatarActivity.class);
                intent.putExtra("EDIT_AVATAR", 1);
                startActivity(intent);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EditProfileActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setDefault() {
        curUser = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(curUser);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name = snapshot.child("nickname").getValue().toString();
                    gender = snapshot.child("gender").getValue().toString();
                    dob = snapshot.child("dob").getValue().toString();
                    cot = snapshot.child("tea").getValue().toString();
                    ava_id=snapshot.child("avatar").getValue().toString();
                    if (avatar == 0) {
                        int resourceId = getResources().getIdentifier(ava_id, "drawable", getApplicationContext().getPackageName());
                        btnChooseAvatar.setImageResource(resourceId);
                    }
                    txtNickName.setText(name);
                    DoB.setText(dob);
                    getDMY(dob);
                    if (gender.equals("Female")){
                        spGender.setSelection(2);
                    }
                    else if (gender.equals("Male")){
                        spGender.setSelection(1);
                    }
                    DoB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //final Calendar cldr = Calendar.getInstance();
                            int day = db_day ;
                            int month = db_month;
                            int year = db_year;
                            month-=1;
                            // date picker dialog
                            picker = new DatePickerDialog(EditProfileActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            DoB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                        }
                                    }, year, month, day);
                            picker.show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError error) { }

        });

    }
    public void getDMY(String dob)
    {
        StringTokenizer str= new StringTokenizer(dob,"/");
        db_day=Integer.parseInt(str.nextToken());
        db_month=Integer.parseInt(str.nextToken());
        db_year=Integer.parseInt(str.nextToken());
    }
    public void Save()
    {
        if (isNameChanged()||isDoBChanged()||isGenderChanged()||isAvaChanged())
            {
                Toast.makeText(EditProfileActivity.this,"Data has been changed",Toast.LENGTH_SHORT).show();
                SendUserToSettingActivity();
                finish();
            }
        else
            Toast.makeText(EditProfileActivity.this,"Data is the same and can not be changed",Toast.LENGTH_SHORT).show();
    }
    public boolean isNameChanged(){
        if ((!name.equals(txtNickName.getText().toString())) &&(!txtNickName.getText().toString().isEmpty()) )
        {
            userRef.child("nickname").setValue(txtNickName.getText().toString());
            return true;
        }
        else
            return false;
    }
    public boolean isDoBChanged(){
        if ((!dob.equals(DoB.getText().toString())) &&(!DoB.getText().toString().isEmpty()) )
        {
            userRef.child("dob").setValue(DoB.getText().toString());
            return true;
        }
        else
            return false;
    }
    public boolean isGenderChanged(){
        if ((!gender.equals(spGender.getSelectedItem().toString())) &&(!spGender.getSelectedItem().toString().equals("Gender")))
        {
            userRef.child("gender").setValue(spGender.getSelectedItem().toString());
            return true;
        }
        else
            return false;
    }
    public boolean isAvaChanged(){
        String avatar_id =  getResources().getResourceEntryName(avatar);
        if ((!ava_id.equals(avatar_id)))
        {
            userRef.child("avatar").setValue(avatar_id);
            return true;
        }
        else
            return false;
    }
    private void SendUserToSettingActivity() {
        Intent intent = new Intent(EditProfileActivity.this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}