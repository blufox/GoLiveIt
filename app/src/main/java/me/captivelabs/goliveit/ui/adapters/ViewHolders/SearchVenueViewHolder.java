package me.captivelabs.goliveit.ui.adapters.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.models.Venue;
import me.captivelabs.goliveit.ui.activities.EventDetailsActivity;

/**
 * Created by Aron on 11/6/2015.
 */
public class SearchVenueViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.ivSearch_favorite)
    ImageView favoriteImage;
    @Bind(R.id.tvSearch_title)
    TextView searchTitle;
    @Bind(R.id.ivSearch_with_shows)
    ImageView searchWithShow;


    public SearchVenueViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void renderView(final Venue venue, final Context context) {
        searchTitle.setText(venue.getName());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("venue", venue);
                context.startActivity(intent);
            }
        });

        favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!venue.is_favorite()) {
                    // if (event.is_favorite()){
                    favoriteImage.setImageDrawable(context.getResources().getDrawable(R.drawable.starred_star));
                    favoriteImage.setTag("favorite");
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_LONG).show();
                    favoriteImage.setTag("favorite");
                    venue.setIs_favorite(true);
                    favorite(venue, context);
                } else {
                    favoriteImage.setImageDrawable(context.getResources().getDrawable(R.drawable.star_unstarred));
                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_LONG).show();
                    favoriteImage.setTag("unfavorite");
                    venue.setIs_favorite(false);
                    unfavorite(venue, context);
                }
              //  EventBus.getDefault().post(new FavoriteEvent());
            }
        });
    }
    public void favorite(Venue venue, Context context) {
        NoSQLEntity<Venue> entity = new NoSQLEntity<Venue>("my_venues", venue.getId() + "");
        entity.setData(venue);
        NoSQL.with(context).using(Venue.class).save(entity);
    }

    public void unfavorite(Venue venue, Context context) {
        NoSQL.with(context).using(Venue.class)
                .bucketId("my_venues")
                .entityId(venue.getId() + "")
                .delete();
    }

}
