package me.captivelabs.goliveit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.ui.adapters.SearchFragmentsAdapter;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.tabLayout)
    TabLayout slidingTabLayout;
    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.psApp_bar_search)
    SearchBox searchBox;
    @Bind(R.id.llSearch_container)
    LinearLayout searchLayout;
    String searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setupTabs();
        searchTerm = getIntent().getStringExtra("searchTerm");
        searchBox.setTag(searchTerm);
        Log.d("searchTerm", searchTerm);
        openSearch();
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    private void setupTabs() {
        SearchFragmentsAdapter adapter = new SearchFragmentsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(0);
        slidingTabLayout.setupWithViewPager(viewPager);
        slidingTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    public void openSearch() {
        searchBox.populateEditText(searchTerm);
        searchBox.setSearchString(searchTerm);
        searchBox.enableVoiceRecognition(this);
        searchBox.requestFocus();
        //  searchBox.revealFromMenuItem(R.id.fab, this);
        //searchBox.setDrawerLogo(R.drawable.btn_back_dark_default);
        searchBox.setMenuVisibility(MenuItem.SHOW_AS_ACTION_NEVER);
        searchBox.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                finish();
            }
        });

        searchBox.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                searchBox.showLoading(true);
                closeSearch();
            }

            @Override
            public void onSearchTermChanged(String term) {
                // React to the search term changing
                // Called after it has updated results
                if (term.isEmpty()) {
                    searchBox.showLoading(false);
                }
                term = searchTerm;
                searchBox.showLoading(true);
                //searchArtists(term);
                Log.d("search_term", term);
            }

            @Override
            public void onSearch(String searchTermm) {
                //searchArtists(searchTerm);
                searchTerm = searchTermm;
            }

            @Override
            public void onResultClick(final SearchResult result) {
                //React to result being clicked
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
            searchBox.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    protected void closeSearch() {
        finish();

        //startActivity(new Intent(SearchActivity.this, LandingPage.class));
    }

}
