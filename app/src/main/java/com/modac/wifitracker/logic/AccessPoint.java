package com.modac.wifitracker.logic;

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;

/**
 * Created by Pascal Goldbrunner
 */

public class AccessPoint implements Comparable<AccessPoint>{
    String BSSID;
    String SSID;
    int frequency;

    public AccessPoint(String BSSID, String SSID, int frequency) {
        this.BSSID = BSSID;
        this.SSID = SSID;
        this.frequency = frequency;
    }

    public String getBSSID() {
        return BSSID;
    }

    public String getSSID() {
        return SSID;
    }

    public int getFrequency() {
        return frequency;
    }

    public AccessPoint clone(AccessPoint ap){
        return new AccessPoint(ap.getBSSID(), ap.getSSID(), ap.getFrequency());
    }

    public static AccessPoint generateOf(ScanResult scanResult){
        return new AccessPoint(scanResult.BSSID, scanResult.SSID, scanResult.frequency);
    }

    public boolean equals(AccessPoint ap) {
        return ap.getSSID().equals(getSSID()) && ap.getBSSID().equals(getBSSID()) && ap.getFrequency()==getFrequency();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AccessPoint)
            return equals((AccessPoint)obj);
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getSSID()+getBSSID()+getFrequency();
    }

    @Override
    public int compareTo(@NonNull AccessPoint o) {
        return equals(o) ? 0 : o.toString().compareTo(toString());
    }
}
