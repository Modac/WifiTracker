package com.modac.wifitracker.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pascal Goldbrunner
 */
class WifiApRecord {

    private boolean active;
    private AccessPoint ap;
    private List<Integer> levels;

    private WifiApRecord(boolean active, String BSSID, String SSID, int frequency, List<Integer> levels){
        this.active = active;
        ap = new AccessPoint(BSSID, SSID, frequency);
        this.levels = levels;
    }

    WifiApRecord(String BSSID, String SSID, int frequency){
        this(true, BSSID, SSID, frequency, new ArrayList<Integer>());
    }

    public boolean isActive() {
        return active;
    }

    AccessPoint getAp(){
        return ap;
    }

    public List<Integer> getLevels() {
        return levels;
    }

    void addLevel(int level){
        levels.add(level);
    }

    double getDistance(double level){
        return Math.abs(level - getAvgLevel());
    }

    double getAvgLevel(){
        int sum = 0;
        for(int level : levels){
            sum+=level;
        }
        return sum/levels.size();
    }

}
