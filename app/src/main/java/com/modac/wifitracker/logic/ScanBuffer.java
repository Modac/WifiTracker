package com.modac.wifitracker.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pascal Goldbrunner on 24.10.16.
 *
 */

public class ScanBuffer {
    private static ScanBuffer instance;

    Map<String, ApBuffer> apBufferMap;

    public ScanBuffer(){
        apBufferMap = new HashMap<>();
    }




    public static ScanBuffer getInstance(){
        if (instance==null)
            instance = new ScanBuffer();
        return instance;
    }

    private class ApBuffer {

        private int lastValue;
        private int n;
        private int value;

        private void updateValue(int val){
            n++;
            value = (n-1)/n * value + (val/n);
        }

        private int getValue(){
            if(n>0) {
                lastValue = value;
                n = 0;
                value = 0;
            }
            return lastValue;
        }

        private void reset(){
            lastValue=0;
            n=0;
            value=0;
        }

    }
}
