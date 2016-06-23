package me.captivelabs.goliveit.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.data.API;
import me.captivelabs.goliveit.data.RetrofitAdapter;
import me.captivelabs.goliveit.models.AutoSuggestSearchResult;
import me.captivelabs.goliveit.models.Venue;
import me.captivelabs.goliveit.ui.activities.SearchActivity;
import me.captivelabs.goliveit.ui.adapters.SearchVenueAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchVenueFragment extends android.support.v4.app.Fragment {
    @Bind(R.id.tvSearch_empty)
    TextView textEmpty;
    @Bind(R.id.rvVenue_search)
    RecyclerView venueSearchList;
    @Bind(R.id.progressBar3)
    ProgressBar searchProgress;
    SearchVenueAdapter searchEventAdapter;
    private ArrayList<Venue> venues;
    private String searchTerm;


    public SearchVenueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_venue, container, false);
        ButterKnife.bind(this, view);

        SearchActivity searchActivity = (SearchActivity) getActivity();

        searchTerm = searchActivity.getSearchTerm();
        searchArtists();

        return view;
    }

    private void searchArtists() {
        API api = RetrofitAdapter.createAPI();
        final Observable<AutoSuggestSearchResult> autoSuggestArtist = api.autoSuggestVenue(searchTerm);
        autoSuggestArtist
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<AutoSuggestSearchResult>() {
                    @Override
                    public void onCompleted() {
                        searchProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        searchProgress.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AutoSuggestSearchResult autoSuggestSearchResult) {
                        if (autoSuggestSearchResult.getTotalVenuesFound() == 0) {
                            searchProgress.setVisibility(ProgressBar.GONE);
                            textEmpty.setVisibility(TextView.VISIBLE);
                            venueSearchList.setVisibility(RecyclerView.GONE);
                        } else {
                            venues = autoSuggestSearchResult.getVenues();
                            venueSearchList.setVisibility(RecyclerView.VISIBLE);
                           // searchEventAdapter.notifyDataSetChanged();
                            searchProgress.setVisibility(ProgressBar.GONE);

                            searchEventAdapter = new SearchVenueAdapter(venues, getContext());
                            venueSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
                            venueSearchList.setAdapter(searchEventAdapter);
                        }
                    }
                });


    }


}
