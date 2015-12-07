package com.material.android.sreeni.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by schalamcharla on 12/6/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    // what data do we need from activity
    // Context, data source

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // What our item looks like

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstagramPhoto photo = getItem(position);

        // Check if we r using the recycled view. if now, inflate it
        if (convertView == null) {
            // Create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,
                    parent,
                    false);
        }

        // Look up the view for populating the data
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);

        // Insert the model data into each of the view items
        tvCaption.setText(photo.caption);
        // Clear the image view
        //ivPhoto.setImageResource(0);
        // Insert the image using picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        // Return the created item as a view
        return convertView;
    }
}