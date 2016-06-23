package me.captivelabs.goliveit.ui.fragments;


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
import me.captivelabs.goliveit.data.callbacks.EventCallback;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.events.FavoriteEvent;
import me.captivelabs.goliveit.ui.adapters.AllEventsAdapter;
import me.captivelabs.goliveit.utils.DividerDecoration;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabAllEventsFragment extends Fragment {
    @Bind(R.id.popularRecycler)
    RecyclerView popularRecycler;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.tvSearchToolBar_title)
    TextView loadTitle;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.tvTryAgain)
    TextView tryAgain;
    AllEventsAdapter allEventsAdapter;
    ArrayList<Event> events;
    public TabAllEventsFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.tvTryAgain)
    void retry() {
        getEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_events, container, false);
        ButterKnife.bind(this, view);

        events = new ArrayList<>();
        //addDummyEvents();
        getEvents();

       // events();
        return view;
    }

    private void getEvents() {
        imageView.setVisibility(ImageView.VISIBLE);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        loadTitle.setVisibility(TextView.VISIBLE);
        tryAgain.setVisibility(TextView.GONE);

        String error = String.format("%s", "Loading your events...");
        loadTitle.setText(error);

        //API
        API api = RetrofitAdapter.createAPI();
        Observable<EventCallback> eventCallbackObservable = api.getEvents();
        eventCallbackObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .filter(new Func1<EventCallback, Boolean>() {
                    @Override
                    public Boolean call(EventCallback eventCallback) {
                        return eventCallback.getLimit() != 0;
                    }
                })
                .subscribe(new Subscriber<EventCallback>() {
                    @Override
                    public void onCompleted() {
                        imageView.setVisibility(ImageView.GONE);
                        progressBar.setVisibility(ProgressBar.GONE);
                        loadTitle.setVisibility(TextView.GONE);

                        Log.d("getting events...", "Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  Toast.makeText(getActivity(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("Allevents Frag error", e.toString());
                        String error = String.format("%s", "Error occurred!");
                        loadTitle.setText(error);
                        loadTitle.setVisibility(TextView.VISIBLE);
                        tryAgain.setVisibility(TextView.VISIBLE);
                        progressBar.setVisibility(ProgressBar.GONE);

                    }

                    @Override
                    public void onNext(EventCallback eventCallback) {

                        events = eventCallback.getEvents();
                        popularRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        allEventsAdapter = new AllEventsAdapter(events, getContext());
                        popularRecycler.setAdapter(allEventsAdapter);
                        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(allEventsAdapter);
                        popularRecycler.addItemDecoration(headersDecor);
                        popularRecycler.addItemDecoration(new DividerDecoration(getActivity()));
                        allEventsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onChanged() {
                                super.onChanged();
                                headersDecor.invalidateHeaders();
                            }
                        });
                    }
                });
    }

    public void save(Event event) {
        NoSQLEntity<Event> entity = new NoSQLEntity<Event>("events", event.getId() + "");
        entity.setData(event);
        NoSQL.with(getActivity()).using(Event.class).save(entity);
    }

    public void events() {
        imageView.setVisibility(ImageView.VISIBLE);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        loadTitle.setVisibility(TextView.VISIBLE);
        tryAgain.setVisibility(TextView.GONE);
        NoSQL.with(getActivity()).using(Event.class)
                .bucketId("events")
                .retrieve(new RetrievalCallback<Event>() {
                    @Override
                    public void retrievedResults(List<NoSQLEntity<Event>> entities) {
                        if (entities.size() > 0) {
                            events.clear();
                            for (NoSQLEntity<Event> entity : entities) {
                                Event event = entity.getData();
                                events.add(entity.getData());
                            }
                            allEventsAdapter.notifyDataSetChanged();
                            //getEvents();
                        }

                        imageView.setVisibility(ImageView.GONE);
                        progressBar.setVisibility(ProgressBar.GONE);
                        loadTitle.setVisibility(TextView.GONE);
                        tryAgain.setVisibility(TextView.GONE);
                    }
                });
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
