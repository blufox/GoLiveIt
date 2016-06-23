package me.captivelabs.goliveit.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.google.android.gms.maps.model.LatLng;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.data.API;
import me.captivelabs.goliveit.data.RetrofitAdapter;
import me.captivelabs.goliveit.models.AutoSuggestSearchResult;
import me.captivelabs.goliveit.models.Performer;
import me.captivelabs.goliveit.ui.adapters.ArtistListAdapter;
import me.captivelabs.goliveit.ui.adapters.MainFragmentsAdapter;
import me.captivelabs.goliveit.utils.Functions;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LandingPage extends AppCompatActivity {

    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    @Bind(R.id.app_bar)
    Toolbar toolbar;
    @Bind(R.id.nav_drawer)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    LinearLayout mNavigationView;
    LatLng myLocation;
    String previous_term = "";
    @Bind(R.id.progressBar2)
    ProgressBar searchProgressbar;
    ArtistListAdapter artistListAdapter;
    ArrayList<Performer> performers;
    @Bind(R.id.psApp_bar_search)
    SearchBox sbSearch;
    @Bind(R.id.vActivity_landing_page_dark_overlay)
    View vDarkOverlay;
    private TabLayout slidingTabLayout;
    private ViewPager viewPager;
    private boolean mUserLearnedDrawer;
    private int mCurrentSelectedPosition;

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    @OnClick(R.id.rvDrawer_home)
    void goToHome() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @OnClick(R.id.rvDrawer_notifications)
    void goToNotifications() {
        startActivity(new Intent(getBaseContext(), NotificationsActivity.class));
    }

    @OnClick(R.id.rvDrawer_manage_artist)
    void goToManageArtists() {
        startActivity(new Intent(getBaseContext(), ManageMyArtistActivity.class));
    }

    @OnClick(R.id.rvDrawer_send_feedback)
    void goToSendFeedback() {
        //open main intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "ronolikeaaron90@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Eventhub feedback");
        intent.putExtra(Intent.EXTRA_TEXT, "Email body");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    @OnClick(R.id.rvDrawer_settings)
    void goToSettings() {
        startActivity(new Intent(getBaseContext(), Settings2Activity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        ButterKnife.bind(this);

        vDarkOverlay.setVisibility(View.GONE);
        // sbSearch.enableVoiceRecognition(this);

        setUpActionbar();
        prepareViews();
        setupTabs();

        myLocation = getMyLocation(); //eventually pick from device

        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            boolean mFromSavedInstanceState = true;
        }

        setUpNavDrawer();

        RippleView rvManageArtist = (RippleView) findViewById(R.id.rvDrawer_manage_artist);

        FrameLayout flMap = (FrameLayout) findViewById(R.id.flDrawer_map);
        flMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(getBaseContext(), LocationActivity.class);
                startActivity(intent);
            }
        });

        ImageView ivMyLocation = (ImageView) findViewById(R.id.ivDrawer_header_my_location);
        Picasso.with(this).load(Functions.deriveMyLocationImage(myLocation.latitude, myLocation.longitude)).into(ivMyLocation);

    }

    public void openSearch() {

        toolbar.setTitle("");
        sbSearch.setHint("search artists, events or venues");
        sbSearch.revealFromMenuItem(R.id.fab, this);
        sbSearch.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                // Hamburger has been clicked
                Toast.makeText(LandingPage.this, "Menu click",
                        Toast.LENGTH_LONG).show();
            }

        });
        sbSearch.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen
                vDarkOverlay.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                vDarkOverlay.setVisibility(View.GONE);
                searchProgressbar.setVisibility(ProgressBar.GONE);
                closeSearch();
            }

            @Override
            public void onSearchTermChanged(String term) {
                // React to the search term changing
                // Called after it has updated results
                if (term.isEmpty()) {
                    searchProgressbar.setVisibility(ProgressBar.GONE);
                }
                searchProgressbar.setVisibility(ProgressBar.VISIBLE);
                sbSearch.showLoading(true);
                searchArtists(term);
                Log.d("search_term", term);
            }

            @Override
            public void onSearch(String searchTerm) {
                //searchArtists(searchTerm);
                toolbar.setTitle(searchTerm);
            }

            @Override
            public void onResultClick(final SearchResult result) {
                //React to result being clicked
                Intent intent = new Intent(LandingPage.this, SearchActivity.class);
                intent.putExtra("searchTerm", result.title);
                startActivity(intent);


            }

            @Override
            public void onSearchCleared() {

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            sbSearch.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        vDarkOverlay.setVisibility(View.GONE);
        sbSearch.hideCircularly(this);
        if (sbSearch.getSearchText().isEmpty()) toolbar.setTitle("");
    }

    private void searchArtists(final String searchTerm) {
        API api = RetrofitAdapter.createAPI();
        Observable<AutoSuggestSearchResult> artistObservable = api.autoSuggestArtist(searchTerm);
        artistObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<AutoSuggestSearchResult>() {
                    @Override
                    public void onCompleted() {
                        searchProgressbar.setVisibility(ProgressBar.GONE);
                        sbSearch.showLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        searchProgressbar.setVisibility(ProgressBar.GONE);
                        sbSearch.showLoading(false);
                        Log.e("Search error", e.toString());
                    }

                    @Override
                    public void onNext(AutoSuggestSearchResult result) {
                        performers = result.getPerformers();
                        if (!previous_term.equals(searchTerm)) {
                            //sbSearch.clearResults();

                            for (Performer performer : performers) {
                                SearchResult option = new SearchResult(performer.getName(), getResources().getDrawable(
                                        R.drawable.ic_history));
                                sbSearch.addSearchable(option);
                            }
                        }

                        previous_term = searchTerm;
                    }
                });
    }

    public LatLng getMyLocation() {
        myLocation = new LatLng(-1.2987243, 36.786388);
        return myLocation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);

    }

    public void setUpActionbar() {
        setSupportActionBar(toolbar);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.primary));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        setTitle("");

        toolbar.setNavigationIcon(R.mipmap.btn_drawer_default);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnEvents_search) {
            openSearch();
            // sbSearch.enableVoiceRecognition(this);
            sbSearch.revealFromMenuItem(id, this);
            vDarkOverlay.setVisibility(View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareViews() {

        slidingTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatedLayout);
    }

    private void setupTabs() {
        MainFragmentsAdapter adapter = new MainFragmentsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);
        slidingTabLayout.setupWithViewPager(viewPager);
        slidingTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setUpNavDrawer() {
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            setTitle("");

        }

        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mUserLearnedDrawer = true;
            saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
        }

    }

    public void save(Performer event) {
        NoSQLEntity<Performer> entity = new NoSQLEntity<Performer>("my_artists", event.getId() + "");
        entity.setData(event);
        NoSQL.with(getApplicationContext()).using(Performer.class).save(entity);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        EventBus.getDefault().unregister(this);
//    }
//
//    public void onEvent(FavoriteEvent event) {
//    }
}
