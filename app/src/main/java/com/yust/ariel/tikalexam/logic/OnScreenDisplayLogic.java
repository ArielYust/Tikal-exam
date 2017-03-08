package com.yust.ariel.tikalexam.logic;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.yust.ariel.tikalexam.R;

/**
 * Created by Ariel Yust on 07-Mar-17.
 */

public class OnScreenDisplayLogic {

    private int mDisplayFragmentsOnScreen;
    private int mGridSpanCount;
    private boolean mIsTablet;

    public void calc(@NonNull Context context) {
        Resources resources = context.getResources();
        mIsTablet = resources.getBoolean(R.bool.isTablet);

        switch (resources.getConfiguration().orientation) {
            default:
            case Configuration.ORIENTATION_PORTRAIT:
                mGridSpanCount = 2;
                mDisplayFragmentsOnScreen = 1;
                if (mIsTablet) {
                    mGridSpanCount++;
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                mGridSpanCount = 3;
                break;
        }
        if (mIsTablet) mDisplayFragmentsOnScreen = 2;
    }

    public boolean getOnScreenSingle() {
        return getOnScreenFragmentsAmount() == 1;
    }

    public boolean getOnScreenDouble() {
        return getOnScreenFragmentsAmount() == 2;
    }

    /**
     * Displays how many fragments should be shown on screen
     * @return
     */
    public int getOnScreenFragmentsAmount() {
        return mDisplayFragmentsOnScreen;
    }

    /**
     * Tells the grid how many tiles per row should be displayed
     * @return
     */
    public int getGridSpanCount() {
        return mGridSpanCount;
    }

    /**
     * Is this a tablet or not
     * @return
     */
    public boolean isTablet() {
        return mIsTablet;
    }

    /*..........................................Singleton.........................................*/
    /**
     * Singleton Implementation
     * @return the single instance of OnScreenDisplayLogic
     */
    public static OnScreenDisplayLogic getDefault() {
        return OnScreenDisplayLogic.Holder.INSTANCE;
    }

    /**
     * Initialization-on-demand holder idiom,
     */
    private static class Holder {
        static final OnScreenDisplayLogic INSTANCE = new OnScreenDisplayLogic();
    }
}
