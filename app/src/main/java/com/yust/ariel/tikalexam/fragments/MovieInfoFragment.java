package com.yust.ariel.tikalexam.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yust.ariel.tikalexam.R;
import com.yust.ariel.tikalexam.data.PostersCacheManager;
import com.yust.ariel.tikalexam.model.MovieInfoData;

import org.parceler.Parcels;

/**
 * Created by Ariel Yust on 07-Mar-17.
 */

public class MovieInfoFragment extends Fragment {
    public static final String TAG = MovieInfoFragment.class.getSimpleName();
    private static final String BUNDLE_PARCEL = "MovieInfoData";

    private ImageView mImgPoster;
    private TextView mTxtHeader,
            mTxtTitle,
            mTxtDuration,
            mTxtScore,
            mTxtDescription;

    private MovieInfoData mMovieInfoData;

    /*........................................Constructor.........................................*/

    public static MovieInfoFragment newInstance() {
        return new MovieInfoFragment();
    }

    public static MovieInfoFragment newInstance(MovieInfoData data) {
        final Bundle args = new Bundle();
        args.putParcelable(BUNDLE_PARCEL, Parcels.wrap(data));

        final MovieInfoFragment fragment = new MovieInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*.....................................Life.Cycle.Methods.....................................*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_info, container, false);

        mImgPoster = (ImageView) v.findViewById(R.id.img_poster);
        mTxtHeader = (TextView) v.findViewById(R.id.txt_header);
        mTxtTitle = (TextView) v.findViewById(R.id.txt_title);
        mTxtDuration = (TextView) v.findViewById(R.id.txt_duration);
        mTxtScore = (TextView) v.findViewById(R.id.txt_score);
        mTxtDescription = (TextView) v.findViewById(R.id.txt_description);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mMovieInfoData =
                    Parcels.unwrap(
                            bundle.getParcelable(BUNDLE_PARCEL)
                    );
            displayInfo();
        }

        return v;
    }

    /**
     * Reload fragment with new data
     *
     * @param data
     */
    public void setMovieInfoData(MovieInfoData data) {
        mMovieInfoData = data;
        if (!this.isDetached()) {
            displayInfo();
        }
    }

    /*......................................Private.Methods.......................................*/

    /**
     * Displays information from mMovieInfoData on screen
     */
    private void displayInfo() {
        /* Load posters from Cache */
        Bitmap bitmap = PostersCacheManager.getDefault().get(mMovieInfoData.getId());
        mImgPoster.setImageBitmap(bitmap);

        mTxtHeader.setText(mMovieInfoData.getOriginalTitle());
        mTxtTitle.setText(mMovieInfoData.getTitle());

        /* ToDo: one day find where the real duration is held */
        mTxtDuration.setText("120min");

        String avgScore = mMovieInfoData.getVoteAverage() + "/10";
        mTxtScore.setText(avgScore);

        mTxtDescription.setText(mMovieInfoData.getOverview());
    }
}
