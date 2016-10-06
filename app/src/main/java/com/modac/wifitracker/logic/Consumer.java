package com.modac.wifitracker.logic;

/**
 * Created by Pascal Goldbrunner
 */
public interface Consumer<T> {

    void accept(T a);
}
