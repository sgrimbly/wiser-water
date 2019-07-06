package com.example.stjohngrimbly.watersaving;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements Home.OnFragmentInteractionListener, Diary.OnFragmentInteractionListener, AddEntry.OnFragmentInteractionListener{

    public static final String TAG = "MainActivity";

    private static String selectedFragment;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawerLayout;
    private static ArrayList<Entry> entries = new ArrayList<>();
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        final NavigationView navigationView = findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawerItem(R.id.nav_third_fragment,entries.size()-1);
                navigationView.setCheckedItem(R.id.nav_third_fragment);
            }
        });

        if(savedInstanceState != null) { //&& savedInstanceState.getString("Current Fragment") != null
            Log.d(TAG, "onCreate: restoring fragment");
            entries=(ArrayList<Entry>)savedInstanceState.getSerializable("Entries"); //Retrieve program data instead of re-reading in all the data from txt file.

            ArrayList<String> mTotals=(ArrayList<String>)savedInstanceState.getSerializable("Saved Totals");
            Bundle bundle = new Bundle();
            bundle.putSerializable("Saved Totals",mTotals);

            Fragment restoredFragment = getSupportFragmentManager().getFragment(savedInstanceState, selectedFragment);
            restoredFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.flContent, restoredFragment, selectedFragment).addToBackStack(selectedFragment).commit();

            /*Log.d(TAG, "onCreate: savedInstanceState with Current Fragment = " + savedInstanceState.getString("Current Fragment"));
            String currentFragment = savedInstanceState.getString("Current Fragment");
            if (currentFragment.equals("Diary")) {
                selectDrawerItem(R.id.nav_second_fragment);
                navigationView.setCheckedItem(R.id.nav_second_fragment);
            } else if (currentFragment.equals("AddEntry")) {
                selectDrawerItem(R.id.nav_third_fragment);
                navigationView.setCheckedItem(R.id.nav_third_fragment);
            }
            else{
                selectDrawerItem(R.id.nav_first_fragment);
                navigationView.setCheckedItem(R.id.nav_first_fragment);
            }*/
        }
        else{
            Log.d(TAG, "onCreate: no savedInstanceState");
            readData(); //Only read the data in the first time the activity launches.
            selectDrawerItem(R.id.nav_first_fragment, entries.size()-1);
            navigationView.setCheckedItem(R.id.nav_first_fragment);
        }
    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Entries",entries);
        outState.putString("Current Fragment", selectedFragment);

        int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        Log.d(TAG, "onSaveInstanceState: "+ fragment);
        getSupportFragmentManager().putFragment(outState, selectedFragment, fragment);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @Override
    public void onFragmentInteraction(Uri uri){
    }

    public static void selectDrawerItem(int id, int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putSerializable("Entries", entries);
        bundle.putInt("Diary Position",position);

        Class fragmentClass;
        switch(id) {
            case R.id.nav_first_fragment:
                selectedFragment="Home";
                fragmentClass = Home.class;
                break;
            case R.id.nav_second_fragment:
                selectedFragment="Diary";
                fragmentClass = Diary.class;
                break;
            case R.id.nav_third_fragment:
                selectedFragment="AddEntry";
                fragmentClass = AddEntry.class;
                break;
            default:
                selectedFragment="Home";
                fragmentClass = Home.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        fragment.setArguments(bundle);
        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment, selectedFragment).addToBackStack(selectedFragment).commit();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        selectDrawerItem(menuItem.getItemId(), entries.size()-1);
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    public void readData(){
        /*Utilities.addDiaryEntry(this,this.getFilesDir().getAbsolutePath()+"/database.txt","3 March 2017", ",3,7,2,1,9,6,8,10,4");
        Utilities.addDiaryEntry(this,this.getFilesDir().getAbsolutePath()+"/database.txt","4 April 2017", ",5,10,10,10,10,10,10,10,9");
        Utilities.addDiaryEntry(this,this.getFilesDir().getAbsolutePath()+"/database.txt","7 June 2017", ",6,3,3,2,1,10,5,10,8");
        Utilities.addDiaryEntry(this,this.getFilesDir().getAbsolutePath()+"/database.txt","13 August 2017", ",20,13,10,17,10,10,10,10,7");
        Utilities.addDiaryEntry(this,this.getFilesDir().getAbsolutePath()+"/database.txt","16 September 2017", ",2,10,4,10,13,11,10,1,6");
        Utilities.addDiaryEntry(this,this.getFilesDir().getAbsolutePath()+"/database.txt","21 October 2017", ",8,10,10,10,10,10,10,10,10,5");
        Utilities.addDiaryEntry(this,this.getFilesDir().getAbsolutePath()+"/database.txt","28 December 2017", ",19,15,14,10,10,10,10,10,4");*/

        // Run task in background thread
                /*new Thread(new Runnable() {
            public void run() {

            }
        }).start();*/

        // NOTE: USING THREAD OR ASYNC ETC HERE CAUSES PROBLEMS WITH LOADING OF GUI. THREAD WAS THUS NOT IMPLEMENTED HERE, BUT WAS USED FOR ADDING OF ENTRIES WHERE IT IS USEFUL.
        final String path = this.getFilesDir().getAbsolutePath()+"/database.txt";
        final Context context = this;
        try{
            entries  = Utilities.LoadDiaryEntries(context, path);
        } catch(Exception e){
            System.out.println(e);
        }

    }

}
