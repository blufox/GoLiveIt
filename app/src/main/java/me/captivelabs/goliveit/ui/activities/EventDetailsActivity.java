package me.captivelabs.goliveit.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
import me.captivelabs.goliveit.models.Performer;
import me.captivelabs.goliveit.models.Venue;
import me.captivelabs.goliveit.ui.adapters.RecommendedEventsInAdapter;
import me.captivelabs.goliveit.utils.DataFormatter;
import me.captivelabs.goliveit.utils.Functions;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventDetailsActivity extends AppCompatActivity {
    @Bind(R.id.pbRecommendedProgress)
    ProgressBar mRecommendedProgress;

    @Bind(R.id.rvRecommendedRecycler)
    RecyclerView mRecommendedRecyclerList;
    RecommendedEventsInAdapter recommendedEventsAdapter;
    @Bind(R.id.tvRecommendation_empty_indicator)
    TextView emptyRecommendedEventsIndicator;
    @Bind(R.id.llRecommended_show_more)
    LinearLayout showMoreRecommendedEvents;

    @Bind(R.id.llEventDetails_recommendations)
    LinearLayout llRecommendations;

    @Bind(R.id.llEventDetails_artists)
    LinearLayout llArtists;

    @Bind(R.id.tvActivity_event_details_event_name)
    TextView tvEventName;

    @Bind(R.id.ivActivity_event_details_event_image)
    ImageView ivEventImage;

    @Bind(R.id.tvActivity_event_details_performers)
    TextView tvEventPerformers;

    @Bind(R.id.tvEvent_details_event_date)
    TextView tvDate;

    @Bind(R.id.tvEvent_details_event_time)
    TextView tvTime;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.rlTicket_layout)
    RelativeLayout ticketlayout;
    @Bind(R.id.tvEvent_details_ticket_name)
    TextView ticketSite;
    @Bind(R.id.pbEvent_loading)
    ProgressBar eventLoadingBar;
    @Bind(R.id.tvEvent_details_ticket_amount)
    TextView ticketAmount;
    String ticketUrl;
    Event event;
    Artist artist;
    ArrayList<Event> recommendedEvents;
    boolean moreRecommendedEvents = false;

    @OnClick(R.id.llRecommended_show_more)
    void showMoreRecommendedEvents() {

    }

    @OnClick(R.id.llVenue_details)
    void goToVenuePage() {
        if (event != null && artist != null) {
            Intent intent = new Intent(EventDetailsActivity.this, VenueActivity.class);
            intent.putExtra("event", event);
            intent.putExtra("artist", artist);
            startActivity(intent);
        }
    }

    @OnClick(R.id.ivEvent_details_venue_image)
    void openVenueMap() {
        if (event != null && artist != null) {
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", event.getVenue().getLatitude(), event.getVenue().getLongitude());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
    }

    @OnClick(R.id.llEvent_details_performer_layout)
    void goToArtistPage() {
        if (artist != null) {
            Intent intent = new Intent(EventDetailsActivity.this, ArtistActivity.class);
            intent.putExtra("artist", artist);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        event = (Event) getIntent().getSerializableExtra("event");

        ButterKnife.bind(this);

        setupRecommendedEvents();

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (event != null) {
            collapsingToolbarLayout.setTitle(event.getName());
        }
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        // toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        llRecommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RecommendedEventActivity.class);

                if (event != null) {
                    intent.putExtra("event", event.getName());
                }
                startActivity(intent);
            }
        });

        llArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ArtistActivity.class);
                startActivity(intent);
            }
        });

        setUpPage();

    }


    public void setUpPage() {
        if (event != null) {
            setUpHeader();
            setUpDateAndTime();
            setUpPerformers();
            setUpVenue();
        } else {
            Toast.makeText(getBaseContext(), "Event object is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpTicket() {


    }

    private void setUpVenue() {
        TextView tvName = (TextView) findViewById(R.id.tvEvent_details_venue_name);
        TextView tvAddress = (TextView) findViewById(R.id.tvEvent_details_venue_address);
        TextView tvCityAndState = (TextView) findViewById(R.id.tvEvent_details_venue_city_and_state);
        TextView tvDistance = (TextView) findViewById(R.id.tvEvent_details_venue_distance);

        TextView tvVenuePge = (TextView) findViewById(R.id.tvEvent_details_venue_more_details);

        ImageView ivMapImage = (ImageView) findViewById(R.id.ivEvent_details_venue_image);

        final Venue venue = event.getVenue();

        tvName.setText(venue.getName());
        if(venue.getAddress1() != null) {
            tvAddress.setText(venue.getAddress1());
        }else{
            tvAddress.setVisibility(View.GONE);
        }
        tvCityAndState.setText(venue.getCity() + ", " + venue.getState() + ", " + venue.getCountry());
        tvDistance.setText((float) Math.round(event.getDistance()) + " km");

        tvVenuePge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), VenueActivity.class);
                intent.putExtra("venue", venue);
                intent.putExtra("event", event);

                startActivity(intent);
            }
        });

        Picasso.with(this).load(Functions.deriveVenueImage(venue.getLatitude(), venue.getLongitude())).into(ivMapImage);
    }

    private void setUpPerformers() {
        LayoutInflater mInflater = LayoutInflater.from(getBaseContext());
        LinearLayout llPerformers = (LinearLayout) findViewById(R.id.llEvent_details_performers_layout);
        LinearLayout llPerformer = (LinearLayout) findViewById(R.id.llEvent_details_performer_layout);

        if (event.getPerformers() != null) {
            if (event.getPerformers().size() == 1) {
                llPerformer.setVisibility(View.VISIBLE);
                CircularImageView civPerformerPic = (CircularImageView) findViewById(R.id.civEvent_details_performer_image);
                TextView tvSingleArtistName = (TextView) findViewById(R.id.tvEvent_details_performer_name);
                TextView tvArtistDescription = (TextView) findViewById(R.id.tvEvent_details_performer_description);

                Performer performer = event.getPerformers().get(0);
                if (performer.getImages() != null) {
                    if (performer.getImages().get(0) != null) {
                        Picasso.with(this).load(performer.getImages().get(0).getUrl()).into(civPerformerPic);
                    }
                }

                tvSingleArtistName.setText(performer.getName());
                // tvSingleArtistName.setText(performer.get); //will do description from artist object
                getArtist(performer.getId(), tvSingleArtistName, tvArtistDescription, civPerformerPic);

            } else {
                llPerformer.setVisibility(View.GONE);
                for (Performer performer : event.getPerformers()) {
                    View view = mInflater.inflate(R.layout.event_details_performers_single_performer, null);
                    CircularImageView civArtistPic = (CircularImageView) view.findViewById(R.id.civEvent_details_performers_single_performer_image);
                    TextView tvArtistName = (TextView) view.findViewById(R.id.tvEvent_details_performers_single_performer_name);
                    if (performer.getImages() != null) {
                        if (performer.getImages().get(0) != null) {
                            Picasso.with(this).load(performer.getImages().get(0).getUrl()).into(civArtistPic);
                        }
                    }
                    getArtist(performer.getId(), tvArtistName, civArtistPic);
                    tvArtistName.setText(performer.getName());
                    llPerformers.addView(view);
                }
            }
        }
    }

    private void getArtist(int id, final TextView tvArtistName, final TextView tvArtistDescription, final CircularImageView civArtistPic) {
        API api = RetrofitAdapter.createAPI();
        Observable<Artist> artistObservable = api.getArtist(id);
        artistObservable
                .observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.newThread()).
                subscribe(new Subscriber<Artist>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Artist artistt) {
                        artist = artistt;
                        tvArtistName.setText(artist.getName());
                        save(artist);

                        tvArtistDescription.setText(Html.fromHtml(artist.getDescription()));
                        if (artist.getImages() != null) {
                            if (artist.getImages().size() > 0) {
                                Picasso.with(getBaseContext()).load(artist.getImages().get(0).getUrl()).into(civArtistPic);
                            }
                        }

                    }
                });
    }

    private void getArtist(int id, final TextView tvArtistName, final CircularImageView civArtistPic) {
        API api = RetrofitAdapter.createAPI();
        Observable<Artist> artistObservable = api.getArtist(id);
        artistObservable
                .take(3)
                .observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.newThread()).
                subscribe(new Subscriber<Artist>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final Artist artist) {
                        tvArtistName.setText(artist.getName());
                        save(artist);

                        if (artist.getImages() != null) {
                            if (artist.getImages().size() > 0) {
                                Picasso.with(getBaseContext()).load(artist.getImages().get(0).getUrl()).into(civArtistPic);
                            }
                        }

                        tvArtistName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getBaseContext(), ArtistActivity.class);
                                intent.putExtra("artist", artist);
                                startActivity(intent);
                            }
                        });

                        civArtistPic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getBaseContext(), ArtistActivity.class);
                                intent.putExtra("artist", artist);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }


    private void setUpDateAndTime() {
        Date date = DataFormatter.DateFormatter(event.getEventDateLocal());
        if (date != null) {
            Calendar cal = Calendar.getInstance(); //might want to do this once
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            String am_pm = "PM";

            String formattedDate = String.format("%s, %d %s ", DataFormatter.getDayOfWeek(day_of_week), day, DataFormatter.getMonth(month));
            tvDate.setText(formattedDate);

            String formattedTime = String.format("%02d:%02d %s", hour, minute, am_pm);
            tvTime.setText(formattedTime);
        }

    }

    private void setUpHeader() {
        tvEventName.setText(event.getName());
        String performers = "";

        if (event.getPerformers() != null) {
            for (Performer performer : event.getPerformers()) {
                performers += performer.getName() + ", ";
            }
        }
        tvEventPerformers.setText(performers);

        Picasso.with(this).load(event.getImageUrl()).into(ivEventImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        MenuItem fav = menu.findItem(R.id.event_favorite);
        if (event != null && event.is_favorite()) {
            fav.setIcon(R.drawable.starred_star);
        } else {
            fav.setIcon(R.drawable.star_unstarred);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.event_favorite) {
            //favorite an event
            if (event != null && !event.is_favorite()) {
                item.setIcon(R.drawable.starred_star);
                event.setIs_favorite(true);
                Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_LONG).show();
                favorite(event);
                saveEvent(event);
            } else {
                item.setIcon(R.drawable.star_unstarred);
                event.setIs_favorite(false);
                Toast.makeText(getApplicationContext(), "Removed from Favorites", Toast.LENGTH_LONG).show();
                favorite(event);
            }
            EventBus.getDefault().post(new FavoriteEvent(event));
            return true;
        }
        if (id == R.id.event_share) {
            String body = "Check out "
                    + event.getName()
                    + " on "
                    + DataFormatter.formatDate(event.getEventDateLocal())
                    + " "
                    + event.getVenue().getUrl();
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(intent, "Share event"));
        }

        return super.onOptionsItemSelected(item);
    }

    public void save(Artist event) {
        NoSQLEntity<Artist> entity = new NoSQLEntity<Artist>("my_artists", event.getId() + "");
        entity.setData(event);
        NoSQL.with(getBaseContext()).using(Artist.class).save(entity);
    }

    public void saveEvent(Event event) {
        NoSQLEntity<Event> entity = new NoSQLEntity<Event>("events", event.getId() + "");
        entity.setData(event);
        NoSQL.with(getBaseContext()).using(Event.class).save(entity);
    }

    private void setupRecommendedEvents() {
        recommendedEvents = new ArrayList<>();
        mRecommendedRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        recommendedEventsAdapter = new RecommendedEventsInAdapter(recommendedEvents, getApplicationContext());
        mRecommendedRecyclerList.setAdapter(recommendedEventsAdapter);
        if (event != null) {
            Log.d("Recommended name", event.getName());
            getRecommendEvents();
        }
        showMoreRecommendedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailsActivity.this, RecommendedEventActivity.class);
                if (event.getName() != null) {
                    intent.putExtra("eventName", event.getName());
                }
                startActivity(intent);
            }
        });
    }

    private void getRecommendEvents() {
        mRecommendedProgress.setVisibility(ProgressBar.VISIBLE);
        mRecommendedRecyclerList.setVisibility(RecyclerView.VISIBLE);
        emptyRecommendedEventsIndicator.setVisibility(TextView.GONE);
        API api = RetrofitAdapter.createAPI();
        Observable<AutoSuggestSearchResult> events = api.autoSuggestEvent(event.getName());
        events.observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.newThread())
                .distinct().
                subscribe(new Subscriber<AutoSuggestSearchResult>() {
                    @Override
                    public void onCompleted() {
                        mRecommendedProgress.setVisibility(ProgressBar.GONE);
                        emptyRecommendedEventsIndicator.setVisibility(TextView.GONE);
                        mRecommendedRecyclerList.setVisibility(RecyclerView.VISIBLE);
                        Log.d("Recommended complete", "Loaded successfully");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRecommendedProgress.setVisibility(ProgressBar.GONE);
                        mRecommendedRecyclerList.setVisibility(RecyclerView.GONE);
                        emptyRecommendedEventsIndicator.setVisibility(TextView.VISIBLE);
                        emptyRecommendedEventsIndicator.setText("Something went wrong");
                        Log.e("Recommended Error: ", e.toString());
                    }

                    @Override
                    public void onNext(AutoSuggestSearchResult eventCallback) {
                        Log.d("Recommended number", "" + eventCallback.getTotalEventsFound());
                        mRecommendedProgress.setVisibility(ProgressBar.VISIBLE);
                        mRecommendedRecyclerList.setVisibility(RecyclerView.VISIBLE);
                        if (eventCallback.getTotalEventsFound() == 1) {
                            recommendedEvents.add(eventCallback.getEvents().get(0));
                        } else if (eventCallback.getTotalEventsFound() == 2) {
                            for (int i = 0; i < 2; i++) {
                                recommendedEvents.add(eventCallback.getEvents().get(i));
                                Log.d("Recommended Event", eventCallback.getEvents().get(i).getName());
                            }

                        } else if (eventCallback.getTotalEventsFound() >= 3) {
                            for (int i = 0; i < 3; i++) {
                                recommendedEvents.add(eventCallback.getEvents().get(i));
                                Log.d("Recommended Event", eventCallback.getEvents().get(i).getName());
                            }
                            moreRecommendedEvents = true;
                            showMoreRecommendedEvents.setVisibility(LinearLayout.VISIBLE);
                        } else {
                            mRecommendedProgress.setVisibility(ProgressBar.GONE);
                            mRecommendedRecyclerList.setVisibility(RecyclerView.GONE);
                            emptyRecommendedEventsIndicator.setVisibility(TextView.VISIBLE);
                            emptyRecommendedEventsIndicator.setText(R.string.no_recommended_events);
                        }
                        recommendedEventsAdapter.notifyDataSetChanged();
                    }

                });
    }

    public void favorite(Event event) {
        NoSQLEntity<Event> entity = new NoSQLEntity<Event>("favorite_artists", event.getId() + "");
        entity.setData(event);
        NoSQL.with(getApplicationContext()).using(Event.class).save(entity);
    }

    public void unfavorite(Event event) {
        NoSQL.with(getApplicationContext()).using(Event.class)
                .bucketId("favorite_artists")
                .entityId(event.getId() + "")
                .delete();
    }

    private void getEvent() {
        int id = event.getId();
        API api = RetrofitAdapter.createAPI();
        Observable<Event> events = api.getEvent(id);
        events.observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.newThread()).
                subscribe(new Subscriber<Event>() {
                    @Override
                    public void onCompleted() {
                        eventLoadingBar.setVisibility(ProgressBar.GONE);

                    }

                    @Override
                    public void onError(Throwable e) {
                        eventLoadingBar.setVisibility(ProgressBar.GONE);
                        Toast.makeText(EventDetailsActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Event eventt) {
                        ticketUrl = event.getTicketUrls().getUrl();
                        ticketSite.setText(eventt.getTicketUrls().getSource());
                    }
                });
        ticketlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ticketUrl;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}
