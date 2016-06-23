package me.captivelabs.goliveit.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.captivelabs.goliveit.R;
import me.captivelabs.goliveit.data.API;
import me.captivelabs.goliveit.data.RetrofitAdapter;
import me.captivelabs.goliveit.models.Artist;
import me.captivelabs.goliveit.models.Event;
import me.captivelabs.goliveit.models.Venue;
import me.captivelabs.goliveit.utils.Functions;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VenueActivity extends AppCompatActivity {

    Venue venue;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        venue = (Venue) getIntent().getSerializableExtra("venue");
        event = (Event) getIntent().getSerializableExtra("event");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(venue != null) {
            collapsingToolbarLayout.setTitle(venue.getName());
        }
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if(venue != null){
            setUpViews();
            setUpVenue();
        }


    }





    private void setUpViews() {
        setUpHeader();
    }

    private void setUpVenue() {
        TextView tvName = (TextView) findViewById(R.id.tvEvent_details_venue_name);
        TextView tvAddress = (TextView) findViewById(R.id.tvEvent_details_venue_address);
        TextView tvCityAndState = (TextView) findViewById(R.id.tvEvent_details_venue_city_and_state);
        TextView tvDistance = (TextView) findViewById(R.id.tvEvent_details_venue_distance);

        ImageView ivMapImage = (ImageView) findViewById(R.id.ivEvent_details_venue_image);

        tvName.setText(venue.getName());
        tvAddress.setText(venue.getAddress1());
        tvCityAndState.setText(venue.getCity() + ", " + venue.getState() + ", " + venue.getCountry());
        tvDistance.setText((float) Math.round(event.getDistance()) + " km");

        Picasso.with(this).load(Functions.deriveVenueImage(venue.getLatitude(), venue.getLongitude())).into(ivMapImage);
    }

    private void setUpHeader() {
        TextView tvName = (TextView) findViewById(R.id.tvActivity_venue_name);
        tvName.setText(venue.getName());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist, menu);
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

    private void getArtist(int id) {
        API api = RetrofitAdapter.createAPI();
        Observable<Artist> artistObservable = api.getArtist(622);
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
                    public void onNext(Artist artist) {

                    }
                });
    }
}
