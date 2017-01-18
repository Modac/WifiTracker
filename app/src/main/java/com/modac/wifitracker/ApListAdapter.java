package com.modac.wifitracker;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.support.constraint.Guideline;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.modac.wifitracker.logic.AccessPoint;
import com.modac.wifitracker.logic.WifiUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Pascal Goldbrunner
 */

public class ApListAdapter extends BaseAdapter {

    public static final String TAG = "ApListAdapter";
    private Context context;
    private List<View> viewList;
    private Map<AccessPoint, Integer> apMap;
    private List<LineGraphSeries<DataPoint>> graphSeriesList;
    private long startTime = -1;
    private RecordNewFragment rnf;

    public ApListAdapter(Context context, Collection<ScanResult> apCollection, RecordNewFragment rnf){
        this(context, rnf);

        /*for ( ScanResult sr : apCollection){
            apMap.put(AccessPoint.generateOf(sr), -1);
        }*/
        update(apCollection);
    }

    public ApListAdapter(Context context, RecordNewFragment rnf){
        this.context=context;
        this.viewList = new ArrayList<>();
        this.apMap = new TreeMap<>();
        this.graphSeriesList = new ArrayList<>();
        this.rnf = rnf;
    }

    private View generateView(AccessPoint accessPoint, ViewGroup root){
        View view  = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(rnf!=null?R.layout.ap_list_item_check:R.layout.ap_list_item, root, false);
        TextView ssidTextView = (TextView) view.findViewById(R.id.ssidTextView);
        TextView moreInfoTextView = (TextView) view.findViewById(R.id.moreInfoTextView);
        GraphView graphView = (GraphView) view.findViewById(R.id.rssiGraphView);
        Guideline guideLine = (Guideline) view.findViewById(R.id.guideline);


        //ViewGroup.MarginLayoutParams ssidLP = (ViewGroup.MarginLayoutParams) ssidTextView.getLayoutParams();
        //ssidTextView.setWidth((int) (guideLine.getX() - ssidLP.leftMargin - ssidLP.rightMargin));
        ssidTextView.setText(accessPoint.getSSID());

        moreInfoTextView.setText(accessPoint.getBSSID() + " | " + accessPoint.getFrequency());

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(105);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);

        graphView.getViewport().setScalable(false);
        graphView.getViewport().setScalableY(false);
        graphView.getViewport().setScrollable(false);
        graphView.getViewport().setScrollableY(false);

        if(rnf!=null){
            ((CheckBox) view.findViewById(R.id.checkBox)).setOnCheckedChangeListener(new CheckBoxListener(accessPoint));
        }

        return view;
    }

    @Override
    public int getCount() {
        return apMap.size();
    }

    @Override
    public Object getItem(int i) {
        return getEntry(i).getKey();
    }

    private Map.Entry<AccessPoint, Integer> getEntry(int i) {
        Iterator<Map.Entry<AccessPoint, Integer>> iterator = apMap.entrySet().iterator();

        for (;i>0;i--) {
            //if(!iterator.hasNext()) throw new ArrayIndexOutOfBoundsException();
            iterator.next();
        }
        return iterator.next();
    }

    @Override
    public long getItemId(int i) {
        return getEntry(i).getValue();
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Map.Entry<AccessPoint, Integer> entry = getEntry(i);

        View view;

        try {
            view = viewList.get(entry.getValue());
        } catch (IndexOutOfBoundsException ex){
            view = generateView(entry.getKey(), viewGroup);
            LineGraphSeries<DataPoint> lgs = new LineGraphSeries<>( new DataPoint[]{});
            lgs.setBackgroundColor(Color.parseColor("#C859C5DA"));
            lgs.setDrawBackground(true);
            ((GraphView) view.findViewById(R.id.rssiGraphView)).addSeries(lgs);

            viewList.add(view);
            graphSeriesList.add(lgs);
            entry.setValue(viewList.size()-1);

        }

        if(rnf!=null){
            if(rnf.checkboxEnabled){
                ((CheckBox) view.findViewById(R.id.checkBox)).setEnabled(true);
            } else {
                ((CheckBox) view.findViewById(R.id.checkBox)).setEnabled(false);
                ignoreChange=true;
                ((CheckBox) view.findViewById(R.id.checkBox)).setChecked(false);
                ignoreChange=false;
            }
        }

        return view;
    }


    public void update(Collection<ScanResult> scanResults){
        if(startTime<0) startTime = System.currentTimeMillis();
        long timeDiv = (System.currentTimeMillis()-startTime)/1000;

        Collection<AccessPoint> aps = new TreeSet<>();

        for (ScanResult scanResult : scanResults){
            AccessPoint e = AccessPoint.generateOf(scanResult);
            aps.add(e);
            if(apMap.containsKey(e)){
                if (apMap.get(e)==-1){
                    Log.d(TAG, e.toString() + ": -1");
                    notifyDataSetChanged();
                }
                appendData(apMap.get(e), timeDiv, WifiUtils.qualityOfdBm(scanResult.level));
            } else {
                apMap.put(e, -1);
                notifyDataSetChanged();
            }
        }

        for (Map.Entry<AccessPoint, Integer> mapEntry : apMap.entrySet()){
            if(!aps.contains(mapEntry.getKey())){
                appendData(mapEntry.getValue(), timeDiv, 0);
            }
        }

        Log.d(TAG, "update");
    }

    private void appendData(int entryIndex, long x, int y) {
        graphSeriesList.get(entryIndex).appendData(new DataPoint(x, y), false, 100000           );
        ((GraphView) viewList.get(entryIndex).findViewById(R.id.rssiGraphView)).getViewport().setMaxX(x);
    }

    private boolean ignoreChange = false;

    private class CheckBoxListener implements CompoundButton.OnCheckedChangeListener{
        AccessPoint ap;

        private CheckBoxListener(AccessPoint ap){
            this.ap=ap;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //if(rnf==null || ignoreChange) return;
            //rnf.trackRecord.setApActive(ap, isChecked);
            //Log.d("CheckedboxListener", ap.getBSSID() + " " + isChecked);
        }
    }
}
