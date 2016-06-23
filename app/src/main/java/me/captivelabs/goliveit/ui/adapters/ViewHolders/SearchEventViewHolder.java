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
import de.greenrobot.event.EventBus;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.events.FavoriteEvent;
import me.captivelabs.goliveit.ui.activities.EventDetailsActivity;

/**
 * Created by Aron on 11/6/2015.
 */
public class SearchEventViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.ivSearch_favorite)
    ImageView favoriteImage;
    @Bind(R.id.tvSearch_title)
    TextView searchTitle;
    @Bind(R.id.ivSearch_with_shows)
    ImageView searchWithShow;

    public SearchEventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void renderView(final Event event, final Context context) {
        searchTitle.setText(event.getName());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("event", event);
                context.startActivity(intent);
            }
        });
        favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!event.is_favorite()) {
                    // if (event.is_favorite()){
                    favoriteImage.setImageDrawable(context.getResources().getDrawable(R.drawable.starred_star));
                    favoriteImage.setTag("favorite");
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_LONG).show();
                    favoriteImage.setTag("favorite");
                    event.setIs_favorite(true);
                    favorite(event, context);
                } else {
                    favoriteImage.setImageDrawable(context.getResources().getDrawable(R.drawable.star_unstarred));
                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_LONG).show();
                    favoriteImage.setTag("unfavorite");
                    event.setIs_favorite(false);
                    unfavorite(event, context);
                }
                EventBus.getDefault().post(new FavoriteEvent(event));
            }
        });
    }

    public void favorite(Event venue, Context context) {
        NoSQLEntity<Event> entity = new NoSQLEntity<Event>("favorites", venue.getId() + "");
        entity.setData(venue);
        NoSQL.with(context).using(Event.class).save(entity);
    }

    public void unfavorite(Event event, Context context) {
        NoSQL.with(context).using(Event.class)
                .bucketId("favorites")
                .entityId(event.getId() + "")
                .delete();
    }

}
