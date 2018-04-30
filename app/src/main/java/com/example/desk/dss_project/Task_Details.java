package com.example.desk.dss_project;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
* this activity is created when the user clicks on the eye button to show the task details
* the activity calls the database to get the data of the task with id ( id )
* the result is the data of the task shown on the screen
* */
public class Task_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details);
        FirebaseDatabase database = Utils.getDatabase();
        database.getReference(Utils.getAuth().getUid()+"/"+ Task_ToDo.editThis).addValueEventListener(new ValueEventListener() {
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
