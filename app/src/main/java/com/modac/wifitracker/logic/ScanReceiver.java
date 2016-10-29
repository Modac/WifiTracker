package com.modac.wifitracker.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by Pascal Goldbrunner.
 *
 */
public class ScanReceiver extends BroadcastReceiver{
    private Consumer<List<ScanResult>> consumer;
    private AppCompatActivity activity;

    public ScanReceiver(AppCompatActivity activity, Consumer<List<ScanResult>> consumer){
        this.consumer=consumer;
        this.activity=activity;
    }

    public static ScanReceiver register(AppCompatActivity activity, Consumer<List<ScanResult>> consumer){
        ScanReceiver receiver = new ScanReceiver(activity, consumer);
        receiver.register();
        return receiver;
    }

    public void register(){
        activity.registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();

    }

    public void unregister(){
        activity.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        //Log.d(this.getClass().getSimpleName(), "onRec");
        consumer.accept(wifiManager.getScanResults());
        wifiManager.startScan();
    }
}
