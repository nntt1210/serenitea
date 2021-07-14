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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
//màn hình login
/* Một số hàm chính:
- check user có login thành công --> AllowingUserToLogin()
- chuyển sang giao diện chính (EmotionActivity) --> SendUserToEmotionActivity()
- chuyển sang trang ResetPasswordActivity --> SendUserToResetPasswordActivity()
*/
    private Button LoginButton;
    private ImageButton GoogleLoginButton;
    private TextView ForgotPwd;
    private EditText Username, Password;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private GoogleSignInClient mGoogleSignInClient;

    private final static int RC_SIGN_IN = 123;

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
        LoginButton = (Button) findViewById(R.id.btn_change_pwd);
        GoogleLoginButton = (ImageButton) findViewById(R.id.btn_google);
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

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Google button
        GoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUsingGoogle();
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

//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user!=null){
//            SendUserToEmotionActivity();
//        }
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
        Intent emotionIntent = new Intent(LoginActivity.this, MainActivity.class);
        emotionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emotionIntent);
        finish();
    }

    private void SendUserToForgotPasswordActivity(){
        Intent emotionIntent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
//        emotionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emotionIntent);
//        finish();
    }

    private void SendUserToSetupActivity(){
        Intent intent = new Intent(LoginActivity.this, SetupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void signInUsingGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this,"Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        loadingBar.setTitle("Login");
        loadingBar.setMessage("Please wait a moment...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //SendUserToEmotionActivity();
                            Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Sorry auth failed", Toast.LENGTH_LONG).show();
                        }
                        loadingBar.dismiss();
                    }
                });
    }
}