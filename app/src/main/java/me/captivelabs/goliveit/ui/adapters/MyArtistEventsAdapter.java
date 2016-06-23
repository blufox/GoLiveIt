package me.captivelabs.goliveit.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.ui.adapters.ViewHolders.EventHeader;
import me.captivelabs.goliveit.ui.adapters.ViewHolders.EventItemHolder;
import me.captivelabs.goliveit.utils.DataFormatter;

/**
 * Created by Aron on 10/26/2015.
 */
public class MyArtistEventsAdapter extends RecyclerView.Adapter<EventItemHolder> implements StickyRecyclerHeadersAdapter<EventHeader> {
    private ArrayList<Event> events;
    private Context context;

    public MyArtistEventsAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @Override
    public EventItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventItemHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.events_single_event, parent, false));
    }

    @Override
    public void onBindViewHolder(EventItemHolder holder, int position) {
        holder.renderView(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size() == 0 ? 0 : events.size();
    }

    @Override
    public long getHeaderId(int position) {
        return position;
    }

    @Override
    public EventHeader onCreateHeaderViewHolder(ViewGroup parent) {
        return new EventHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_sticky_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(EventHeader holder, int position) {
        String date = DataFormatter.formatDate(events.get(position).getEventDateLocal());
        holder.renderView(date);
    }
}
