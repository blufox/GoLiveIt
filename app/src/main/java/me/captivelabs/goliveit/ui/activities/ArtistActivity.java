package me.captivelabs.goliveit.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.data.API;
import me.captivelabs.goliveit.data.RetrofitAdapter;
import me.captivelabs.goliveit.data.callbacks.EventCallback;
import me.captivelabs.goliveit.models.Artist;
import me.captivelabs.goliveit.models.AutoSuggestSearchResult;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.models.Performer;
import me.captivelabs.goliveit.ui.adapters.RecommendedEventsAdapter;
import me.captivelabs.goliveit.ui.adapters.UpcomingEventsAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArtistActivity extends AppCompatActivity {
    @Bind(R.id.pbRecommendedProgress)
    ProgressBar mRecommendedProgress;
    @Bind(R.id.rvRecommendedRecycler)
    RecyclerView mRecommendedRecyclerList;
    RecommendedEventsAdapter recommendedEventsAdapter;

    @Bind(R.id.pbUpcomingProgress)
    ProgressBar mUpcomingProgress;
    @Bind(R.id.rvUpcoming_event_recycler)
    RecyclerView mUpcomingRecylerList;
    @Bind(R.id.ivArtist_facebook)
    ImageView facebook;
    @Bind(R.id.ivArtist_twitter)
    ImageView twitter;
    @Bind(R.id.ivArtist_website)
    ImageView website;
    @Bind(R.id.ivArtist_wikipedia)
    ImageView wikipedia;
    @Bind(R.id.tvNo_upcoming_events)
    TextView noUpcomingEvents;

    ArrayList<Event> upcomingEvents;
    ArrayList<Event> recommendedEvents;
    Artist artist;
    Performer performer;
    boolean max_lines_four = true;
    @Bind(R.id.llUpcoming_show_more)
    LinearLayout showMoreUpcomingLayout;
    private UpcomingEventsAdapter upcomingEventsAdapter;

    @OnClick(R.id.llRecommended_show_more)
    void showMoreRecommededEvents() {
        startActivity(new Intent(ArtistActivity.this, RecommendedEventActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        ButterKnife.bind(this);

        if (getIntent().getSerializableExtra("artist") instanceof Artist) {
            artist = (Artist) getIntent().getSerializableExtra("artist");
        }
        if (getIntent().getSerializableExtra("artist") instanceof Performer) {
            performer = (Performer) getIntent().getSerializableExtra("artist");
            getArtist(performer.getId());
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (artist != null) {
            collapsingToolbarLayout.setTitle(artist.getName());
        }
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        setUpPage();


    }

    public void setUpPage() {
        if (artist != null) {
            setUpSocialMedia();
            setupRecommendedEvents();
            setupUpcomingEvents();
            setUpViews();
            setUpAbout();

            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle(artist.getName());

        }
    }

    private void setUpAbout() {
        TextView tvAboutTitle = (TextView) findViewById(R.id.tvArtist_info_about_title);
        final TextView tvDescription = (TextView) findViewById(R.id.tvArtist_info_description);
        LinearLayout llShowMore = (LinearLayout) findViewById(R.id.llArtist_info_show_more);
        final TextView tvShowMoreLess = (TextView) findViewById(R.id.tvArtist_info_show_more_less);
        final ImageView ivShowMoreLess = (ImageView) findViewById(R.id.ivArtist_info_show_more_less_arrow);

        tvAboutTitle.setText("About " + artist.getName());
        tvDescription.setText(artist.getDescription());
        tvDescription.setMaxLines(4);


        llShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (max_lines_four) {
                    tvDescription.setMaxLines(20);
                    max_lines_four = !max_lines_four;
                    tvShowMoreLess.setText("Show less");
                    ivShowMoreLess.setImageDrawable(getResources().getDrawable(R.drawable.btn_show_more_default));
                } else {
                    tvDescription.setMaxLines(4);
                    max_lines_four = !max_lines_four;
                    tvShowMoreLess.setText("Show more");
                    ivShowMoreLess.setImageDrawable(getResources().getDrawable(R.drawable.btn_show_more_default));

                }
            }
        });


    }

    private void setUpViews() {
        setUpHeader();
    }

    private void setUpHeader() {
        TextView tvName = (TextView) findViewById(R.id.tvActivity_artist_name);
        TextView tvGenre = (TextView) findViewById(R.id.tvActivity_artist_genre);
        CircularImageView civPpic = (CircularImageView) findViewById(R.id.ivActivity_artist_image);
        ImageView ivBanner = (ImageView) findViewById(R.id.ivActivity_artist_banner);

        tvName.setText(artist.getName());
        String genres = "";
        int i = 0;
        if (artist.getGenres() != null) {
            for (String genre : artist.getGenres()) {
                if (i++ > 0) {
                    genre = genre.substring(0, 1).toUpperCase() + genre.substring(1);
                    genres += genre + "";
                    break;
                }
                genres += genre + "/ ";
            }
        }
        tvGenre.setText(genres);
        if (artist.getImages() != null) {
            if (artist.getImages().size() > 0) {
                Picasso.with(this).load(artist.getImages().get(0).getUrl()).into(civPpic);
                Picasso.with(this).load(artist.getImages().get(0).getUrl()).into(ivBanner);
            }
        }
    }

    private void setUpSocialMedia() {

        if(artist.getUrls() != null) {
            final String facebookUrl = artist.getUrls().getFacebook_url();
            final String twitterUrl = artist.getUrls().getTwitter_url();
            final String websiteUrl = artist.getUrls().getOfficial_url();
            final String wikipediaUrl = artist.getUrls().getWikipedia_url();

            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/426253597411506"));
                        startActivity(intent);
                    } catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
                    }
                }
            });
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl)));
                        // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter_user_name)));
                    } catch (Exception e) {
                        if (twitterUrl != null) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl)));
                        }
                        // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitter_user_name)));
                    }
                }
            });
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(websiteUrl));
                    startActivity(intent);

                }
            });
            wikipedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(wikipediaUrl));
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist, menu);
        //  Menu favorite = (Menu) menu.getItem(R.id.favorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.favorite) {
            if (artist != null && !artist.is_favorite()) {
                item.setIcon(R.drawable.starred_star);
                artist.setIs_favorite(true);
                Toast.makeText(getApplicationContext(), "Added to your favorites", Toast.LENGTH_LONG).show();
                favorite(artist);

            } else if (artist != null && artist.is_favorite()) {
                unfavorite(artist);
                item.setIcon(R.drawable.star_unstarred);
                artist.setIs_favorite(false);
                Toast.makeText(getApplicationContext(), "Removed from your favorites", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUpcomingEvents() {
        mUpcomingProgress.setVisibility(ProgressBar.VISIBLE);
        API api = RetrofitAdapter.createAPI();
        Observable<AutoSuggestSearchResult> autoSuggestSearchResultObservable = api.autoSuggestEvent(artist.getName());
        autoSuggestSearchResultObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<AutoSuggestSearchResult>() {
                    @Override
                    public void onCompleted() {
                        mUpcomingProgress.setVisibility(ProgressBar.GONE);
                        if (upcomingEvents.size() > 3) {
                            showMoreUpcomingLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ArtistActivity.this, UpcomingEventsActivity.class);
                                    intent.putExtra("artist_name", artist.getName());
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", e.toString());
                        mUpcomingProgress.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onNext(AutoSuggestSearchResult autoSuggestSearchResult) {
                        if (autoSuggestSearchResult.getTotalEventsFound() > 0) {
                            mUpcomingRecylerList.setVisibility(RecyclerView.VISIBLE);
                            noUpcomingEvents.setVisibility(TextView.GONE);
                            if (autoSuggestSearchResult.getTotalEventsFound() == 1) {
                                upcomingEvents.add(autoSuggestSearchResult.getEvents().get(0));
                            } else if (autoSuggestSearchResult.getTotalEventsFound() == 2) {
                                for (int i = 0; i < 2; i++) {
                                    upcomingEvents.add(autoSuggestSearchResult.getEvents().get(i));
                                }
                            } else {
                                for (int i = 0; i < 3; i++) {
                                    showMoreUpcomingLayout.setVisibility(LinearLayout.VISIBLE);
                                    upcomingEvents.add(autoSuggestSearchResult.getEvents().get(i));
                                }
                            }
                            upcomingEventsAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Artist Upcoming events", "No upcoming events");
                        }
                    }
                });
    }

    private void setupUpcomingEvents() {
        upcomingEvents = new ArrayList<>();
        getUpcomingEvents();
        upcomingEventsAdapter = new UpcomingEventsAdapter(upcomingEvents, getApplicationContext());
        mUpcomingRecylerList.setLayoutManager(new LinearLayoutManager(this));
        mUpcomingRecylerList.setAdapter(upcomingEventsAdapter);
    }

    private void setupRecommendedEvents() {
        recommendedEvents = new ArrayList<>();
        mRecommendedRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        getRecommendEvents();
        recommendedEventsAdapter = new RecommendedEventsAdapter(recommendedEvents, getApplicationContext());
        mRecommendedRecyclerList.setAdapter(recommendedEventsAdapter);
    }

    private void getRecommendEvents2() {
        mRecommendedProgress.setVisibility(ProgressBar.VISIBLE);
        API api = RetrofitAdapter.createAPI();
        Observable<EventCallback> events = api.getRecommendedEvents();
        events
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .take(3)
                .subscribe(new Subscriber<EventCallback>() {
                    @Override
                    public void onCompleted() {
                        // Toast.makeText(ArtistActivity.this, "Recommended events loading complete...", Toast.LENGTH_SHORT).show();
                        mRecommendedProgress.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Toast.makeText(ArtistActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        mRecommendedProgress.setVisibility(ProgressBar.GONE);
                        Log.e("Error: ", e.toString());
                    }

                    @Override
                    public void onNext(EventCallback eventCallback) {
                        for (int i = 0; i < eventCallback.getEvents().size(); i++) {
                            recommendedEvents.add(eventCallback.getEvents().get(i));
                            Log.d("Recommended Event", eventCallback.getEvents().get(i).getName());
                        }
                        recommendedEventsAdapter.notifyDataSetChanged();
                    }

                });
    }

    private void getRecommendEvents() {
        mRecommendedProgress.setVisibility(ProgressBar.VISIBLE);
        API api = RetrofitAdapter.createAPI();
        Observable<AutoSuggestSearchResult> events = api.getMyArtistEvents2(artist.getName());
        events
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .take(3)
                .subscribe(new Subscriber<AutoSuggestSearchResult>() {
                    @Override
                    public void onCompleted() {
                        // Toast.makeText(ArtistActivity.this, "Recommended events loading complete...", Toast.LENGTH_SHORT).show();
                        mRecommendedProgress.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Toast.makeText(ArtistActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        mRecommendedProgress.setVisibility(ProgressBar.GONE);
                        Log.e("Error: ", e.toString());
                    }

                    @Override
                    public void onNext(AutoSuggestSearchResult eventCallback) {
                        for (int i = 0; i < eventCallback.getEvents().size(); i++) {
                            recommendedEvents.add(eventCallback.getEvents().get(i));
                            Log.d("Recommended Event", eventCallback.getEvents().get(i).getName());
                        }
                        recommendedEventsAdapter.notifyDataSetChanged();
                    }

                });
    }

    public void favorite(Artist artist) {
        NoSQLEntity<Artist> entity = new NoSQLEntity<Artist>("my_artists", artist.getId() + "");
        entity.setData(artist);
        NoSQL.with(getApplicationContext()).using(Artist.class).save(entity);
    }

    public void unfavorite(Artist artist) {
        NoSQL.with(getApplicationContext()).using(Artist.class)
                .bucketId("my_artists")
                .entityId(artist.getId() + "")
                .delete();
    }

    private void getArtist(int id) {
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
                    public void onNext(final Artist artiste) {
                        artist = artiste;
                        setUpPage();
                    }
                });
    }
}
