package ru.kraynov.app.ssaknitu.events.util.helper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DimensionHelper {
    /**
     * px to dp, dp to px
     */
    Context activity;
    public DimensionHelper(Context arg1){
        activity = arg1;
    }
    public int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, activity.getResources().getDisplayMetrics());
    }

    public int dpToPxOld(int dp) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px) {
        //return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, activity.getResources().getDisplayMetrics());
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
}
