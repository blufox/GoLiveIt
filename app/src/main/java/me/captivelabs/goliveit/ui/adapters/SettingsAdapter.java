package me.captivelabs.goliveit.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.captivelabs.goliveit.R;

/**
 * Created by Aron on 10/30/2015.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

    public SettingsAdapter() {
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SettingsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        if (position == 0) {
            holder.settingsTitle.setText("Connected Music");
            holder.settingsSubtitle.setText("Spotify, Device Library, etc");
        }
        if (position == 1) {
            holder.settingsTitle.setText("Notifications");
            holder.settingsSubtitle.setText("Receive app notifications");
            holder.notificationSwitch.setVisibility(Switch.VISIBLE);
        }
        if (position == 2) {
            holder.settingsTitle.setText("Legal");
            holder.settingsSubtitle.setVisibility(TextView.GONE);
        }
        if (position == 3) {
            holder.settingsTitle.setText("Version");
            holder.settingsSubtitle.setText("1.0.dev - 1023152033");
        }
        holder.settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvSettings_title)
        TextView settingsTitle;
        @Bind(R.id.tvSettings_subtitle)
        TextView settingsSubtitle;
        @Bind(R.id.swNotification_switch)
        Switch notificationSwitch;
        @Bind(R.id.settingsLayout)
        RelativeLayout settingsLayout;

        public SettingsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
