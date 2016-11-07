package com.modac.wifitracker.logic;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pascal Goldbrunner
 */
public class RoomTrackRecord extends TrackRecord implements Comparable<RoomTrackRecord>{

    private static String TAG = "RoomTrackRecord";

    private String room;



    public RoomTrackRecord(String room, Set<WifiApRecord> wifiApRecords) {
        super(wifiApRecords);
        this.room = room;
    }

    public RoomTrackRecord(String room) {
        this(room, new HashSet<WifiApRecord>());
    }

    public String getRoom() {
        return room;
    }

    @Override
    public int compareTo(RoomTrackRecord o) {
        return room.compareTo(o.getRoom());
    }
}
