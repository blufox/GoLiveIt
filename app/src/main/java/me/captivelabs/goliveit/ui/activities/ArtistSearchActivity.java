package me.captivelabs.goliveit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.captivelabs.goliveit.R;
import rx.subjects.PublishSubject;

/**
 * Created by Aron on 10/27/2015.
 */
public class ArtistSearchActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.psApp_bar_search)
    SearchBox sbSearch;
    @Bind(R.id.vActivity_landing_page_dark_overlay)
    View vDarkOverlay;
    PublishSubject<String> searchText = PublishSubject.create();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search);
        ButterKnife.bind(this);

        vDarkOverlay.setVisibility(View.GONE);
        sbSearch.enableVoiceRecognition(this);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openSearch();
                return true;
            }
        });


    }

    public void openSearch() {
        mToolbar.setTitle("");
        sbSearch.revealFromMenuItem(R.id.mnEvents_search, this);
        for (int x = 0; x < 10; x++) {
            SearchResult option = new SearchResult("Result "
                    + Integer.toString(x), getResources().getDrawable(
                    R.drawable.ic_history));
            sbSearch.addSearchable(option);
        }
        sbSearch.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                // Hamburger has been clicked
                Toast.makeText(ArtistSearchActivity.this, "Menu click",
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
                closeSearch();
            }

            @Override
            public void onSearchTermChanged(String term) {
                // React to the search term changing
                // Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(ArtistSearchActivity.this, searchTerm + " Searched",
                        Toast.LENGTH_LONG).show();
                mToolbar.setTitle(searchTerm);

            }

            @Override
            public void onResultClick(SearchResult result) {
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
            sbSearch.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        sbSearch.hideCircularly(this);
        if (sbSearch.getSearchText().isEmpty()) mToolbar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
