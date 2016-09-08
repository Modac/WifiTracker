package com.modac.wifitracker.listeners;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.modac.wifitracker.logic.AlreadyTrackingException;
import com.modac.wifitracker.logic.RoomTrackRecord;
import com.modac.wifitracker.logic.TrackManager;

import org.w3c.dom.Text;

import java.util.Map;

/**
 * Created by Keller2 on 23.06.2016.
 */
public class WhereButtonClickListener implements View.OnClickListener {

    TextView textView;
    Button button;

    public WhereButtonClickListener(TextView textView, Button button){
        this.textView=textView;
        this.button = button;
    }

    @Override
    public void onClick(View v) {
        /*
        button.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    setResult(TrackManager.getInstance(null).track());
                } catch (AlreadyTrackingException e) {
                    e.printStackTrace();
                } finally {
                    button.setEnabled(true);
                }
            }
        }).run();
        */
        try {

            setResult(TrackManager.getInstance(null).track());
        } catch (AlreadyTrackingException e) {
            e.printStackTrace();
        }
    }

    public void setResult(Map<RoomTrackRecord, Double> map){
        String res = "";
        for (Map.Entry<RoomTrackRecord, Double> entry : map.entrySet()){
            res+= entry.getValue() + ": " + entry.getKey().getRoom() + "\n";
        }
        textView.setText(res);
    }
}
