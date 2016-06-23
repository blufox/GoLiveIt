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
import me.captivelabs.goliveit.ui.adapters.ViewHolders.RecommendedViewHolder;
import me.captivelabs.goliveit.utils.DataFormatter;

/*
 * Created by Aron on 10/31/2015.f
 */
public class RecommendedEventsInAdapter extends RecyclerView.Adapter<RecommendedViewHolder> implements StickyRecyclerHeadersAdapter<EventHeader> {
    private ArrayList<Event> events = new ArrayList<>();
    private Context context;

    public RecommendedEventsInAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @Override
    public RecommendedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecommendedViewHolder holder, int position) {
        holder.renderView(context, events.get(position));
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

    @Override
    public int getItemCount() {
        return events.size();
    }
}
