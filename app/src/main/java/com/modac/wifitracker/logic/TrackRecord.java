package com.modac.wifitracker.logic;

import android.net.wifi.ScanResult;

import java.security.InvalidParameterException;
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
                war = new WifiApRecord(scanResult.BSSID, scanResult.SSID);
                wifiApRecords.add(war);
            }
            war.addLevel(scanResult.level);

        }
    }

    private WifiApRecord contains(String BSSID, String SSID) {
        for (WifiApRecord wifiApRecord : wifiApRecords) {
            if (wifiApRecord.getBSSID().equals(BSSID) && wifiApRecord.getSSID().equals(SSID))
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
            WifiApRecord war = record.contains(wifiApRecord.getBSSID(), wifiApRecord.getSSID());
            double avgLevel = war == null ? -100 : war.getAvgLevel();

            //Log.d(TAG, wifiApRecord.getSSID() + ": " + avgLevel + ", " + wifiApRecord.getAvgLevel());

            sum += wifiApRecord.getDistance(avgLevel);
        }
        return sum;
    }

    @Deprecated
    public String serialize() {
        String res = "'" + accuracy + "'" + Strings.TR_SERIALIZE_FIELD_SPACER + "'";
        boolean first = true;
        for (WifiApRecord wifiApRecord : wifiApRecords) {
            if (!first) res += Strings.TR_SERIALIZE_LIST_ITEM_SPACER;
            else first = false;
            res += wifiApRecord.serialize();
        }
        res += "'";
        return res;
    }

    @Deprecated
    static TrackRecord deserialize(String string) throws InvalidParameterException {
        String[] fields = string.split(Strings.TR_SERIALIZE_FIELD_SPACER);

        if (fields.length != 2
                || !hasQuotes(fields[0])
                || !hasQuotes(fields[1])) {
            throw new InvalidParameterException("Invalid string of serialized TrackRecord");
        }

        double acc = Double.valueOf(trimQuotes(fields[0]));

        String[] parts = trimQuotes(fields[1]).split(Strings.TR_SERIALIZE_LIST_ITEM_SPACER);
        Set<WifiApRecord> wars = new HashSet<>();
        for (String part : parts) {
            wars.add(WifiApRecord.deserialize(part));
        }
        return new TrackRecord(wars);

    }

    static String trimQuotes(String string) {
        return string.substring(1, string.length() - 1);
    }

    static boolean hasQuotes(String string){
        return string.startsWith("'") && string.endsWith("'");
    }
}
