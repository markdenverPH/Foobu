package com.example.babar.foobu.Matches;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.babar.foobu.R;

/**
 * Created by babar on 3/17/2018.
 */

public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView matchid;
    public MatchesViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        matchid = itemView.findViewById(R.id.matchid);
    }

    @Override
    public void onClick(View view) {

    }
}
