package com.example.desk.dss_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;


public class Task_ToDo extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private LinearLayout cont;
    public static String editThis;

    public Task_ToDo() { }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_todo, container, false);
        cont = view.findViewById(R.id.Todo);
        if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = Utils.getDatabase();
        myRef = database.getReference(currentUser.getUid());
        (view.findViewById(R.id.addNew) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNew();
            }
        });
        myRef.orderByChild(getString(R.string.done)).equalTo(false)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        cont.removeAllViews();
                        if(dataSnapshot.hasChildren()) {
                            for (DataSnapshot taskSnap : dataSnapshot.getChildren()) {
                                View inflatedView = inflater.inflate(R.layout.task, cont, false);
                                LinearLayout layout = inflatedView.findViewById(R.id.task);
                                setListeners(layout);
                                Task task = taskSnap.getValue(Task.class);
                                ((TextView) layout.findViewById(R.id.idHolder)).setText(taskSnap.getKey());
                                ((TextView) layout.findViewById(R.id.title)).setText(task.getTitle());
                                ((TextView) layout.findViewById(R.id.date)).setText(Task.getFullDate(task.getStartDate(),task.getDueDate(),task.getDoneDate()));
                                cont.addView(layout, 0);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
        view.findViewById(R.id.logOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), User_Login.class));
            }
        });
        return view;
    }
    private void addNew(){
        startActivity(new Intent(getActivity(), Task_AddTask.class));
    }

    private void setListeners(final LinearLayout layout) {
        final Button delete = layout.findViewById(R.id.delete),
                edit = layout.findViewById(R.id.edit),
                view = layout.findViewById(R.id.details);
        final Switch swi = layout.findViewById(R.id.markDone);

        ////// Mark Task Done //////
        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if( isChecked ){
                    LinearLayout task = (LinearLayout) buttonView.getParent();
                    String id = ((TextView)task.findViewById(R.id.idHolder)).getText().toString();
                    myRef.child(id + "/"+getString(R.string.doneDate)).setValue(Calendar.getInstance().getTime());
                    myRef.child(id + "/"+getString(R.string.done)).setValue(true);
                }
            }
        });
        //////////// Delete Task////////////
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                LinearLayout task = (LinearLayout) v.getParent().getParent();
                                String id = ((TextView)task.findViewById(R.id.idHolder)).getText().toString();
                                myRef.child(id).removeValue();
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setMessage(getString(R.string.deleteConfirm)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show();
            }
        });


        //////// Edit Task///////
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout task = (LinearLayout) v.getParent().getParent();
                editThis = ((TextView)task.findViewById(R.id.idHolder)).getText().toString();
                startActivity(new Intent(getActivity(),Task_EditTask.class));
            }
        });

        /////// Show Details///////
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout task = (LinearLayout) v.getParent().getParent();
                editThis = ((TextView)task.findViewById(R.id.idHolder)).getText().toString();
                startActivity(new Intent(getActivity(),Task_Details.class));
            }
        });
    }

}