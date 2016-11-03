package com.modac.wifitracker;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.constraint.Guideline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.modac.wifitracker.logic.AccessPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pascal Goldbrunner
 */

public class ApListAdapter extends BaseAdapter {

    private Context context;
    private List<View> viewList;
    private Map<AccessPoint, Integer> apMap;
    private List<LineGraphSeries<DataPoint>> graphSeriesList;
    private long startTime = -1;

    public ApListAdapter(Context context, Collection<ScanResult> apCollection){
        this.context=context;
        this.viewList = new ArrayList<>();
        this.apMap = new LinkedHashMap<>();
        this.graphSeriesList = new ArrayList<>();

        for ( ScanResult sr : apCollection){
            apMap.put(AccessPoint.generateOf(sr), -1);
        }
    }

    private View generateView(AccessPoint accessPoint, ViewGroup root){
        View view  = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ap_list_item, root, false);
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

        graphView.getViewport().setScalable(false);
        graphView.getViewport().setScalableY(false);
        graphView.getViewport().setScrollable(false);
        graphView.getViewport().setScrollableY(false);


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
            ((GraphView) view.findViewById(R.id.rssiGraphView)).addSeries(lgs);

            viewList.add(view);
            graphSeriesList.add(lgs);
            entry.setValue(viewList.size()-1);

        }

        return view;
    }

    public void update(Collection<ScanResult> scanResults){
        if(startTime<0) startTime = System.currentTimeMillis();
        long timeDiv = (System.currentTimeMillis()-startTime)/1000;

        Collection<AccessPoint> aps = new HashSet<>(scanResults.size());

        for (ScanResult scanResult : scanResults){
            AccessPoint e = AccessPoint.generateOf(scanResult);
            aps.add(e);
            if(apMap.keySet().contains(e)){
                graphSeriesList.get(apMap.get(e)).appendData(new DataPoint(timeDiv, scanResult.level), true, 100);
            } else {
                apMap.put(e, -1);
                notifyDataSetChanged();
            }
        }

        for (Map.Entry<AccessPoint, Integer> mapEntry : apMap.entrySet()){
            if()
        }
    }
}
