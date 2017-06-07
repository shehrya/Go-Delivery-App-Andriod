package com.androidgroup.godelivery;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;


public class JobsListAdapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater = null;

    List<String> jobIDsList = new ArrayList<String>();

    List<String> productNamesList = new ArrayList<String>();
    List<String> distanceList = new ArrayList<String>();
    List<String> rateList = new ArrayList<String>();


    Typeface font;



    public JobsListAdapter(Context context, List<String> productNamesList, List<String> distanceList, List<String> rateList) {
        // UDO Auto-generated constructor stub
        this.context = context;
        this.productNamesList = productNamesList;
        this.distanceList = distanceList;
        this.rateList = rateList;
        font = Typeface.createFromAsset(context.getAssets(), "FancyFont_1.ttf");



        //this.jobIDsList = jobIDsList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return productNamesList.size();
    }

    @Override
    public Object getItem(int position) {
        //return data[position];
        return productNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);


        return vi;
    }

    public void updateReceiptsList(List<String> productNames, List<String> distance, List<String> rate) {

        productNamesList = productNames;
        distanceList = distance;
        rateList = rate;

        this.notifyDataSetChanged();
    }

}
