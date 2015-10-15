package ru.kraynov.app.ssaknitu.events.view.widget;

import android.webkit.WebView;

public abstract class ScrollDirectionListener {
    private int mLastScrollY;
    private int mScrollThreshold;

    public abstract void onScrollUp();

    protected abstract void onScrollDown();

    public void onScrollChanged(WebView who, int l, int t, int oldl, int oldt) {
        boolean isSignificantDelta = Math.abs(t - this.mLastScrollY) > this.mScrollThreshold;
        if (isSignificantDelta) {
            if (t > this.mLastScrollY) {
                this.onScrollUp();
            } else {
                this.onScrollDown();
            }
        }

        this.mLastScrollY = t;
    }

    public void setScrollThreshold(int scrollThreshold) {
        this.mScrollThreshold = scrollThreshold;
    }
}