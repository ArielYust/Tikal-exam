package com.yust.ariel.tikalexam.events;

import com.yust.ariel.tikalexam.model.MovieInfoData;

/**
 * Created by Ariel Yust on 07-Mar-17.
 */

public class MovieClickedEvent {
    private final MovieInfoData data;

    public MovieInfoData getData() {
        return data;
    }

    public MovieClickedEvent(MovieInfoData data) {
        this.data = data;
    }
}
