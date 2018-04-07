package com.example.desk.dss_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class User_SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    CheckBox showPassword;
    EditText emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);
        if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();
        passwordField = findViewById(R.id.password);
        emailField = findViewById(R.id.email);
        showPassword = findViewById(R.id.showPassword);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    public void newAccount(View view) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        int passwordLen = password.length();
        boolean isEmail = isValidEmail(email);
        if(!isEmail)
            setBackground(emailField, R.drawable.rect_red);
        else
            setBackground(emailField, R.drawable.rect_black);
        if(passwordLen == 0)
            setBackground(passwordField, R.drawable.rect_red);
        else
            setBackground(passwordField, R.drawable.rect_black);
        if(isEmail && passwordLen != 0) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getBaseContext(), getString(R.string.addedSuccessfully) ,Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(getBaseContext(), task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void setBackground(View view, int resource){
        view.setBackgroundResource(resource);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, User_Login.class));
        finish();
    }
}
