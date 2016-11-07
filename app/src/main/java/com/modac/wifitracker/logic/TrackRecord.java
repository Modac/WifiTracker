package com.modac.wifitracker.logic;

import android.net.wifi.ScanResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pascal Goldbrunner
 */
public class TrackRecord {

    private static final String TAG = "TrackRecord";

    private double accuracy;
    private Set<WifiApRecord> wifiApRecords;


    public TrackRecord() {
        wifiApRecords = new HashSet<>();
        accuracy = 0f;
    }

    TrackRecord(Collection<WifiApRecord> wifiApRecords) {
        this.wifiApRecords = new HashSet<>(wifiApRecords);
        accuracy = 0f;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public Set<WifiApRecord> getWifiApRecords() {
        return wifiApRecords;
    }

    void update(Collection<ScanResult> scanResults) {
        for (ScanResult scanResult : scanResults) {

            WifiApRecord war;
            if ((war = contains(scanResult.BSSID, scanResult.SSID)) == null) {
                war = new WifiApRecord(scanResult.BSSID, scanResult.SSID, scanResult.frequency);
                wifiApRecords.add(war);
            }
            war.addLevel(scanResult.level);

        }
    }

    private WifiApRecord contains(String BSSID, String SSID) {
        for (WifiApRecord wifiApRecord : wifiApRecords) {
            if (wifiApRecord.getAp().getBSSID().equals(BSSID) && wifiApRecord.getAp().getSSID().equals(SSID))
                return wifiApRecord;
        }
        return null;
    }


    double compare(TrackRecord record) {
        double sum = -1;
        boolean first = true;
        for (WifiApRecord wifiApRecord : wifiApRecords) {
            if (first) {
                sum = 0;
                first = false;
            }
            WifiApRecord war = record.contains(wifiApRecord.getAp().getBSSID(), wifiApRecord.getAp().getSSID());
            double avgLevel = war == null ? -100 : war.getAvgLevel();
            //double avgLevel = war == null ? -100 : record.getWifiApRecords()..getAvgLevel();

            //Log.d(TAG, wifiApRecord.getSSID() + ": " + avgLevel + ", " + wifiApRecord.getAvgLevel());

            sum += wifiApRecord.getDistance(avgLevel);
        }
        return sum;
    }

}
