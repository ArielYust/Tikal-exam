package com.yust.ariel.tikalexam.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yust.ariel.tikalexam.R;
import com.yust.ariel.tikalexam.data.Utils;
import com.yust.ariel.tikalexam.loader.MoviesLoader;
import com.yust.ariel.tikalexam.logic.OnScreenDisplayLogic;
import com.yust.ariel.tikalexam.model.MovieInfoData;
import com.yust.ariel.tikalexam.model.MoviesData;
import com.yust.ariel.tikalexam.ui.MoviesGridAdapter;

import java.util.List;

/**
 * Created by Ariel Yust on 07-Mar-17.
 */

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<MoviesData> {
    public static final String TAG = MovieInfoFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MoviesGridAdapter mRvAdapter;
    private MoviesData mMoviesData;
    private boolean mIsOnline;

    /*........................................Constructor.........................................*/

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    /*.....................................Life.Cycle.Methods.....................................*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /* Calculate how many fragments to display */
        OnScreenDisplayLogic.getDefault().calc(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRvAdapter = new MoviesGridAdapter(getContext());
        mRecyclerView.setAdapter(mRvAdapter);

        mRecyclerView.setLayoutManager(
                new GridLayoutManager(getContext(),
                OnScreenDisplayLogic.getDefault().getGridSpanCount()
        ));

        return v;
    }

    /**
     * Reload fragment with new data
     *
     * @param data
     */
    public void setMoviesData(MoviesData data) {
        mMoviesData = data;
        if (!this.isDetached()) {
            getFragmentManager().beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }

    /*.......................................Loader.Methods.......................................*/

    @Override
    public Loader<MoviesData> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<MoviesData> loader, MoviesData data) {
        mMoviesData = data;
        mRvAdapter.setData(mMoviesData);
    }

    @Override
    public void onLoaderReset(Loader<MoviesData> loader) {
        mMoviesData = null;
    }

}
