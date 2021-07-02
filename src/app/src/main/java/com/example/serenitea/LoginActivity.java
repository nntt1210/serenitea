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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
//màn hình login
/* Một số hàm chính:
- check user có login thành công --> AllowingUserToLogin()
- chuyển sang giao diện chính (EmotionActivity) --> SendUserToEmotionActivity()
- chuyển sang trang ResetPasswordActivity --> SendUserToResetPasswordActivity()
*/
    private Button LoginButton;
    private TextView ForgotPwd;
    private EditText Username, Password;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Back");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get input field
        Username = (EditText) findViewById(R.id.login_username);
        Password = (EditText) findViewById(R.id.login_password);
        ForgotPwd = (TextView) findViewById(R.id.txt_forgotPass);
        LoginButton = (Button) findViewById(R.id.btn_login);
        loadingBar = new ProgressDialog(this);

        //event click LoginButton
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowingUserToLogin();
            }
        });

        ForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToForgotPasswordActivity();
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
    protected void onStart(){
        super.onStart();

        //nếu user!=null--> gọi SendUserToMainActivity()
    }

    private void AllowingUserToLogin(){//nếu Login thành công -> gọi SendUserToMainActivity()
        String username = Username.getText().toString();
        String pwd = Password.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please input your Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "Please input your Password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Please wait a moment...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(username, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        SendUserToEmotionActivity();
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }

    private void SendUserToEmotionActivity(){
        Intent emotionIntent = new Intent(LoginActivity.this,EmotionActivity.class);
        emotionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emotionIntent);
        finish();
    }

    private void SendUserToForgotPasswordActivity(){
        Intent emotionIntent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        emotionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emotionIntent);
        finish();
    }

}