package com.modac.wifitracker;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.modac.wifitracker.logic.Consumer;
import com.modac.wifitracker.logic.ScanReceiver;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ApListFragment extends Fragment {

    private ScanReceiver receiver;
    private ListView apListView;


    public ApListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ap_list, container, false);

        apListView = (ListView) view.findViewById(R.id.apListView);
        ApListAdapter adapter = new ApListAdapter(getContext());
        apListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onPause() {
        receiver.unregister();
        super.onPause();
    }

    @Override
    public void onResume() {

        updateApList(((WifiManager)getContext().getSystemService(Context.WIFI_SERVICE)).getScanResults());
        receiver = ScanReceiver.register((MainActivity)getActivity(), new Consumer<List<ScanResult>>() {
            @Override
            public void accept(List<ScanResult> a) {
                updateApList(a);
            }
        });
        super.onResume();
    }

    private void updateApList(List<ScanResult> scanResults){
        //apListView.setText(WifiUtils.formatScanResults(scanResults));
        //apListView.setBackgroundColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random()*255)));
        ((ApListAdapter)apListView.getAdapter()).update(scanResults);
    }
}
