package com.example.desk.dss_project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    /*
    * getItem is invoked when the user swipe the screen left and right it is called.
    * the parameter is the page ( fragment position ).
    * return value is the fragment in position ( position ).
    * */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Task_ToDo();

            case 1:
                return new Task_Done();

            default:
                return  null;
        }
    }

    /*
    * getCount is invoked once to prepare the page view in the beginning.
    * the return value pages count.
    * */
    @Override
    public int getCount() {
        return 2;
    }

    /*
    * getPageTitle is invoked to get the names of the fragments included
    * the parameters is the position of the fragment
    * the return value is the name of the fragment in the position ( position )*/
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "To Do";
            case 1:
                return "Done";
        }
        return null;
    }
}