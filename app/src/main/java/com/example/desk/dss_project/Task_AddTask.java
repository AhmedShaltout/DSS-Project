package com.example.desk.dss_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Task_AddTask extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private Date dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add_task);
        FirebaseDatabase database = Utils.getDatabase();
        if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        myRef = database.getReference(currentUser.getUid());
    }
    public void Save(View view){
        EditText titleField = findViewById(R.id.add_title), bodyField = findViewById(R.id.add_body);
        final String title = titleField.getText().toString(),
                body = bodyField.getText().toString(),
                message;
        boolean titleLenZero = title.length() ==0, bodyLenZero = body.length() == 0 ;
        if( titleLenZero || bodyLenZero){
            if(titleLenZero)
                setBackground(titleField,R.drawable.rect_red);
            else
                setBackground(titleField,R.drawable.rect_black);
            if(bodyLenZero)
                setBackground(bodyField,R.drawable.rect_red);
            else
                setBackground(bodyField,R.drawable.rect_black);
            message = getString(R.string.fillAll);
        } else {
            Task task = new Task(title, dueDate, body);
            myRef.push().setValue(task);
            message = getString(R.string.addedSuccessfully);
            onBackPressed();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void setBackground(View view, int resource){
        view.setBackgroundResource(resource);
    }

    public void pickDate(View view){
        final Calendar currentDate = Calendar.getInstance();
        final Calendar pickedDate = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                pickedDate.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(Task_AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        pickedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        pickedDate.set(Calendar.MINUTE, minute);
                        dueDate = pickedDate.getTime();
                        ((TextView) findViewById(R.id.dueDate)).setText(DateFormat.format("yyyy-MM-dd hh:mm a", dueDate).toString());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    public void Cancel(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
