package me.captivelabs.goliveit.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.ui.adapters.ViewHolders.SearchEventViewHolder;

/**
 * Created by Aron on 11/6/2015.
 */
public class SearchEventAdapter extends RecyclerView.Adapter<SearchEventViewHolder> {
    private ArrayList<Event> events;
    private Context context;

    public SearchEventAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @Override
    public SearchEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchEventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_search_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchEventViewHolder holder, int position) {
        holder.renderView(events.get(position), context);

    }

    @Override
    public int getItemCount() {
        return events.size() == 0 ? 0 : events.size();
    }
}
