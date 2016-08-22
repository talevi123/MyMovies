package com.tal.mymovies.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tal.mymovies.Moduls.NavDrawerItem;
import com.tal.mymovies.R;

import java.util.ArrayList;

/**
 * Created by tal on 17/08/16.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context,ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_item, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView counter = (TextView) convertView.findViewById(R.id.counter);

        icon.setImageResource(navDrawerItems.get(position).getIcon());
        title.setText(navDrawerItems.get(position).getTitle());

        if(navDrawerItems.get(position).getCounterVisibility()){
            counter.setText(navDrawerItems.get(position).getCount());
        }else{
            counter.setVisibility(View.GONE);
        }

        return convertView;
    }


}
