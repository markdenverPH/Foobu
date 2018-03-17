package com.example.babar.foobu.Matches;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.babar.foobu.R;

import java.util.List;

/**
 * Created by babar on 3/17/2018.
 */

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolder> {
    private List<MatchesObject> matcheslist;
    private Context context;

    public MatchesAdapter(List<MatchesObject> matchesList, Context context){
        this.matcheslist = matchesList;
        this.context = context;
    }

    @Override
    public MatchesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutview.setLayoutParams(lp);
        MatchesViewHolder rcv = new MatchesViewHolder(layoutview);

        return null;
    }

    @Override
    public void onBindViewHolder(MatchesViewHolder holder, int position) {
holder.matchid.setText(matcheslist.get(position).getUserId());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
