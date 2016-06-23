package me.captivelabs.goliveit.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.data.API;
import me.captivelabs.goliveit.data.RetrofitAdapter;
import me.captivelabs.goliveit.models.AutoSuggestSearchResult;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.ui.adapters.UpcomingEventsAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpcomingEventsActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.popularRecycler)
    RecyclerView upcomingRecycler;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.tvSearchToolBar_title)
    TextView loadTitle;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.tvTryAgain)
    TextView tryAgain;
    UpcomingEventsAdapter upcomingEventsAdapter;
    ArrayList<Event> events = new ArrayList<>();

    private String artistName;

    @OnClick(R.id.tvTryAgain)
    void retry() {
        getEvents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        ButterKnife.bind(this);

        artistName = getIntent().getStringExtra("artist_name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(getResources().getColor(R.color.background));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("");

        upcomingEventsAdapter = new UpcomingEventsAdapter(events, getBaseContext());
        upcomingRecycler.setAdapter(upcomingEventsAdapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(upcomingEventsAdapter);
        upcomingRecycler.addItemDecoration(headersDecor);
        //   popularRecycler.addItemDecoration(new DividerDecoration(getBaseContext()));
        //getEvents();

        upcomingEventsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                headersDecor.invalidateHeaders();
            }
        });
        getEvents();
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
        Observable<AutoSuggestSearchResult> eventCallbackObservable = api.getUpcomingEvents(artistName);
        eventCallbackObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<AutoSuggestSearchResult>() {
                    @Override
                    public void onCompleted() {
                        imageView.setVisibility(ImageView.GONE);
                        progressBar.setVisibility(ProgressBar.GONE);
                        loadTitle.setVisibility(TextView.GONE);

                        Log.d("getting events...", "Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(UpcomingEventsActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();

                        String error = String.format("%s", "Error occurred!");
                        loadTitle.setText(error);
                        loadTitle.setVisibility(TextView.VISIBLE);
                        tryAgain.setVisibility(TextView.VISIBLE);
                        progressBar.setVisibility(ProgressBar.GONE);

                    }

                    @Override
                    public void onNext(AutoSuggestSearchResult eventCallback) {

                        for (int i = 0; i < eventCallback.getTotalEventsFound(); i++) {
                            Log.d("Event id", String.valueOf(eventCallback.getEvents().get(i).getId()));
                            events.add(eventCallback.getEvents().get(i));
                            //save(eventCallback.getEvents().get(i));
                        }
                        upcomingEventsAdapter.notifyDataSetChanged();
                    }
                });
    }
}
