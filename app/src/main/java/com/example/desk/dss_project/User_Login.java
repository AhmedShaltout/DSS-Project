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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        if(Utils.getAuth().getCurrentUser() != null) {
            startActivity(new Intent(this, Task_ToDoConnector.class));
            onBackPressed();
        }
    }

    /**
     * is used to login for the entered email and password
     * the return value is the user logged in if the email and password are found and matched
     * or the fields color is changed.
     * */
    public void login(View view) {
        final EditText emailField = findViewById(R.id.email), passwordField = findViewById(R.id.password);
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        int passwordLen = password.length();
        boolean isEmail = Utils.isValidEmail(email);
        if(!isEmail)
            Utils.setBackground(emailField, R.drawable.rect_red);
        else
            Utils.setBackground(emailField, R.drawable.rect_black);
        if(passwordLen == 0)
            Utils.setBackground(passwordField, R.drawable.rect_red);
        else
            Utils.setBackground(passwordField, R.drawable.rect_black);
        if(isEmail && passwordLen != 0){
            Utils.getAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getBaseContext(), Task_ToDoConnector.class));
                                onBackPressed();
                            } else {
                                Utils.setBackground(emailField, R.drawable.rect_red);
                                Utils.setBackground(passwordField, R.drawable.rect_red);
                            }
                        }
                    });
        }
    }

    /**
     * is called when the user clicks on create new account button for showing sign up activity
     * the result is the sign up activity is shown and the login activity terminated
     * */
    public void newAccount(View view) {
        startActivity(new Intent(this, User_SignUp.class));
        onBackPressed();
    }
    /**
    * is called when the user clicks on forget password text to show forget password activity
     * the result is the forget password activity is shown and the login activity terminated
     * */
    public void forgotPassword(View view) {
        startActivity(new Intent(this, User_ForgetPassword.class));
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
