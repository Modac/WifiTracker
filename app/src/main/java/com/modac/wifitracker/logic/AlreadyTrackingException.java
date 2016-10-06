package com.modac.wifitracker.logic;

/**
 * Created by Pascal Goldbrunner
 */
public class AlreadyTrackingException extends Exception {

    AlreadyTrackingException(){
        super("TrackManager is already recording");
    }
}
