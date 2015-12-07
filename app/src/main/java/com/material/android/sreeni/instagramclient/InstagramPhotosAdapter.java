package com.material.android.sreeni.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;
import android.graphics.Color;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
        TextView tvCommentsCount = (TextView) convertView.findViewById(R.id.tvCommentsCount);
        // Insert the model data into each of the view items
        if (photo.caption == null) {
            tvCaption.setVisibility(View.INVISIBLE);
        } else {
            tvCaption.setText(photo.caption);
        }

        if (photo.commentsCount == 0) {
            tvCommentsCount.setVisibility(View.INVISIBLE);
        } else {
            tvCommentsCount.setText("view all " + photo.commentsCount + " comments");
        }
        tvAuthor.setText(photo.username);
        tvLikes.setText(photo.likesCount + " likes");
        // Clear the image view
        //ivPhoto.setImageResource(0);
        // Insert the image using picasso
        Picasso.with(getContext())
                .load(photo.imageUrl)
                .into(ivPhoto);

        //Profile pic
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.GRAY)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(photo.profilePicUrl)
                .fit()
                .centerInside()
                .transform(transformation)
                .into(ivProfilePic);

        //Picasso.with(getContext()).load(photo.profilePicUrl).fit().centerInside().into(ivProfilePic);

        // Return the created item as a view
        return convertView;
    }
}
