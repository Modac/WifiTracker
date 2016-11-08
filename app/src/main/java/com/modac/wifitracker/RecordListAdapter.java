package com.modac.wifitracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.modac.wifitracker.logic.PositionTrackRecord;
import com.modac.wifitracker.logic.TrackManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Pascal Goldbrunner
 */

public class RecordListAdapter extends BaseAdapter {

    Context context;
    List<PositionTrackRecord> records;
    boolean hasUpdated = false;

    public RecordListAdapter(Context context, Collection<PositionTrackRecord> records){
        this.context = context;
        this.records = new ArrayList<>();
        update(records);
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

        //if(hasUpdated || convertView == null) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.record_list_item, parent, false);
            ((TextView) view.findViewById(R.id.positionTextView)).setText(records.get(position).getPosition());
            view.findViewById(R.id.deleteButtonImageView).setOnClickListener(new DeleteListener(records.get(position)));

            //if(position+1==records.size()) hasUpdated = false;

        //} else {
        //    view = convertView;
        //}

        return view;
    }

    private void update(Collection<PositionTrackRecord> records){
        this.records.clear();
        this.records.addAll(records);
        hasUpdated = true;
    }

    private class DeleteListener implements View.OnClickListener{
        PositionTrackRecord rtr;

        private DeleteListener(PositionTrackRecord positionTrackRecord){
            rtr= positionTrackRecord;
        }

        @Override
        public void onClick(View v) {
            TrackManager.getInstance().deletePositionTrackRecord(rtr.getPosition());
            update(TrackManager.getInstance().getSavedRecords());
            notifyDataSetChanged();
        }
    }
}
