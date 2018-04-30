package com.example.desk.dss_project;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class Task {
    private boolean done;
    private String title, body;
    private Date startDate, doneDate, dueDate;

    public Task(){}
    Task(String title,Date dueDate, String body){
        this.title=title;
        this.body=body;
        this.dueDate = dueDate;
        this.startDate = Calendar.getInstance().getTime();
        this.doneDate= null;
        this.done = false;
    }

    public boolean isDone() {
        return this.done;
    }

    public String getTitle() {
        return this.title;
    }

    public Date getDoneDate() {
        return this.doneDate;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*
    * getFullDate is invoked to get the date combined in a format to be shown.
    * the parameters are the startDate, dueDate and doneDate ( they can be null if not exists ).
    * the return value is the dates combined together to be shown.*/
    public static String getFullDate(Date startDate, Date dueDate, Date doneDate){
        StringBuilder date =
                new StringBuilder("Started: "+ DateFormat.format("yyyy-MM-dd hh:mm:ss a", startDate).toString());
        if(dueDate != null)
            date.append("\nDue:     "+ DateFormat.format("yyyy-MM-dd hh:mm    a", dueDate).toString());
        if(doneDate !=null)
            date.append("\nDone:    "+ DateFormat.format("yyyy-MM-dd hh:mm:ss a", doneDate).toString());
        return date.toString();
    }
}
