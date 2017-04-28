package org.literacyapp.startguide.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Date;


public class StartPrefsHelper {

    private static final String PREF_FIRST_STARTUP_DATE = "first_startup_date";

    private static final long SEVEN_DAYS_IN_MILLISECONDS = 604_800_000; // 7d*24h*60m*60s*1000;


    public static boolean startAfterBoot(Context context) {
        long firstStartupDate = getFirstStartupDate(context);
        long currentDate = System.currentTimeMillis();

        Log.d(StartPrefsHelper.class.getName(),
                "First start-up less than seven days ago: " + ((firstStartupDate + SEVEN_DAYS_IN_MILLISECONDS) > currentDate));

        if (firstStartupDate == 0) {
            storeFirstStartupDate(context);
        }
        return ((firstStartupDate + SEVEN_DAYS_IN_MILLISECONDS) > currentDate);
    }

    public static boolean inactivateStartGuide(){
        //UTC Thu May 11 2017 20:59:59
        long inactivationTime = 1494536399000L;
        long currentTime = System.currentTimeMillis();
        boolean inactive = currentTime > inactivationTime;
        Log.i(StartPrefsHelper.class.getName(), "Start Guide inactive: " + inactive + ", inactivationTime: " + new Date(inactivationTime) + ", currentTime: " + new Date(currentTime));
        return inactive;
    }

    private static long getFirstStartupDate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getLong(PREF_FIRST_STARTUP_DATE, 0);
    }

    private static boolean storeFirstStartupDate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.edit().putLong(PREF_FIRST_STARTUP_DATE, System.currentTimeMillis()).commit();
    }
}
