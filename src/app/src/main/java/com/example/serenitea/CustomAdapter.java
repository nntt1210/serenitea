package com.example.serenitea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int logos[];
    LayoutInflater inflater;
    public CustomAdapter(Context applicationContext, int[] logos) {
        this.context = applicationContext;
        this.logos = logos;
        inflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return logos.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_gridview, null); // inflate the layout
        ImageButton btn = (ImageButton) view.findViewById(R.id.btn); // get the reference of ImageView
        btn.setImageResource(logos[i]); // set logo images
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GridView)viewGroup).performItemClick(v,i,0);
            }
        });
        return view;
    }
}
