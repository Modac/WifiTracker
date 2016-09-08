package com.modac.wifitracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.modac.wifitracker.listeners.WhereButtonClickListener;


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
        Button whereButton = (Button) view.findViewById(R.id.button);
        whereButton.setOnClickListener(new WhereButtonClickListener((TextView) view.findViewById(R.id.textView2), whereButton));
        return view;
    }

}
