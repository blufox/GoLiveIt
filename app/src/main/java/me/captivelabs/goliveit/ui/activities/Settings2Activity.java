package me.captivelabs.goliveit.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.Switch;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.captivelabs.goliveit.R;

/**
 * Created by Aron on 10/30/2015.
 */
public class Settings2Activity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.settingsMusic)
    RelativeLayout settingsMusic;
    @Bind(R.id.swNotification_switch)
    Switch notificationsSwitch;
    @Bind(R.id.settingsLegal)
    RelativeLayout settingsLegal;
    @Bind(R.id.mainContainer)
    RelativeLayout mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings3);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    //listeners
    @OnClick(R.id.settingsMusic)
    void loadMusic() {

    }

    @OnClick(R.id.settingsLegal)
    void loadLegal() {

    }
}
