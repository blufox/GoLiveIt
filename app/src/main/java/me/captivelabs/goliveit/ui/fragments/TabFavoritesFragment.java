package me.captivelabs.goliveit.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.events.FavoriteEvent;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.ui.adapters.FavoritesAdapter;
import me.captivelabs.goliveit.utils.DividerDecoration;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFavoritesFragment extends Fragment {
    @Bind(R.id.popularRecycler)
    RecyclerView popularRecycler;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.tvSearchToolBar_title)
    TextView emptyLayoutIndicator;

    FavoritesAdapter favoritesAdapter;
    ArrayList<Event> events;


    public TabFavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_favorites, container, false);
        ButterKnife.bind(this, view);
        events = new ArrayList<>();
        getEvents();
        popularRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoritesAdapter = new FavoritesAdapter(events, getContext(), TabFavoritesFragment.this);
        popularRecycler.setAdapter(favoritesAdapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(favoritesAdapter);
        popularRecycler.addItemDecoration(headersDecor);
        popularRecycler.addItemDecoration(new DividerDecoration(getActivity()));


        favoritesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                headersDecor.invalidateHeaders();
            }
        });

        return view;
    }

    public void getEvents() {
        imageView.setVisibility(ImageView.VISIBLE);
        popularRecycler.setVisibility(ImageView.GONE);
        String error = String.format("%s", "Loading your events...");


        NoSQL.with(getActivity()).using(Event.class)
                .bucketId("favorites")
                .retrieve(new RetrievalCallback<Event>() {
                    @Override
                    public void retrievedResults(List<NoSQLEntity<Event>> entities) {
                        if (entities.size() > 0) {
                            events.clear();
                            for (NoSQLEntity<Event> entity : entities) {
                                Event event = entity.getData();
                                //order.setIs_accepted(true);
                                events.add(entity.getData());
                            }
                            favoritesAdapter.notifyDataSetChanged();
                            imageView.setVisibility(ImageView.GONE);

                            if (events.size() == 0) {
                                popularRecycler.setVisibility(View.GONE);
                            } else {
                                emptyLayoutIndicator.setVisibility(TextView.GONE);
                                popularRecycler.setVisibility(View.VISIBLE);
                            }

                        } else {
                            imageView.setVisibility(ImageView.VISIBLE);
                            String error = String.format("%s", "Loading your events...");

                        }

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    // This method will be called when a MessageEvent is posted
    public void onEventMainThread(FavoriteEvent event) {
        events.add(event.getEvent());
        favoritesAdapter.notifyDataSetChanged();
    }

    public void unFavorite(Context context, Event event) {
        NoSQL.with(context).using(Event.class)
                .bucketId("favorites")
                .entityId(event.getId() + "")
                .delete();
        getEvents();
        //favoritesAdapter.notifyDataSetChanged();
    }
}
