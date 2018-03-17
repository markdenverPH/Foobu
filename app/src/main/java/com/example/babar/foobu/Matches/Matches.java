package com.example.babar.foobu.Matches;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.babar.foobu.R;

import java.util.ArrayList;
import java.util.List;

public class Matches extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter matchesadapter;
    private RecyclerView.LayoutManager matcheslayoutmanager;
    private ArrayList<MatchesObject> dataSetMatches = new ArrayList<MatchesObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        matcheslayoutmanager = new LinearLayoutManager(Matches.this);
        recyclerView.setLayoutManager(matcheslayoutmanager);
        matchesadapter = new MatchesAdapter(getDataSetMatches(), Matches.this);
        recyclerView.setAdapter(matchesadapter);

        for (int i = 0;i<100;i++){
            MatchesObject obj = new MatchesObject(Integer.toString(i));
            dataSetMatches.add(obj);
        }

        matchesadapter.notifyDataSetChanged();
    }

    public List<MatchesObject> getDataSetMatches() {
        return dataSetMatches;
    }
}
