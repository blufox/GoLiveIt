package me.captivelabs.goliveit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.data.API;
import me.captivelabs.goliveit.data.RetrofitAdapter;
import me.captivelabs.goliveit.models.Artist;
import me.captivelabs.goliveit.models.AutoSuggestSearchResult;
import me.captivelabs.goliveit.models.Performer;
import me.captivelabs.goliveit.ui.adapters.ArtistListAdapter;
import me.captivelabs.goliveit.utils.DividerDecoration;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ManageMyArtistActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.artistRecycler)
    RecyclerView artistList;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.progress)
    ProgressBar searchProgressbar;
    @Bind(R.id.switch1)
    Switch artistWithventsSwitch;
    ArtistListAdapter artistListAdapter;
    ArrayList<Performer> performers;
    SearchBox sbSearch;
    @Bind(R.id.vActivity_landing_page_dark_overlay)
    View vDarkOverlay;
    String previous_term = "";
    private ArrayList<Artist> artists = new ArrayList<>();
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_artist);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        vDarkOverlay.setVisibility(View.GONE);
        sbSearch = (SearchBox) findViewById(R.id.sbApp_bar_search);
        sbSearch.enableVoiceRecognition(this);

        artistList.setLayoutManager(new LinearLayoutManager(this));
        artistListAdapter = new ArtistListAdapter(artists, getApplicationContext());
        artistList.setAdapter(artistListAdapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(artistListAdapter);
        artistList.addItemDecoration(headersDecor);
        artistList.addItemDecoration(new DividerDecoration(getApplicationContext()));

        artistListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                headersDecor.invalidateHeaders();
            }
        });

        artists();

        artistWithventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked){
                  //filter artists with events
               }
            }
        });

    }

    //takes you to search artist page
    @OnClick(R.id.fab)
    void searchArtist() {
        vDarkOverlay.setVisibility(View.VISIBLE);
        sbSearch.enableVoiceRecognition(this);
        openSearch();
    }

    public void openSearch() {
        fab.setVisibility(FloatingActionButton.GONE);
        toolbar.setTitle("");
        sbSearch.revealFromMenuItem(R.id.fab, this);
        sbSearch.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                // Hamburger has been clicked
                Toast.makeText(ManageMyArtistActivity.this, "Menu click",
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
                fab.setVisibility(FloatingActionButton.VISIBLE);
                vDarkOverlay.setVisibility(View.GONE);
                searchProgressbar.setVisibility(ProgressBar.GONE);
                closeSearch();
            }

            @Override
            public void onSearchTermChanged(String term) {
                // React to the search term changing
                // Called after it has updated results
                searchProgressbar.setVisibility(ProgressBar.VISIBLE);
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
                Observable<ArrayList<Performer>> searchResultObservable = Observable.just(performers);
                searchResultObservable.filter(new Func1<ArrayList<Performer>, Boolean>() {
                    @Override
                    public Boolean call(ArrayList<Performer> performers) {
                        boolean isTheOne = false;
                        for (int i = 0; i < performers.size(); i++) {
                            isTheOne = performers.get(i).getName().equals(result.title);
                            if (performers.get(i).getName().equals(result.title)) {
                                save(performers.get(i));
                                Toast.makeText(ManageMyArtistActivity.this, result.title + " saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return isTheOne;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread());


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
        sbSearch.hideCircularly(this);
        fab.setVisibility(FloatingActionButton.VISIBLE);
        if (sbSearch.getSearchText().isEmpty()) toolbar.setTitle("");
    }

    private void searchArtists(final String searchTerm) {
        final ArrayList<SearchResult> option = new ArrayList<>();
        API api = RetrofitAdapter.createAPI();
        Observable<AutoSuggestSearchResult> artistObservable = api.autoSuggestArtist(searchTerm);
        artistObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<AutoSuggestSearchResult>() {
                               @Override
                               public void onCompleted() {
                                   searchProgressbar.setVisibility(ProgressBar.GONE);
                                   sbSearch.setSearchables(option);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   searchProgressbar.setVisibility(ProgressBar.GONE);
                                   Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();

                               }

                               @Override
                               public void onNext(AutoSuggestSearchResult result) {
                                   performers = result.getPerformers();
                                   if (!previous_term.equals(searchTerm)) {
                                       //sbSearch.clearResults();
                                       for (int i = 0; i < result.getTotalPerformersFound(); i++) {
                                           option.add(new SearchResult(result.getPerformers().get(i).getName(), getResources().getDrawable(
                                                   R.drawable.ic_history)));
                                           // sbSearch.addSearchable(option);
                                           previous_term = searchTerm;
                                       }
                                   }
                               }
                           }

                );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_artist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }


    }

    public void artists() {
        NoSQL.with(this).using(Artist.class)
                .bucketId("my_artists")
                .retrieve(new RetrievalCallback<Artist>() {
                    @Override
                    public void retrievedResults(List<NoSQLEntity<Artist>> entities) {
                        if (entities.size() > 0) {
                            artists.clear();
                            for (NoSQLEntity<Artist> entity : entities) {
                                Artist artist = entity.getData();
                                //order.setIs_accepted(true);
                                artists.add(entity.getData());
                            }
                            artistListAdapter.notifyDataSetChanged();

                            Collections.sort(artists, new Comparator<Artist>() {
                                public int compare(Artist artist1, Artist artist2) {
                                    return artist1.getName().compareTo(artist2.getName());
                                }
                            });

                        }


                    }
                });

    }

    public void save(Performer event) {
        NoSQLEntity<Performer> entity = new NoSQLEntity<Performer>("my_artists", event.getId() + "");
        entity.setData(event);
        NoSQL.with(getApplicationContext()).using(Performer.class).save(entity);
    }

}
