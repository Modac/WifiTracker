package com.modac.wifitracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.modac.wifitracker.logic.PositionTrackRecord;
import com.modac.wifitracker.logic.TrackManager;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class WhereFragment extends Fragment {


    public WhereFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_where, container, false);
        Button whereButton = (Button) view.findViewById(R.id.whereButton);
        whereButton.setOnClickListener(new WhereButtonClickListener((TextView) view.findViewById(R.id.textView2), whereButton));
        return view;
    }

    private class WhereButtonClickListener implements View.OnClickListener {

        private TextView textView;
        private Button button;

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

            setResult(TrackManager.getInstance().track());
        }

        private void setResult(Map<PositionTrackRecord, Double> map){
            String res = "";
            for (Map.Entry<PositionTrackRecord, Double> entry : map.entrySet()){
                res+= entry.getValue() + ": " + entry.getKey().getPosition() + "\n";
            }
            textView.setText(res);
        }
    }

}
