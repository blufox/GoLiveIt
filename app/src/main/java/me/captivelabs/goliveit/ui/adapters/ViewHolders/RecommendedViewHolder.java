package me.captivelabs.goliveit.ui.adapters.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.ui.activities.EventDetailsActivity;
import me.captivelabs.goliveit.utils.DataFormatter;

/**
 * Created by Aron on 10/31/2015.
 */
public class RecommendedViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.ivRecommendedProfile)
    ImageView mRecommendedEventImage;
    @Bind(R.id.tvRecommendedEventTitle)
    TextView mRecommendedEventTitle;
    @Bind(R.id.tvRecommendedEventTime)
    TextView mRecommendedEventTime;
    @Bind(R.id.rlRecommendedEvent)
    RelativeLayout mRecommendedEventLayout;

    public RecommendedViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void renderView(final Context context, final Event event) {
        Picasso.with(context).load(event.getImageUrl()).placeholder(R.drawable.placeholderartist_small).into(mRecommendedEventImage);
        mRecommendedEventTitle.setText(event.getName());
        String time = DataFormatter.formatDate(event.getEventDateLocal());
        mRecommendedEventTime.setText(time);
        mRecommendedEventLayout.setOnClickListener(new View.OnClickListener() {
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
