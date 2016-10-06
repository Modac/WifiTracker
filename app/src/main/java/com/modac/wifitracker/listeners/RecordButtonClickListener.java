package com.modac.wifitracker.listeners;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.modac.wifitracker.R;
import com.modac.wifitracker.RecordNewFragment;

/**
 * Created by Pascal Goldbrunner
 */
public class RecordButtonClickListener implements View.OnClickListener {

    private AppCompatActivity activity;

    public RecordButtonClickListener(AppCompatActivity activity){
        this.activity=activity;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragMan = activity.getSupportFragmentManager();
        fragMan.beginTransaction().replace(R.id.relLayMain, new RecordNewFragment()).commit();

    }
}
