package com.modac.wifitracker;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.modac.wifitracker.logic.AlreadyTrackingException;
import com.modac.wifitracker.logic.RoomTrackRecord;
import com.modac.wifitracker.logic.TrackManager;
import com.modac.wifitracker.logic.TrackRecord;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordNewFragment extends Fragment {

    private EditText roomTextField;
    private Button recButton;

    private boolean recording;
    private TrackRecord trackRecord;

    private AppCompatActivity activity;

    public RecordNewFragment() {
        // Required empty public constructor
        recording = false;
        trackRecord = new TrackRecord();
        activity = (AppCompatActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_new, container, false);
        roomTextField = (EditText) view.findViewById(R.id.roomTextField);

        recButton = (Button) view.findViewById(R.id.recButton);
        recButton.setOnClickListener(new RecButtonListener());

        getChildFragmentManager().beginTransaction().replace(R.id.newRecInnerRelLay, new ApListFragment()).commit();

        return view;
    }

    private class RecButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            TrackManager trackManager = TrackManager.getInstance();
            if(!recording){
                try {
                    trackRecord = trackManager.startRecord();
                    toActive();
                    recording = true;
                } catch (AlreadyTrackingException e) {
                    //noinspection ConstantConditions
                    Snackbar.make(getView(), "Already tracking", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            } else {
                toInactive();
                trackManager.stopRecord();
                recording = false;
                trackManager.addRoomTrackRecord(new RoomTrackRecord(roomTextField.getText().toString(), trackRecord.getWifiApRecords()));
            }
        }
    }

    private void toActive(){
        roomTextField.setEnabled(false);
        recButton.setText(R.string.recButtonStop);
    }

    private void toInactive(){
        roomTextField.setEnabled(true);
        recButton.setText(R.string.recButtonStart);
    }


}
