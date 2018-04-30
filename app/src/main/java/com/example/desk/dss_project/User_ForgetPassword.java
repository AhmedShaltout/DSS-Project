package com.example.desk.dss_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class User_ForgetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_forget_password);
    }

    /*
    * this function is invoked to see if the email is a user or not in our system and sends a reset
    * email to his email.
    * the parameter is the view clicked
    * the result is the email field changes to read if it is empty or not a user email
    * or it will send a reset message to the email.
    */
    public void reset(View view) {
        final EditText emailField = findViewById(R.id.email);
        String email= emailField.getText().toString();
        boolean isEmail = Utils.isValidEmail(email);
        if(!isEmail)
            Utils.setBackground(emailField, R.drawable.rect_red);
        else{
            Utils.setBackground(emailField, R.drawable.rect_black);
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getBaseContext(), getString(R.string.resetMessage), Toast.LENGTH_LONG).show();
                                onBackPressed();
                            } else {
                                Utils.setBackground(emailField, R.drawable.rect_red);
                                Toast.makeText(getBaseContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                        });
        }

    }
    /*
    * it returns to the main activity (Login activity)
    * and terminates the reset activity
    * */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), User_Login.class));
        finish();
    }
}
