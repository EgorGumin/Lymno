package com.lymno.quest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Roman Belkov on 06.04.2015.
 *
 * The info was taken from http://hmkcode.com/android-simple-recyclerview-widget-example/
 * and http://www.101apps.co.za/index.php/articles/android-s-recyclerview-and-cardview-widgets.html
 */

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    private ArrayList<String> questsData; // these are the things we want to display

    public CardsAdapter(ArrayList<String> questsData) {
        this.questsData = questsData;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.txtViewTitle.setText(questsData.get(position));
        //viewHolder.imgViewIcon.setImageResource(questsData[position].getImageUrl());
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.recycler_item_title);
            //imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return questsData.size();
    }
}

