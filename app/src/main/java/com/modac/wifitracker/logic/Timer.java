package com.modac.wifitracker.logic;


import android.os.Handler;
import android.os.Message;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pascal Goldbrunner
 */

public class Timer extends Handler{

    private static Timer instance;
    private Set<Runnable> runnables;
    private boolean running;

    public Timer(){
        runnables = new HashSet<>();
        running = true;
        sendMessageDelayed(obtainMessage(), 1000);
    }

    @Override
    public void handleMessage(Message msg) {
        if(!running) return;

        for (Runnable run : runnables) {
            run.run();
        }

        sendMessageDelayed(obtainMessage(), 1000);
    }

    public static Timer getInstance(){
        if (instance==null)
            instance = new Timer();
        return instance;
    }

    public void addRunnable(Runnable runnable){
        runnables.add(runnable);
    }

    public void removeRunnable(Runnable runnable){
        runnables.remove(runnable);
    }

}

/*
    final Handler bombHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if (!bombTimerRunning) {
                return;
            }

            boolean bombPlaced = false;
            while(!bombPlaced){
                int x = (int) (Math.random() * tiles.length);
                int y = (int) (Math.random() * tiles[x].length);
                if(tiles[x][y].isBlank()){
                    tiles[x][y].setBomb();
                    detonateBomb(x,y);
                    bombPlaced = true;
                }
            }
            sendMessageDelayed(obtainMessage(), delay);

        }
    };

bombHandler.sendMessageDelayed(bombHandler.obtainMessage(), delay+1000);
*/
