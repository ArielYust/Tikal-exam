package com.yust.ariel.tikalexam.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.noodle.Noodle;
import com.yust.ariel.tikalexam.data.PostersCacheManager;
import com.yust.ariel.tikalexam.data.Utils;
import com.yust.ariel.tikalexam.model.MovieInfoData;
import com.yust.ariel.tikalexam.model.MoviesData;
import com.yust.ariel.tikalexam.model.SQLMoviesData;
import com.yust.ariel.tikalexam.network.WebApiHub;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Ariel Yust on 07-Mar-17.
 */

public class MoviesLoader extends AsyncTaskLoader<MoviesData> {

    private MoviesData mDataCache;
    private Call<MoviesData> mCall;

    public MoviesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mDataCache != null) {
            deliverResult(mDataCache);
        } else {
            forceLoad();
        }
    }

    static boolean doOnce;

    @Override
    public MoviesData loadInBackground() {
        try {
            if (Utils.isNetworkAvailable(getContext())) {
                /* Requests movies from WebApi */
                mCall = WebApiHub.getDefault().getPopularMovies(getContext(), 1);
                mDataCache = mCall.execute().body();
                doOnce=!doOnce;
            }else {
                /* No Internet, request movies from database */
                Noodle noodle = Noodle.with(getContext()).build();
                SQLMoviesData sql = noodle.get("save", SQLMoviesData.class).now();
                if (sql!=null) {
                    /* Get MoviesData from Database */
                    mDataCache = sql.getMoviesData();

                    /* Get All Cached posters from Database */
                    PostersCacheManager.getDefault().copyAll(sql.getPostersCacheManager());
                }else{
                    Timber.e("Please connect to internet at least once to see results.");
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return mDataCache;
    }

    @Override
    public void deliverResult(MoviesData data) {
        mDataCache = data;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        if (mCall != null && !mCall.isCanceled()) mCall.cancel();
    }
}
