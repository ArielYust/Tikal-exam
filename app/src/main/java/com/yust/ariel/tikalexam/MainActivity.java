package com.yust.ariel.tikalexam;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.yust.ariel.tikalexam.events.MovieClickedEvent;
import com.yust.ariel.tikalexam.fragments.MovieInfoFragment;
import com.yust.ariel.tikalexam.logic.OnScreenDisplayLogic;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import timber.log.Timber;

/**
 * Created by Ariel Yust on 07-Mar-17.
 */

public class MainActivity extends AppCompatActivity {

    /*......................................Events.Handling.......................................*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MovieClickedEvent event) {
        /* When a single fragment should be visible we make sure we add the movie info fragment */
        if (OnScreenDisplayLogic.getDefault().getOnScreenSingle()) {

            /* Title and Back Arrow */
            getSupportActionBar().setTitle(R.string.toolbar_details_screen);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_movies, MovieInfoFragment.newInstance(event.getData()), MovieInfoFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            /* We are in dual mode, MovieInfoFragment should be notified */
            MovieInfoFragment f = (MovieInfoFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_movie_info);
            f.setMovieInfoData(event.getData());
        }
    }

    /*.....................................Life.Cycle.Methods.....................................*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Calculate how many fragments to display */
        OnScreenDisplayLogic.getDefault().calc(getBaseContext());

        /* make sure to display only debug logs */
        Timber.plant(new Timber.DebugTree());

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        /* Default ActionBar title */
        getSupportActionBar().setTitle(R.string.toolbar_main_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onBackPressed() {
        /* There are only two screens for the app so no need to create a
         * procedure for ActionBarTitle and back Arrow implemetations
         */
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

            /* Title and Back Arrow */
            getSupportActionBar().setTitle(R.string.toolbar_main_screen);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /*.........................................Action.Bar.........................................*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                /* display credits */
                displayCredits();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*......................................Private.Methods.......................................*/

    /**
     * Just a simple about dialog
     */
    private void displayCredits() {
        new AlertDialog.Builder(this)
                .setTitle("This App was Created By")
                .setMessage("Ariel Yust\nfor\nTikal\non 07/03/2017")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }
}
