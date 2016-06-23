package me.captivelabs.goliveit.ui.adapters.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.models.Performer;
import me.captivelabs.goliveit.ui.activities.EventDetailsActivity;
import me.captivelabs.goliveit.ui.fragments.TabFavoritesFragment;
import me.captivelabs.goliveit.utils.DataFormatter;

/**
 * Created by Aron on 11/4/2015.
 */
public class FavoritesViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.ivEventsSingleEvent_favorite_image)
    ImageView ivFavorite;
    @Bind(R.id.ivEventImage)
    ImageView eventImage;
    @Bind(R.id.tvEventTitle)
    TextView eventTitle;
    @Bind(R.id.tvEventDesc)
    TextView eventDesc;
    @Bind(R.id.tvEventTime)
    TextView eventTime;
    @Bind(R.id.tvEventVenue)
    TextView eventVenue;
    @Bind(R.id.tvEventCity)
    TextView eventCity;
    @Bind(R.id.ivShare)
    ImageView shareEvent;

    View itemView;

    public FavoritesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemView = itemView;
    }

    public void renderView(@NonNull final Event event, final Context context, final TabFavoritesFragment favoriteEventListener) {
        eventTitle.setText(event.getName());
        eventDesc.setText(event.getVenue().getName());
        Picasso.with(context).load(event.getImageUrl()).placeholder(R.drawable.placeholder).into(eventImage);
        eventTime.setText(event.getEventDateLocal());
        eventVenue.setText(event.getVenue().getName());
        String time = DataFormatter.formatTime(event.getEventDateLocal());
        eventTime.setText(time);
        // eventCity.setText(event.getVenue().getCity());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("event", event);
                context.startActivity(intent);
            }
        });

        String performers = "";

        if (event.getPerformers() != null) {
            for (Performer performer : event.getPerformers()) {
                performers += performer.getName() + ", ";
            }
        }
        eventDesc.setText(performers);

        ivFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.starred_star));
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.is_favorite()) {
                    // if (event.is_favorite()){
                    favoriteEventListener.unFavorite(context, event);
                    ivFavorite.setTag("unfavorite");
                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_LONG).show();
                    ivFavorite.setTag("favorite");
                    event.setIs_favorite(false);
                    ivFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.star_unstarred));

                }
               // EventBus.getDefault().post(new FavoriteEvent());
            }
        });
    }
}
