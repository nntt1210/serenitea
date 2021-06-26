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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
/* Trang Đăng kí
* Một số hàm chính:
* Kiểm tra và lưu dữ liệu user vào database --> CreateNewAccount()
* Chuyển sang trang Profile (SetupActivity) --> SendUserToSetupActivity()
*
* */
    private EditText Username, UserPassword, UserConfirmPassword;
    private Button SignUpButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Back");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get text from input
        Username = (EditText) findViewById(R.id.editTxt_username);
        UserPassword = (EditText) findViewById(R.id.editTxt_password);
        UserConfirmPassword = (EditText) findViewById(R.id.editTxt_confirm);
        SignUpButton = (Button) findViewById(R.id.btn_signup);
        loadingBar = new ProgressDialog(this);

        //event click SignUpButton
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //kiểm tra nếu hiện tại user đã login rồi --> gọi SendUserToMainActivity()
    }

    private void CreateNewAccount(){//tạo tài khoản lưu trên FireBase

        String username = Username.getText().toString();
        String pwd = UserPassword.getText().toString();
        String confirmPwd = UserConfirmPassword.getText().toString();

        //check nếu user chưa nhập field
        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please input your Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "Please input your Password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmPwd)){
            Toast.makeText(this, "Please input your Confirm Password", Toast.LENGTH_SHORT).show();
        }
        else if (!pwd.equals(confirmPwd)){
            Toast.makeText(this, "Your password do not match with your confirm password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait a moment...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(username, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        SendUserToSetupActivity();

                        Toast.makeText(RegisterActivity.this, "Create account successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }


    }

    private void SendUserToSetupActivity(){
        //chuyển sang trang Profile
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }


}