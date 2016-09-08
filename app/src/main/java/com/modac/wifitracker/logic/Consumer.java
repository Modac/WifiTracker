package com.modac.wifitracker.logic;

/**
 * Created by Keller2 on 24.06.2016.
 */
public interface Consumer<T> {

    public void accept(T a);
}
