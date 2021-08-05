package com.example.serenitea;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    /* Trang Đăng kí
     * Một số hàm chính:
     * Kiểm tra và lưu dữ liệu user vào database --> CreateNewAccount()
     * Chuyển sang trang Profile (SetupActivity) --> SendUserToSetupActivity()
     *
     * */
    private EditText Username, UserPassword, UserConfirmPassword;
    private ImageButton GoogleLoginButton;
    private ImageButton FacebookLoginButton;
    private Button SignUpButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private GoogleSignInClient mGoogleSignInClient;

    private final static int RC_SIGN_IN = 123;

    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get text from input
        Username = (EditText) findViewById(R.id.editTxt_username);
        UserPassword = (EditText) findViewById(R.id.editTxt_password);
        UserConfirmPassword = (EditText) findViewById(R.id.editTxt_confirm);
        SignUpButton = (Button) findViewById(R.id.btn_signup);
        GoogleLoginButton = (ImageButton) findViewById(R.id.btn_google);
        loadingBar = new ProgressDialog(this);

        //event click SignUpButton
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
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

        //Facebook button
        //Initialize for Facebook SDK
        FacebookSdk.sdkInitialize(RegisterActivity.this);
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        FacebookLoginButton = (ImageButton) findViewById(R.id.btn_facebook);
        FacebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        //Toast.makeText(RegisterActivity.this,"success",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(RegisterActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void CreateNewAccount() {//tạo tài khoản lưu trên FireBase

        String username = Username.getText().toString();
        String pwd = UserPassword.getText().toString();
        String confirmPwd = UserConfirmPassword.getText().toString();

        //check nếu user chưa nhập field
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "Please input your Password", Toast.LENGTH_SHORT).show();
        } else if (!isValidPassword(pwd)) {
            Toast.makeText(this, "Password must contain at least 1 lowercase letter, 1 uppercase letter and 1 number digit", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPwd)) {
            Toast.makeText(this, "Please input your Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (!pwd.equals(confirmPwd)) {
            Toast.makeText(this, "Your password do not match with your confirm password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait a moment...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(username, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SendUserToAvatarActivity();

                        Toast.makeText(RegisterActivity.this, "Create account successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }

    private void SendUserToAvatarActivity() {
        //chuyển sang chọn avatar
        Intent avatarIntent = new Intent(RegisterActivity.this, AvatarActivity.class);
        avatarIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(avatarIntent);
        finish();
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private void SendUserToEmotionActivity() {
        Intent emotionIntent = new Intent(RegisterActivity.this, MainActivity.class);
        emotionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(emotionIntent);
        finish();
    }


    private void signInUsingGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
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
                            SendUserToEmotionActivity();
                            Toast.makeText(RegisterActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Sorry auth failed", Toast.LENGTH_LONG).show();
                        }
                        loadingBar.dismiss();
                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        loadingBar.setTitle("Login");
        loadingBar.setMessage("Please wait a moment...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            SendUserToEmotionActivity();
                            Toast.makeText(RegisterActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            String message = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, "Error: " + message,
                                    Toast.LENGTH_SHORT).show();
                        }
                        loadingBar.dismiss();
                    }
                });
    }
}