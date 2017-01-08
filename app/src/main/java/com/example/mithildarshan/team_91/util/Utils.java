package com.example.mithildarshan.team_91.util;

import android.content.res.Resources;

import java.util.Calendar;

/**
 * Created by mithishri on 1/8/2017.
 */

public class Utils {

    public static final String PREFS = "sharedprefs";
    public static final String KEY_FIRST_START = "firststart";
    public static final String KEY_REF_RATE = "refreshrate";

    public static boolean isMarketClosed() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        return ((c.get(Calendar.HOUR_OF_DAY) > 14 && c.get(Calendar.MINUTE) > 30)
                || (c.get(Calendar.HOUR_OF_DAY) < 10 && c.get(Calendar.MINUTE) < 15));
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
