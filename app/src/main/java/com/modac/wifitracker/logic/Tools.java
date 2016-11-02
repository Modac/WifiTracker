package com.modac.wifitracker.logic;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.AbstractList;
import java.util.List;

/**
 * Created by Pascal Goldbrunner
 */
public class Tools {

    public static List<Integer> asList(final int[] is)
    {
        return new AbstractList<Integer>() {
            public Integer get(int i) { return is[i]; }
            public int size() { return is.length; }
        };
    }


    // Source: http://stackoverflow.com/questions/8295986/how-to-calculate-dp-from-pixels-in-android-programmatically
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getDisplayWidthDp(){
        Configuration configuration = Resources.getSystem().getConfiguration();
        return configuration.screenWidthDp;
    }

    public static int getDisplayWidthPx(){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.widthPixels;
    }
}
