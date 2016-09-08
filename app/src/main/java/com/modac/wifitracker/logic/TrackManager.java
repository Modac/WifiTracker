package com.modac.wifitracker.logic;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.modac.wifitracker.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.InvalidParameterException;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Keller2 on 23.06.2016.
 */
public class TrackManager {

    private static String TAG = "TrackManager";

    private AppCompatActivity activity;

    private Set<RoomTrackRecord> savedRecords;
    private static TrackManager instance;
    private boolean recording;

    private ScanReceiver receiver = null;

    private static final double DEFAULT_MAXIMUM_DISTANCE = 10.0;
    private static final String DEFAULT_RECORDS_FILE =  "records.save";

    private TrackManager(AppCompatActivity activity){
        this.activity = MainActivity.instance;
        savedRecords = new HashSet<>();
        updateLoadedRecords();
    }

    public Set<RoomTrackRecord> getSavedRecords(){
        return savedRecords;
    }

    public Map<RoomTrackRecord, Double> track() throws AlreadyTrackingException {
        return track(DEFAULT_MAXIMUM_DISTANCE);
    }

    public Map<RoomTrackRecord, Double> track(double distance) throws AlreadyTrackingException {

        TrackRecord tr = new TrackRecord();
        /*
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stopRecord();
        */

        //Log.d("TM", "track2");
        WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        tr.update(wifiManager.getScanResults());
        wifiManager.startScan();

        return track(tr, distance);
    }

    public Map<RoomTrackRecord, Double> track(TrackRecord record){
        return track(record, DEFAULT_MAXIMUM_DISTANCE);
    }

    public Map<RoomTrackRecord, Double> track(TrackRecord record, double distance){
        //Log.d("TM", "track4");
        Map<RoomTrackRecord, Double> resMap = new HashMap<>();
        for (RoomTrackRecord rtr : savedRecords){
            double dis = rtr.compare(record);
            /*if(dis<=distance)*/ resMap.put(rtr, dis);
            //Log.d("TM", "track4Loop");
        }
        return resMap;
    }

    public TrackRecord startRecord() throws AlreadyTrackingException {
        if (recording==true){
            throw new AlreadyTrackingException();
        }
        final TrackRecord tr = new TrackRecord();

        receiver = ScanReceiver.register(activity, new Consumer<List<ScanResult>>() {
            @Override
            public void accept(List<ScanResult> a) {
                tr.update(a);
            }
        });

        return tr;

    }

    public void stopRecord(){
        if (receiver !=null) receiver.unregister();
        recording = false;
    }

    public void update(String room){
// TODO: 24.06.2016
    }

    public void updateLoadedRecords() {
        savedRecords.clear();
        File file = new File(activity.getFilesDir(), DEFAULT_RECORDS_FILE);
        try {
            file.createNewFile();
            FileInputStream fis = activity.openFileInput(DEFAULT_RECORDS_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis), 2097152);
            String line;
            while ((line=br.readLine())!=null){
                Log.d(TAG, line);
                try {
                    savedRecords.add(RoomTrackRecord.deserialize(line));
                } catch (InvalidParameterException e) {
                    Log.w(TAG, "Ignoring line of wrong format");
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRoomTrackRecord(RoomTrackRecord record){

        savedRecords.add(record);
        try {
            FileOutputStream fos = activity.openFileOutput(DEFAULT_RECORDS_FILE, AppCompatActivity.MODE_APPEND);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            String serialize = record.serialize();
            //Log.d(TAG, serialize);
            bw.write(serialize + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TrackManager getInstance(AppCompatActivity activity){
        if (instance==null)
            instance = new TrackManager(activity);
        return instance;
    }
}
