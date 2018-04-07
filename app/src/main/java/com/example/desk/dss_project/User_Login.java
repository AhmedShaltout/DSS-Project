package com.example.desk.dss_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class User_Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, Task_ToDoConnector.class));
            onBackPressed();
        }
    }


    public void login(View view) {
        final EditText emailField = findViewById(R.id.email), passwordField = findViewById(R.id.password);
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
        if(isEmail && passwordLen != 0){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getBaseContext(), Task_ToDoConnector.class));
                                onBackPressed();
                            } else {
                                setBackground(emailField, R.drawable.rect_red);
                                setBackground(passwordField, R.drawable.rect_red);
                            }
                        }
                    });
        }
    }
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void setBackground(View view, int resource){
        view.setBackgroundResource(resource);
    }

    public void newAccount(View view) {
        startActivity(new Intent(this, User_SignUp.class));
        onBackPressed();
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(this, User_ForgetPassword.class));
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
