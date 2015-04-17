package com.lymno.quest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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

    private ArrayList<Quest> questsData; // these are the things we want to display

    public CardsAdapter(ArrayList<Quest> questsData) {
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

        Quest questsDataPos = questsData.get(position);

        viewHolder.QuestName.setText(questsDataPos.getName());
        viewHolder.QuestDescription.setText(questsDataPos.getDescription());
        //viewHolder.imgViewIcon.setImageResource(questsData[position].getImageUrl());
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView QuestName;
        public TextView QuestDescription;
        //public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            QuestName = (TextView) itemLayoutView.findViewById(R.id.recycler_quest_name);
            QuestDescription = (TextView) itemLayoutView.findViewById(R.id.recycler_quest_description);
            //imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent quest_info_intent = new Intent(context, QuestInfo.class);
            context.startActivity(quest_info_intent);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return questsData.size();
    }
}

