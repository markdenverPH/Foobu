package com.example.babar.foobu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by babar on 3/10/2018.
 */

public class arrayAdapter extends ArrayAdapter<cards>{

    public arrayAdapter(Context context, int resource, List<cards> items) {
        super(context, resource, items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);


        TextView name = convertView.findViewById(R.id.profile_name);
        ImageView image = convertView.findViewById(R.id.profile_image);

        name.setText(card_item.getName());
        image.setImageResource(R.drawable.applogo);

        return convertView;
    }

}
