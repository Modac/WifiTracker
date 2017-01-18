package com.modac.wifitracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.modac.wifitracker.logic.TrackManager;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ListAdapter listAdapter;
    private int[] imageListArray = {R.drawable.ic_my_location_black_24dp, R.drawable.ic_view_headline_black_24dp, R.drawable.ic_view_headline_black_24dp};
    private String fragArray[] = {"Where", "APs", "Records"};
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private ActionBar SAB;             // Support Action Bar

    private FloatingActionButton fab;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private static final String TAG = "MainActivity";

    @SuppressLint("StaticFieldLeak")
    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        SAB = getSupportActionBar();

        if(SAB==null) {
            Log.e(TAG, "Activity has no support action bar");
            Toast.makeText(MainActivity.this, "No Support Action Bar found", Toast.LENGTH_SHORT).show();
        }

        if (SAB!=null) {
            SAB.setDisplayHomeAsUpEnabled(true);
            SAB.setHomeButtonEnabled(true);
        }

        activityTitle = getTitle().toString();

        listView = (ListView) findViewById(R.id.navList);
        listAdapter = new ImageAdapter(this, R.layout.image_list_item, R.id.imageListItemMainImage, imageListArray, R.id.imageListItemMainText, fragArray);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new NavClickListener());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setupDrawer();

        FragmentManager fragMan = getSupportFragmentManager();
        fragMan.beginTransaction().replace(R.id.relLayMain, new WhereFragment()).commit();

        drawerLayout.closeDrawers();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new FABClickListener(this));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }

    }

    private void setupDrawer(){
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (SAB!=null) SAB.setTitle(R.string.title_drawer_open);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (SAB!=null) SAB.setTitle(activityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to track your location");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Activate the navigation drawer toggle
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

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

    private class NavClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Fragment frag;
            switch (position) {
                case 0:
                    frag = new WhereFragment();
                    break;
                case 1:
                    frag = new ApListFragment();
                    break;
                case 2:
                    frag = new RecordsFragment();
                    break;
                default:
                    frag = new WhereFragment();
            }

            FragmentManager fragMan = getSupportFragmentManager();
            fragMan.beginTransaction().replace(R.id.relLayMain, frag).commit();

            drawerLayout.closeDrawers();
        }
    }

    /*void snackbar(String msg){
        Snackbar.make(drawerLayout, "Already recording", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }*/


    public void showTestDialog(View view){
        new TestDialog().show(getSupportFragmentManager(), "TestDialog");
    }

    public static class TestDialog extends DialogFragment{

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.test_dialog, null);
            TextView textView = (TextView) view.findViewById(R.id.testDialogText);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            textView.setText(gson.toJson(TrackManager.getInstance().getSavedRecords()));
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view);
            return builder.create();

        }
    }


    private class FABClickListener implements View.OnClickListener {

        private AppCompatActivity activity;

        private FABClickListener(AppCompatActivity activity){
            this.activity=activity;
        }

        @Override
        public void onClick(View v) {
            FragmentManager fragMan = activity.getSupportFragmentManager();
            fragMan.beginTransaction().replace(R.id.relLayMain, new RecordNewFragment()).commit();

        }
    }
}
