package com.example.desk.dss_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
* this activity is called to edit some task by clicking the pencil button from todo fragment
* it shows all the details of the task but in editable fields
* */
public class Task_EditTask extends AppCompatActivity {

    private DatabaseReference myRef;
    private Task task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);
        String id = Task_ToDo.editThis;
        FirebaseDatabase database = Utils.getDatabase();
        myRef = database.getReference(Utils.getAuth().getUid()).child(id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    task = dataSnapshot.getValue(Task.class);
                    ((EditText) findViewById(R.id.edit_title)).setText(task.getTitle());
                    ((EditText) findViewById(R.id.edit_body)).setText(task.getBody());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /*
    * is called when the user finishes editing the task to save it in the database
    * the parameter is the clicked view (save button)
    * the result is the data saved to the database
    * */
    public void Save(View view){
        final String title = ((EditText)findViewById(R.id.edit_title)).getText().toString(),
                body = ((EditText)findViewById(R.id.edit_body)).getText().toString(),
                message;
        if(title.equals("") || body.equals("") ){
            message = getString(R.string.fillAll);
        }else{
            task.setTitle(title);
            task.setBody(body);
            myRef.setValue(task);
            message = getString(R.string.addedSuccessfully);
            onBackPressed();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /*
    * is called to close the activity
    * the parameter is the view clicked (cancel button)
    * the result activity is terminated
    **/
    public void Cancel(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
