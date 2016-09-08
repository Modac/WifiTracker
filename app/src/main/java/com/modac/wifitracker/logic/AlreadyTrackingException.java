package com.modac.wifitracker.logic;

/**
 * Created by Keller2 on 23.06.2016.
 */
public class AlreadyTrackingException extends Exception {

    public AlreadyTrackingException(){
        super("TrackManager is already recording");
    }
}
