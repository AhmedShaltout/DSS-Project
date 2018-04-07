package com.example.desk.dss_project;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Task_Details extends AppCompatActivity {


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details);
        if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = Utils.getDatabase();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        database.getReference(currentUser.getUid()+"/"+ Task_ToDo.editThis).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    Task task = dataSnapshot.getValue(Task.class);
                    ((TextView) findViewById(R.id.title)).setText(task.getTitle());
                    ((TextView) findViewById(R.id.body)).setText(task.getBody());
                    ((TextView) findViewById(R.id.date)).setText(Task.getFullDate(task.getStartDate(),task.getDueDate(),task.getDoneDate()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
