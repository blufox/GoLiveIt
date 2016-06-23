package me.captivelabs.goliveit.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.data.API;
import me.captivelabs.goliveit.data.RetrofitAdapter;
import me.captivelabs.goliveit.models.Artist;
import me.captivelabs.goliveit.models.AutoSuggestSearchResult;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.events.FavoriteEvent;
import me.captivelabs.goliveit.ui.activities.ManageMyArtistActivity;
import me.captivelabs.goliveit.ui.adapters.MyArtistEventsAdapter;
import me.captivelabs.goliveit.utils.DividerDecoration;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabArtistsFragment extends Fragment {
    @Bind(R.id.artistRecycler)
    RecyclerView popularRecycler;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.tvSearchToolBar_title)
    TextView loadTitle;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.tvTryAgain)
    TextView tryAgain;
    ArrayList<Event> events;
    ArrayList<Artist> artists;
    ArrayList<String> names;
    MyArtistEventsAdapter myArtistEventsAdapter;
    private boolean areEventsLoaded = false;

    public TabArtistsFragment() {
    }

    //boolean

    @OnClick(R.id.tvTryAgain)
    void goTo() {
        startActivity(new Intent(getActivity(), ManageMyArtistActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_artists, container, false);
        ButterKnife.bind(this, view);

        events = new ArrayList<>();
        artists = new ArrayList<>();
        names = new ArrayList<>();

        popularRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        myArtistEventsAdapter = new MyArtistEventsAdapter(events, getContext());
        popularRecycler.setAdapter(myArtistEventsAdapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(myArtistEventsAdapter);
        popularRecycler.addItemDecoration(headersDecor);
        popularRecycler.addItemDecoration(new DividerDecoration(getActivity()));
        myArtistEventsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                headersDecor.invalidateHeaders();
            }
        });

        favoriteArtists();

        return view;
    }

    private void setUpEmptyEvents() {
        imageView.setVisibility(ImageView.VISIBLE);
        progressBar.setVisibility(ProgressBar.GONE);
        String info = String.format("%s", "Your artists doesnt have events at the moment");
        loadTitle.setText(info);
        loadTitle.setVisibility(TextView.VISIBLE);
        tryAgain.setVisibility(TextView.VISIBLE);

        String more = String.format("%s", "Find more artists");
        tryAgain.setText(more);
        popularRecycler.setVisibility(RecyclerView.GONE);
    }

    private void getEvents(final String name) {
        imageView.setVisibility(ImageView.VISIBLE);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        loadTitle.setVisibility(TextView.VISIBLE);
        tryAgain.setVisibility(TextView.GONE);

        String info = String.format("%s", "Loading your events...");
        loadTitle.setText(info);
        popularRecycler.setVisibility(RecyclerView.GONE);

        //API
        API api = RetrofitAdapter.createAPI();
        Observable<AutoSuggestSearchResult> eventCallbackObservable = api.getMyArtistEvents2(name);
        eventCallbackObservable
                .distinct()
                .take(20)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<AutoSuggestSearchResult>() {
                    @Override
                    public void onCompleted() {
                        imageView.setVisibility(ImageView.GONE);
                        progressBar.setVisibility(ProgressBar.GONE);
                        loadTitle.setVisibility(TextView.GONE);
                        popularRecycler.setVisibility(RecyclerView.VISIBLE);
                        Log.d("getting my events...", "Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        String error = String.format("%s", "Error occurred!");
                        Log.e("artist Fragment error:", e.toString());
                        loadTitle.setText(error);
                        imageView.setVisibility(ImageView.VISIBLE);
                        tryAgain.setVisibility(TextView.VISIBLE);
                        tryAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getEvents(name);
                            }
                        });
                        progressBar.setVisibility(ProgressBar.GONE);
                        popularRecycler.setVisibility(RecyclerView.GONE);

                    }

                    @Override
                    public void onNext(AutoSuggestSearchResult eventCallback) {
                        if (eventCallback.getTotalEventsFound() > 0) {
                            loadTitle.setVisibility(TextView.GONE);
                            imageView.setVisibility(ImageView.GONE);
                            tryAgain.setVisibility(TextView.GONE);
                            progressBar.setVisibility(ProgressBar.GONE);
                            popularRecycler.setVisibility(RecyclerView.VISIBLE);

                            for (int i = 0; i < eventCallback.getTotalEventsFound(); i++) {
                                events.add(eventCallback.getEvents().get(i));
                                Log.d("My artist events: ", eventCallback.getEvents().get(i).getName());

                            }
                            myArtistEventsAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("My artist events: ", "No events for my artists");
                        }
                    }
                });
    }

    public void save(Event event) {
        NoSQLEntity<Event> entity = new NoSQLEntity<Event>("events", event.getId() + "");
        entity.setData(event);
        NoSQL.with(getActivity()).using(Event.class).save(entity);
    }

    public void favoriteArtists() {
        NoSQL.with(getActivity()).using(Artist.class)
                .bucketId("my_artists")
                .retrieve(new RetrievalCallback<Artist>() {
                    @Override
                    public void retrievedResults(List<NoSQLEntity<Artist>> entities) {
                        if (entities.size() > 0) {
                            areEventsLoaded = true;
                            //artists.clear();
                            for (NoSQLEntity<Artist> entity : entities) {
                                Artist artist = entity.getData();
                                artists.add(artist);
                                names.add(artist.getName());
                                Log.d("My artist list", artist.getName());
                                getEvents(artist.getName());
                            }
                        } else {
                            setUpEmptyEvents();
                        }

                    }
                });
    }

    private ArrayList<String> getArtistNames() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < artists.size(); i++) {
            names.add(artists.get(i).getName());
        }
        return names;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // This method will be called when a MessageEvent is posted
    public void onEvent(FavoriteEvent event) {
        //events();
    }


}
