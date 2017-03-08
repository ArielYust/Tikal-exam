package com.yust.ariel.tikalexam.model;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.yust.ariel.tikalexam.data.PostersCacheManager;
import com.yust.ariel.tikalexam.data.Utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import timber.log.Timber;

/**
 * Created by Ariel Yust on 07-Mar-17.
 */
public class SQLMoviesData {

    String mMoviesDataJson;
    String mPostersBase64Json;

    public SQLMoviesData() {
    }

    public MoviesData getMoviesData() {
        try {
            return new Gson().fromJson(
                    Utils.decompress(
                            mMoviesDataJson.getBytes(StandardCharsets.ISO_8859_1)
                    ),
                    MoviesData.class);
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    public PostersCacheManager getPostersCacheManager() {
        try {
            return new Gson().fromJson(
                    Utils.decompress(
                            mPostersBase64Json.getBytes(StandardCharsets.ISO_8859_1)
                    ),
                    PostersCacheManager.class);
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    public SQLMoviesData(String moviesDataJson, String postersBase64Json) {
        try {
            mMoviesDataJson = new String(
                    Utils.compress(moviesDataJson),
                    StandardCharsets.ISO_8859_1
            );
            mPostersBase64Json = new String(
                    Utils.compress(postersBase64Json),
                    StandardCharsets.ISO_8859_1
            );
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
