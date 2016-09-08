package com.modac.wifitracker;

import android.content.Context;
import android.media.Image;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.modac.wifitracker.logic.Tools;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Keller2 on 25.06.2016.
 */
public class ImageAdapter extends BaseAdapter {
    private String TAG = getClass().getSimpleName();

    private final Context context;
    private int resource;
    private int imageViewRes;
    private List<Integer> imageResIds;
    private int textViewRes;
    private List<String> lines;

    public ImageAdapter(Context context, @LayoutRes int resource, @IdRes int imageViewRes, int[] imageResIds, @IdRes int textViewRes, String[] lines){
        if (imageResIds.length!=lines.length) throw new InvalidParameterException("Both arrays must have equal amount of items.");

        this.context=context;
        this.resource=resource;
        this.imageViewRes=imageViewRes;
        this.imageResIds = Tools.asList(imageResIds);
        this.textViewRes=textViewRes;
        this.lines = Arrays.asList(lines);

    }

    @Override
    public int getCount() {
        return imageResIds.size();
    }

    @Override
    public Entry getItem(int position) {
        return new Entry(imageResIds.get(position), lines.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Entry e = getItem(position);

        View view;

        if(convertView==null){
            view  = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        ImageView imageView;
        TextView textView;

        try{
            imageView = (ImageView) view.findViewById(imageViewRes);
            textView = (TextView) view.findViewById(textViewRes);
        } catch (ClassCastException ex){
            Log.e(TAG, "View ids must be ids of ImageView and TextView");
            throw new IllegalStateException("View ids must be ids of ImageView and TextView");
        }

        imageView.setImageResource(e.getImageId());
        textView.setText(e.getLine());

        return view;
    }

    private static class Entry {
        private int imageId;
        private String line;

        public Entry(int imageId, String line){
            this.imageId=imageId;
            this.line=line;
        }

        public int getImageId(){
            return imageId;
        }

        public String getLine() {
            return line;
        }
    }
}
