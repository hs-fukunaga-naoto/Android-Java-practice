package com.example.nfukunaga.androidtwitterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import twitter4j.ExtendedMediaEntity;

public class DisplayNumberChangeAdapter extends ArrayAdapter<Integer> {
    private int mResource;
    private List<Integer> mItems;
    private LayoutInflater mInflater;
    private Context context;
    public DisplayNumberChangeAdapter(Context context, int resource, List<Integer> items) {
        super(context, resource, items);
        this.context = context;
        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(mResource, null);
        }

        String item=mItems.get(position).toString();

        TextView importDisplayNumber =view.findViewById(R.id.display_number);
        importDisplayNumber.setText(item);
        return view;
    }
}
