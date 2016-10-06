package com.modac.wifitracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.modac.wifitracker.logic.RoomTrackRecord;
import com.modac.wifitracker.logic.TrackManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordsFragment extends Fragment {


    private ListView recordsListView;

    public RecordsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        recordsListView = (ListView) view.findViewById(R.id.recordsListView);
        TrackManager tm = TrackManager.getInstance();
        if (tm.getSavedRecords().size()>0) {
            String[] rooms = new String[tm.getSavedRecords().size()];
            for (int i = 0; i < rooms.length; i++) {
                rooms[i] = ((RoomTrackRecord) tm.getSavedRecords().toArray()[i]).getRoom();
                Log.d("RF", rooms[i]);
            }
            recordsListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, rooms));
        }
        return view;
    }

}
