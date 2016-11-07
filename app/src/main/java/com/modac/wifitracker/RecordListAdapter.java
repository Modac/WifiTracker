package com.modac.wifitracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.modac.wifitracker.logic.RoomTrackRecord;
import com.modac.wifitracker.logic.TrackManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Pascal Goldbrunner
 */

public class RecordListAdapter extends BaseAdapter {

    Context context;
    List<RoomTrackRecord> records;

    public RecordListAdapter(Context context, Collection<RoomTrackRecord> records){
        this.context = context;
        this.records = new ArrayList<>();
    }

    public RecordListAdapter(Context context){
        this(context, TrackManager.getInstance().getSavedRecords());
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView==null){
            view  = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.record_list_item, parent, false);
            ((TextView) view.findViewById(R.id.roomTextView)).setText(records.get(position).getRoom());
            ((ImageView) view.findViewById(R.id.deleteButtonImageView)).setOnClickListener(new DeleteListener(records.get(position)));
        } else {
            view = convertView;
        }

        return view;
    }

    private void update(Collection<RoomTrackRecord> records){
         this.records.clear();
        this.records.addAll(records);
    }

    private class DeleteListener implements View.OnClickListener{
        RoomTrackRecord rtr;

        private DeleteListener(RoomTrackRecord roomTrackRecord){
            rtr=roomTrackRecord;
        }

        @Override
        public void onClick(View v) {
            TrackManager.getInstance().deleteRoomTrackRecord(rtr.getRoom());
            update(TrackManager.getInstance().getSavedRecords());
        }
    }
}
