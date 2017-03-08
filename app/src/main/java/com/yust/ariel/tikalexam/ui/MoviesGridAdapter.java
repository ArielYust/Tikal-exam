package com.yust.ariel.tikalexam.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.noodle.Noodle;
import com.yust.ariel.tikalexam.R;
import com.yust.ariel.tikalexam.data.PostersCacheManager;
import com.yust.ariel.tikalexam.data.Utils;
import com.yust.ariel.tikalexam.events.MovieClickedEvent;
import com.yust.ariel.tikalexam.model.MovieInfoData;
import com.yust.ariel.tikalexam.model.MoviesData;
import com.yust.ariel.tikalexam.model.SQLMoviesData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Ariel Yust on 07-Mar-17.
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.ViewHolder> {

    private MoviesData mMoviesData;
    private Context mContext;
    private int mCompleteBitmaps;
    private String mStrImgPath;

    public void setData(@NonNull MoviesData list) {
        mMoviesData = list;
        mCompleteBitmaps = 0;
        notifyDataSetChanged();
    }

    public MoviesGridAdapter(@NonNull Context context) {
        mMoviesData = new MoviesData(1, new ArrayList<MovieInfoData>(), 0, 0);
        mCompleteBitmaps = 0;
        mContext = context;
    }

    public MoviesGridAdapter(@NonNull Context context, MoviesData movieInfoDatas) {
        mMoviesData = movieInfoDatas;
        mCompleteBitmaps = 0;
        mContext = context;
    }

    /* Easy access to the context object in the RecyclerView */
    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View contactView = inflater.inflate(R.layout.grid_item_movie, parent, false);

        /* Simply load images from site */
        mStrImgPath = getContext().getResources().getString(R.string.web_api_image_base_url);

        /* Return a new holder instance */
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /* Get the data model based on position */
        final MovieInfoData info = mMoviesData.getResults().get(position);

        /* Set poster based on data model */
        final ImageView poster = holder.getPoster();

        /* Set onclickListener */
        bindClick(poster, info);

        if (Utils.isNetworkAvailable(getContext())) {
            Glide.with(getContext())
                    .load(mStrImgPath + info.getPosterPath())
                    .asBitmap()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            Timber.e(e);
                            mCompleteBitmaps++;
                            checkForCompleteLoading();
                        }

                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        /* Cache downloaded Bitmap in a map, later be saved to SQLLite */
                            if (!PostersCacheManager
                                    .getDefault()
                                    .put(info.getId(), bitmap)) {
                                mCompleteBitmaps++;
                                checkForCompleteLoading();
                            }
                            poster.setImageBitmap(bitmap);
                        }
                    });
        } else {
            /* Load posters from Cache */
            Bitmap bitmap = PostersCacheManager.getDefault().get(info.getId());
            poster.setImageBitmap(bitmap);
        }
    }

    /**
     * We loaded all the Bitmaps let's save everything Locally in SQLLite
     */
    private void checkForCompleteLoading() {
        try {
            if (mCompleteBitmaps >= mMoviesData.getResults().size()) {
                Gson gson = new Gson();

                SQLMoviesData sql = new SQLMoviesData(
                        gson.toJson(mMoviesData),
                        gson.toJson(PostersCacheManager.getDefault())
                );
                Noodle noodle = Noodle.with(getContext()).build();
                noodle.put("save", sql).now();
            }

        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * Make sure that when an item is clicked then activity will be notified of it, could have
     * used an interface instead but preferred to use EventBus for comfort.
     *
     * @param view
     * @param data
     */
    private void bindClick(@NonNull final View view, final MovieInfoData data) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MovieClickedEvent(data));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMoviesData.getResults().size();
    }

    /**
     * Provide a direct reference to each of the views within a data item
     * Used to cache the views within the item layout for fast access
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPoster;

        public ImageView getPoster() {
            return mPoster;
        }

        /**
         * We also create a constructor that accepts the entire item row
         * and does the view lookups to find each subview
         *
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mPoster = (ImageView) itemView.findViewById(R.id.poster);
        }
    }
}
