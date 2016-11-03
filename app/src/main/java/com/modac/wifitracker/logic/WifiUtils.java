package com.modac.wifitracker.logic;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Pascal Goldbrunner
 */
public class WifiUtils {

    public static String formatScanResults(Collection<ScanResult> scanResults){
        String res = "";
        List<ScanResult> scanResultsS = new ArrayList<>(scanResults);
        Collections.sort(scanResultsS, new ScanResultsLevelSortComparator());
        for (ScanResult sR : scanResultsS) {
            res += String.format("%s | %s | %s | %s\n", sR.SSID, sR.BSSID, sR.level, sR.frequency);
        }
        return res;
    }

    private static class ScanResultsLevelSortComparator implements Comparator<ScanResult> {

        @Override
        public int compare(ScanResult lhs, ScanResult rhs){
            //return lhs.level - rhs.level;
            return WifiManager.compareSignalLevel(rhs.level, lhs.level);
        }
    }

    public int qualityOfdBm(int level){
        if(level <= -100)
            return 0;
        else if(level >= -50)
            return 100;
        else
            return 2 * (level + 100);
    }
}
