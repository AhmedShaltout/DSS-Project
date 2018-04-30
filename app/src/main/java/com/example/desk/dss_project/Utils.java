package com.example.desk.dss_project;

import android.text.TextUtils;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public final class Utils {
    private static FirebaseDatabase mDatabase;
    private static FirebaseAuth mAuth;

    /*
    * gets a database instance
     */
    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    /*
    * gets authentication instance to see if there is a user logged in or not*/
    public static FirebaseAuth getAuth() {
        if (mAuth == null)
            mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }
    /*
     * is used to check if the sting is email or not
     * parameter is the string needed to check
     * the result is true if email and false if not email
     * */
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    /*
     * change background of the view by the resource id
     * the parameters are the view needed to change its background and the resource id
     * the result is the view background is changed.
     * */
    public static void setBackground(View view, int resource){
        view.setBackgroundResource(resource);
    }
}
