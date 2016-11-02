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

import java.util.ArrayList;
import java.util.Collection;
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
    private Map<ScanResult, Integer> apMap;
    private List<LineGraphSeries<DataPoint>> graphSeriesList;

    public ApListAdapter(Context context, Collection<ScanResult> apCollection){
        this.context=context;
        this.viewList = new ArrayList<>();
        this.apMap = new LinkedHashMap<>();
        this.graphSeriesList = new ArrayList<>();

        for ( ScanResult sr : apCollection){
            //sr.level=1;         // value level normally never reaches 1 so itss the value for undefinded
            apMap.put(sr, 1);
        }
    }

    private View generateView(ScanResult scanResult, ViewGroup root){
        View view  = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ap_list_item, root, false);
        TextView ssidTextView = (TextView) view.findViewById(R.id.ssidTextView);
        TextView moreInfoTextView = (TextView) view.findViewById(R.id.moreInfoTextView);
        GraphView graphView = (GraphView) view.findViewById(R.id.rssiGraphView);
        Guideline guideLine = (Guideline) view.findViewById(R.id.guideline);


        //ViewGroup.MarginLayoutParams ssidLP = (ViewGroup.MarginLayoutParams) ssidTextView.getLayoutParams();
        //ssidTextView.setWidth((int) (guideLine.getX() - ssidLP.leftMargin - ssidLP.rightMargin));
        ssidTextView.setText(scanResult.SSID);

        moreInfoTextView.setText(scanResult.BSSID + " | " + scanResult.frequency);

        return view;
    }

    @Override
    public int getCount() {
        return apMap.size();
    }

    @Override
    public Object getItem(int i) {
        Iterator<ScanResult> iterator = apMap.keySet().iterator();

        for (;i>0;i--) {
            //if(!iterator.hasNext()) throw new ArrayIndexOutOfBoundsException();
            iterator.next();
        }
        return iterator.next();
    }

    private Map.Entry<ScanResult, Integer> getEntry(int i) {
        Iterator<Map.Entry<ScanResult, Integer>> iterator = apMap.entrySet().iterator();

        for (;i>0;i--) {
            //if(!iterator.hasNext()) throw new ArrayIndexOutOfBoundsException();
            iterator.next();
        }
        return iterator.next();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Map.Entry<ScanResult, Integer> entry = getEntry(i);

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
}
