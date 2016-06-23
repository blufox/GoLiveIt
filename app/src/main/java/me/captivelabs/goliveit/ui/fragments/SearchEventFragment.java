package me.captivelabs.goliveit.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.data.API;
import me.captivelabs.goliveit.data.RetrofitAdapter;
import me.captivelabs.goliveit.models.AutoSuggestSearchResult;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.ui.activities.SearchActivity;
import me.captivelabs.goliveit.ui.adapters.SearchEventAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchEventFragment extends android.support.v4.app.Fragment {
    @Bind(R.id.tvSearch_empty)
    TextView textEmpty;
    @Bind(R.id.rvEvent_search)
    RecyclerView artistSearchList;
    @Bind(R.id.progressBar3)
    ProgressBar searchProgress;
    SearchEventAdapter searchEventAdapter;
    private ArrayList<Event> events;
    private String searchTerm;


    public SearchEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_event, container, false);
        ButterKnife.bind(this, view);

        SearchActivity searchActivity = (SearchActivity) getActivity();

        searchTerm = searchActivity.getSearchTerm();
        searchEvents();

        return view;
    }

    private void searchEvents() {
        API api = RetrofitAdapter.createAPI();
        final Observable<AutoSuggestSearchResult> autoSuggestArtist = api.autoSuggestEvent(searchTerm);
        autoSuggestArtist
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<AutoSuggestSearchResult>() {
                    @Override
                    public void onCompleted() {
                        searchProgress.setVisibility(ProgressBar.GONE);
                        textEmpty.setVisibility(TextView.GONE);
                        artistSearchList.setVisibility(RecyclerView.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        searchProgress.setVisibility(ProgressBar.GONE);
                        textEmpty.setVisibility(TextView.VISIBLE);
                        textEmpty.setText("An error occurred");
                        artistSearchList.setVisibility(RecyclerView.GONE);
                        Log.e("Search event error: ", e.toString());
                    }

                    @Override
                    public void onNext(AutoSuggestSearchResult autoSuggestSearchResult) {
                        if (autoSuggestSearchResult.getTotalEventsFound() == 0) {
                            searchProgress.setVisibility(ProgressBar.GONE);
                            textEmpty.setVisibility(TextView.VISIBLE);
                            artistSearchList.setVisibility(RecyclerView.GONE);
                        } else {
                            events = autoSuggestSearchResult.getEvents();
                            artistSearchList.setVisibility(RecyclerView.VISIBLE);
                            // searchEventAdapter.notifyDataSetChanged();
                            searchProgress.setVisibility(ProgressBar.GONE);

                            searchEventAdapter = new SearchEventAdapter(events, getContext());
                            artistSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
                            artistSearchList.setAdapter(searchEventAdapter);
                        }
                    }
                });
    }
}
