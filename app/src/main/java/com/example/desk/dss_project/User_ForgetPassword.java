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

    public void reset(View view) {
        final EditText emailField = findViewById(R.id.email);
        String email= emailField.getText().toString();
        boolean isEmail = isValidEmail(email);
        if(!isEmail)
            setBackground(emailField, R.drawable.rect_red);
        else{
            setBackground(emailField, R.drawable.rect_black);
            if( isEmail ) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getBaseContext(), getString(R.string.resetMessage), Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                } else {
                                    setBackground(emailField, R.drawable.rect_red);
                                    Toast.makeText(getBaseContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }

    }
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void setBackground(View view, int resource){
        view.setBackgroundResource(resource);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), User_Login.class));
        finish();
    }
}
