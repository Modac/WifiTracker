package com.modac.wifitracker;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.modac.wifitracker.logic.Consumer;
import com.modac.wifitracker.logic.ScanReceiver;
import com.modac.wifitracker.logic.WifiUtils;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ApListFragment extends Fragment {

    ScanReceiver receiver;
    TextView apListView;


    public ApListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ap_list, container, false);
        apListView = (TextView) view.findViewById(R.id.apListView);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    public void updateApList(List<ScanResult> scanResults){
        apListView.setText(WifiUtils.formatScanResults(scanResults));
    }
}
