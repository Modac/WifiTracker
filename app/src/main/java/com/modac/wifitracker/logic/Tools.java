package com.modac.wifitracker.logic;

import java.util.AbstractList;
import java.util.List;

/**
 * Created by Keller2 on 25.06.2016.
 */
public class Tools {

    public static List<Integer> asList(final int[] is)
    {
        return new AbstractList<Integer>() {
            public Integer get(int i) { return is[i]; }
            public int size() { return is.length; }
        };
    }
}
