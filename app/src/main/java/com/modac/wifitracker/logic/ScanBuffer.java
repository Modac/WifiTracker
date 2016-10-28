package com.modac.wifitracker.logic;

import android.net.wifi.ScanResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pascal Goldbrunner on 24.10.16.
 *
 */

public class ScanBuffer {
    private static ScanBuffer instance;

    // BSSID is used as Key
    Map<String, ApBuffer> apBufferMap;

    public ScanBuffer(){
        apBufferMap = new HashMap<>();
    }

    void update(Collection<ScanResult> scanResults) {

        Map<String, Integer> levelMap = new HashMap<>(scanResults.size());

        for (ScanResult scanResult : scanResults) {

            /*if (apBufferMap.containsKey(scanResult.BSSID)) {
                apBufferMap.get(scanResult.BSSID).updateValue(scanResult.level);
            }*/
            levelMap.put(scanResult.BSSID, scanResult.level);
        }

        for (Map.Entry<String, ApBuffer> mapEntry : apBufferMap.entrySet()){
            if (levelMap.containsKey(mapEntry.getKey())){
                mapEntry.getValue().updateValue(levelMap.get(mapEntry.getKey()));
            } else {
                mapEntry.getValue().updateValue(0);
            }
        }

        for (Map.Entry<String, Integer> mapEntry : levelMap.entrySet()){
            if(!apBufferMap.containsKey(mapEntry.getKey())){
                apBufferMap.put(mapEntry.getKey(), new ApBuffer(mapEntry.getValue()));
            }
        }

    }


    public static ScanBuffer getInstance(){
        if (instance==null)
            instance = new ScanBuffer();
        return instance;
    }

    private class ApBuffer {

        private int lastValue;
        private int n;
        private int value;

        private ApBuffer(){
            this.reset();
        }

        private ApBuffer(int startValue){
            this();
            updateValue(startValue);
        }

        private void updateValue(int val){
            n++;
            value = (n-1)/n * value + (val/n);
        }

        private int getValue(){
            if(n>0) {
                lastValue = value;
                n = 0;
                value = 0;
            }
            return lastValue;
        }

        private void reset(){
            lastValue=0;
            n=0;
            value=0;
        }

    }
}
