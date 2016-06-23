package me.captivelabs.goliveit.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.models.Artist;
import me.captivelabs.goliveit.ui.adapters.ViewHolders.ArtistViewHolder;
import me.captivelabs.goliveit.ui.adapters.ViewHolders.EventHeader;

/**
 * Created by Aron on 10/29/2015.
 */
public class ArtistListAdapter extends RecyclerView.Adapter<ArtistViewHolder> implements StickyRecyclerHeadersAdapter<EventHeader> {
    private ArrayList<Artist> artists;
    private Context context;

    public ArtistListAdapter(ArrayList<Artist> artists, Context context) {
        this.artists = artists;
        this.context = context;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ArtistViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        holder.render(artists.get(position));
    }

    @Override
    public long getHeaderId(int position) {

        return getItem(position).charAt(0);

    }

    private CharSequence getItem(int position) {
        Artist artist = artists.get(position);

        return artist.getName();
    }


    @Override
    public EventHeader onCreateHeaderViewHolder(ViewGroup parent) {
        return new EventHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_sticky_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(EventHeader holder, int position) {
        holder.renderView(artists.get(position).getName().substring(0, 1));
    }

    @Override
    public int getItemCount() {
        return artists.size() == 0 ? 0 : artists.size();
    }
}
