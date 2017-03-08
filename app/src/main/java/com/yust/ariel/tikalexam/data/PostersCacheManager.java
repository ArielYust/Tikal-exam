package com.yust.ariel.tikalexam.data;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.yust.ariel.tikalexam.model.PostersCacheData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ariel Yust on 07-Mar-17.
 */

public class PostersCacheManager {    
    @Expose
    private Map<String,PostersCacheData> mMap = new HashMap<>();

    public void copyAll(PostersCacheManager manager) {
        mMap.clear();
        mMap.putAll(manager.mMap);
    }

    /**
     * Save Bitmaps to a Map to later be used Offline
     * @param movieId
     * @param bitmap
     */
    public boolean put(int movieId, Bitmap bitmap) {
        String strBase64 = Utils.encodeToBase64(
                bitmap,
                Bitmap.CompressFormat.JPEG,
                100);

        Object obj = mMap.put(String.valueOf(movieId), new PostersCacheData(strBase64));
        return obj != null;
    }

    /**
     * Load bitmaps from cache, use this method only when offline,
     * currently not very effective due to base64 decoding
     * @param movieId
     * @return
     */
    public Bitmap get(int movieId) {
        PostersCacheData cacheData = mMap.get(String.valueOf(movieId));
        if (cacheData == null) return null;
        return Utils.decodeBase64(cacheData.getmBase64());
    }
    
    /*..........................................Singleton.........................................*/
    /**
     * Singleton Implementation
     * @return the single instance of PostersCacheManager
     */
    public static PostersCacheManager getDefault() {
        return PostersCacheManager.Holder.INSTANCE;
    }

    /**
     * Initialization-on-demand holder idiom,
     */
    private static class Holder {
        static final PostersCacheManager INSTANCE = new PostersCacheManager();
    }
}
