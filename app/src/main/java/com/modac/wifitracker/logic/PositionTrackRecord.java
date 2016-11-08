package com.modac.wifitracker.logic;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pascal Goldbrunner
 */
public class PositionTrackRecord extends TrackRecord implements Comparable<PositionTrackRecord>{

    private static String TAG = "PositionTrackRecord";

    private String position;



    public PositionTrackRecord(String position, Set<WifiApRecord> wifiApRecords) {
        super(wifiApRecords);
        this.position = position;
    }

    public PositionTrackRecord(String position) {
        this(position, new HashSet<WifiApRecord>());
    }

    public String getPosition() {
        return position;
    }

    @Override
    public int compareTo(PositionTrackRecord o) {
        return position.compareTo(o.getPosition());
    }
}
