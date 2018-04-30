package com.example.desk.dss_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Arrays;
import java.util.Comparator;


public class Task_Done extends Fragment {

    private DatabaseReference myRef;
    private LinearLayout cont;

    public Task_Done() {
    }

    /*
    * when the fragment is created it calls the database to get all the done tasks
    * and it sorts the tasks by done date, the way we show the content is very old,
    * we create a linear layout, and inflate the task_done layout then add the task details
    * and show it in the container doneView which in vertical LinearLayout and the parent is
    * scroll view
    * setting some listeners using setListeners function ( defined below ) to add functionality to
     * the buttons ( eye, recycle )
     * the result is all done tasks shown.
     * */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_done, container, false);
        cont = view.findViewById(R.id.doneView);
        FirebaseDatabase database = Utils.getDatabase();
        myRef = database.getReference(Utils.getAuth().getUid());
        myRef.orderByChild(getString(R.string.done)).equalTo(true)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        cont.removeAllViews();
                        if(dataSnapshot.hasChildren()) {
                            int childrenCount = (int) dataSnapshot.getChildrenCount();
                            Task[] tasks = new Task[childrenCount];
                            String []keys = new String[childrenCount];
                            int index =0;
                            for (DataSnapshot taskSnap : dataSnapshot.getChildren()) {
                                tasks[index] = taskSnap.getValue(Task.class);
                                keys[index] = taskSnap.getKey();
                                index++;
                            }
                            Arrays.sort(tasks, new Comparator<Task>() {
                                @Override
                                public int compare(Task o1, Task o2) {
                                    return o1.getDoneDate().compareTo(o2.getDoneDate());
                                }
                            });
                            index = 0;
                            for (Task task : tasks) {
                            View inflatedView = inflater.inflate(R.layout.task, cont, false);
                            LinearLayout layout = inflatedView.findViewById(R.id.task);
                            setListeners(layout);
                            ((TextView) layout.findViewById(R.id.idHolder)).setText(keys[index]);
                            index++;
                            ((TextView) layout.findViewById(R.id.title)).setText(task.getTitle());
                            ((TextView) layout.findViewById(R.id.date)).setText(Task.getFullDate(task.getStartDate(),task.getDueDate(),task.getDoneDate()));
                            cont.addView(layout,0);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
        });
        return view;
    }

    /*
    * is used to add onClickListener to every component in the view (delete and show)
    * the parameter is layout ( row )
    * the result is the listeners set to the buttons*/
    private void setListeners(final LinearLayout layout) {
        final Button delete = layout.findViewById(R.id.delete),
                edit = layout.findViewById(R.id.edit),
                view = layout.findViewById(R.id.details),
                done = layout.findViewById(R.id.markDone);
        done.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);

        ////////////Delete////////////
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE){
                            LinearLayout task = (LinearLayout) v.getParent().getParent();
                            String id = ((TextView)task.findViewById(R.id.idHolder)).getText().toString();
                            myRef.child(id).removeValue();
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setMessage(getString(R.string.deleteConfirm)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show();
            }
        });



        ///////view///////
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout task = (LinearLayout) v.getParent().getParent();
                Task_ToDo.editThis  = ((TextView)task.findViewById(R.id.idHolder)).getText().toString();
                startActivity(new Intent(getActivity(),Task_Details.class));
            }
        });
    }
}
