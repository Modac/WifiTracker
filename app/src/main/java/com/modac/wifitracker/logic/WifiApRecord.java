package com.modac.wifitracker.logic;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pascal Goldbrunner
 */
class WifiApRecord {

    private String BSSID;
    private String SSID;
    //private String frequency;
    private List<Integer> levels;


    private WifiApRecord(String BSSID, String SSID, List<Integer> levels){
        this.BSSID=BSSID;
        this.SSID=SSID;
        this.levels = levels;
    }

    WifiApRecord(String BSSID, String SSID){
        this(BSSID, SSID, new ArrayList<Integer>());
    }

    String getBSSID() {
        return BSSID;
    }

    String getSSID() {
        return SSID;
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

    String serialize() {
        String res = "'" + BSSID + "'" + Strings.WAR_SERIALIZE_FIELD_SPACER + "'" + SSID + "'" + Strings.WAR_SERIALIZE_FIELD_SPACER + "'";
        boolean first = true;
        for (int level : levels){
            if (!first) res+=Strings.WAR_SERIALIZE_LIST_ITEM_SPACER;
            else first=false;
            res+=level;
        }
        res+="'";
        return res;
    }

    static WifiApRecord deserialize(String string) throws InvalidParameterException {
        String[] fields = string.split(Strings.WAR_SERIALIZE_FIELD_SPACER);

        if (fields.length != 3
                || !hasQuotes(fields[0])
                || !hasQuotes(fields[1])
                || !hasQuotes(fields[2])) {
            throw new InvalidParameterException("Invalid string of serialized TrackRecord");
        }

        String BSSID = trimQuotes(fields[0]);
        String SSID = trimQuotes(fields[1]);
        String[] levelsParts = trimQuotes(fields[2]).split(Strings.WAR_SERIALIZE_LIST_ITEM_SPACER);
        List<Integer> levels = new ArrayList<>();
        for (String levelsPart : levelsParts){
            levels.add(Integer.valueOf(levelsPart));
        }
        return new WifiApRecord(BSSID, SSID, levels);
    }

    private static String trimQuotes(String string) {
        return string.substring(1, string.length() - 1);
    }

    private static boolean hasQuotes(String string){
        return string.startsWith("'") && string.endsWith("'");
    }

}
