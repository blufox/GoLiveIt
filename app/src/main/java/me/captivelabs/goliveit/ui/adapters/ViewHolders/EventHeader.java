package me.captivelabs.goliveit.ui.adapters.ViewHolders;

/**
 * Created by Gilbert on 28/10/2015.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.captivelabs.goliveit.R;

public class EventHeader extends RecyclerView.ViewHolder {
    @Bind(R.id.dateHeader)
    TextView dateHeader;

    public EventHeader(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void renderView(String header_text){
        dateHeader.setText(header_text);
    }
}
