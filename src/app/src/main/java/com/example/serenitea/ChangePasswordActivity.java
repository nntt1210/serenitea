package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {
    private Button UpdatePasswordButton;
    private EditText NewPassword;
    private EditText ConfirmNewPassword;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //get input field
        //TODO: Bên giao diện, với mỗi field để kiểu Text để khi enter nó qua field khác
        //TODO: Khi chọn 1 field thì có giao diện field màu tím, có giao diện field màu hồng
        UpdatePasswordButton = (Button)findViewById(R.id.btn_change_pwd);
        NewPassword = (EditText)findViewById(R.id.reset_password);
        ConfirmNewPassword = (EditText)findViewById(R.id.reset_confirm_password);
        loadingBar = new ProgressDialog(this);

        //event click Update Button
        UpdatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordForUser();
            }
        });
    }
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_change_password, container, false);
//    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//
//        //get input field
//        //TODO: Bên giao diện, với mỗi field để kiểu Text để khi enter nó qua field khác
//        //TODO: Khi chọn 1 field thì có giao diện field màu tím, có giao diện field màu hồng
//        UpdatePasswordButton = (Button) getView().findViewById(R.id.btn_change_pwd);
//        NewPassword = (EditText) getView().findViewById(R.id.reset_password);
//        ConfirmNewPassword = (EditText) getView().findViewById(R.id.reset_confirm_password);
//        loadingBar = new ProgressDialog(getActivity());
//
//        //event click Update Button
//        UpdatePasswordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChangePasswordForUser();
//            }
//        });
//
//    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ChangePasswordForUser() {
        String pwd = NewPassword.getText().toString();
        String confirmPwd = ConfirmNewPassword.getText().toString();

        //check nếu user chưa nhập field
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(ChangePasswordActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPwd)) {
            Toast.makeText(ChangePasswordActivity.this, "Please enter your Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (!isValidPassword(pwd)) {
            Toast.makeText(ChangePasswordActivity.this, "Password must contain at least 1 lowercase letter, 1 uppercase letter and 1 number digit", Toast.LENGTH_SHORT).show();
        } else if (!pwd.equals(confirmPwd)) {
            Toast.makeText(ChangePasswordActivity.this, "Your password do not match with your confirm password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Updating your password");
            loadingBar.setMessage("Please wait a moment...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            currentUser.updatePassword(pwd).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    SendUserToEmotionActivity();
                    Toast.makeText(ChangePasswordActivity.this, "Change password successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.getMessage();
                    Toast.makeText(ChangePasswordActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            });
            loadingBar.dismiss();
        }

    }

    private void SendUserToEmotionActivity() {
        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity (intent);
    }

    private boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}