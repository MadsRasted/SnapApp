package com.example.snapapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.snapapp.R;
import com.example.snapapp.model.Snap;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Snap> items; // denne holder på data
    private LayoutInflater layoutInflater; // kan "inflate" layout filer. den kan tage XML filen og lave den om til et java objekt

    public MyAdapter(List<Snap> items, Context context) {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    // vi skal fortælle hvor mange rækker der skal være
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    //for hver række vil vi få et layout som vi bestemmer hvad der skal ske med
    public View getView(int i, View view, ViewGroup viewGroup) {
        //make layout .xml file first..

        // tjekker her om tilstanden er null
        if(view == null){
            view = layoutInflater.inflate(R.layout.myrow, null);
        }
        LinearLayout linearLayout = (LinearLayout)view;
        TextView textView = view.findViewById(R.id.textView1);
        if(textView != null) {
            textView.setText(items.get(i).getId()); //  vil forbinde til item listen senere
        }
        return linearLayout;
    }
}
