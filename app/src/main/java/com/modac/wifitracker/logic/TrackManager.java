package com.modac.wifitracker.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.modac.wifitracker.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Pascal Goldbrunner
 */
public class TrackManager {

    private static String TAG = "TrackManager";

    private AppCompatActivity activity;

    private Set<RoomTrackRecord> savedRecords;
    @SuppressLint("StaticFieldLeak")
    private static TrackManager instance;
    private boolean recording;

    private ScanReceiver receiver = null;

    private static final double DEFAULT_MAXIMUM_DISTANCE = 10.0;
    private static final String DEFAULT_RECORDS_FILE =  "records.json";

    private TrackManager(){
        this.activity = MainActivity.instance;
        savedRecords = new TreeSet<>();
        updateLoadedRecords();
    }

    public Set<RoomTrackRecord> getSavedRecords(){
        return savedRecords;
    }

    public Map<RoomTrackRecord, Double> track() {
        return track(DEFAULT_MAXIMUM_DISTANCE);
    }

    private Map<RoomTrackRecord, Double> track(@SuppressWarnings("SameParameterValue") double distance) {

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

    private Map<RoomTrackRecord, Double> track(TrackRecord record, @SuppressWarnings("UnusedParameters") double distance) {
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
        if (recording){
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

    private void updateLoadedRecords() {
        savedRecords.clear();
        File file = new File(activity.getFilesDir(), DEFAULT_RECORDS_FILE);
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            FileInputStream fis = activity.openFileInput(DEFAULT_RECORDS_FILE);
            InputStreamReader isr = new InputStreamReader(fis);

            Gson gson = new Gson();
            Type savedRecordsType = new TypeToken<Set<RoomTrackRecord>>(){}.getType();

            Set<RoomTrackRecord> rtrSet = gson.fromJson(isr, savedRecordsType);
            if(rtrSet!=null){
                savedRecords.addAll(rtrSet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRoomTrackRecord(RoomTrackRecord record){

        if(savedRecords.contains(record)){
            savedRecords.remove(record);
            savedRecords.add(record);
            Log.d(TAG, "addRTR: Replaced " + record.getRoom());
        } else {
            savedRecords.add(record);

        }

        try {
            //FileOutputStream fos = activity.openFileOutput(DEFAULT_RECORDS_FILE, AppCompatActivity.MODE_PRIVATE);
            //OutputStreamWriter osw = new OutputStreamWriter(fos);
            Writer w = new FileWriter(new File(activity.getFilesDir(), DEFAULT_RECORDS_FILE));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(savedRecords, w);
            Log.d(TAG, gson.toJson(savedRecords));
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRoomTrackRecord(String room){
        savedRecords.remove(new RoomTrackRecord(room));
    }

    public static TrackManager getInstance(){
        if (instance==null)
            instance = new TrackManager();
        return instance;
    }
}
