package me.captivelabs.goliveit.ui.adapters.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.ui.activities.EventDetailsActivity;
import me.captivelabs.goliveit.utils.DataFormatter;

/**
 * Created by Aron on 10/31/2015.
 */
public class UpcomingViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tvUpcoming_event_date)
    TextView mUpcomingEventDate;
    @Bind(R.id.tvUpcoming_event_title)
    TextView mUpcomingEventTitle;
    @Bind(R.id.tvUpcoming_event_venue)
    TextView mUpcomingEventVenue;
    @Bind(R.id.rlUpcomingEvent)
    RelativeLayout mUpcomingEventlayout;

    public UpcomingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void renderView(final Context context, final Event event) {
        mUpcomingEventTitle.setText(event.getName());
        mUpcomingEventVenue.setText(event.getVenue().getName());
        mUpcomingEventDate.setText(DataFormatter.formatDate(event.getEventDateLocal()));
        mUpcomingEventlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("event", event);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
}
