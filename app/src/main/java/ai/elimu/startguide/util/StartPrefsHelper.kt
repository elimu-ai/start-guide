package ai.elimu.startguide.util

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log

object StartPrefsHelper {
    private const val PREF_FIRST_STARTUP_DATE = "first_startup_date"

    private const val SEVEN_DAYS_IN_MILLISECONDS: Long = 604800000 // 7d*24h*60m*60s*1000;


    fun startAfterBoot(context: Context?): Boolean {
        val firstStartupDate = getFirstStartupDate(context)
        val currentDate = System.currentTimeMillis()

        if (firstStartupDate == 0L) {
            Log.d(StartPrefsHelper::class.java.getName(), "First start-up")
            storeFirstStartupDate(context)
            return true
        } else {
            Log.d(
                StartPrefsHelper::class.java.getName(),
                "First start-up less than seven days ago: " + ((firstStartupDate + SEVEN_DAYS_IN_MILLISECONDS) > currentDate)
            )
            return ((firstStartupDate + SEVEN_DAYS_IN_MILLISECONDS) > currentDate)
        }
    }

    private fun getFirstStartupDate(context: Context?): Long {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getLong(PREF_FIRST_STARTUP_DATE, 0)
    }

    private fun storeFirstStartupDate(context: Context?): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.edit().putLong(PREF_FIRST_STARTUP_DATE, System.currentTimeMillis())
            .commit()
    }
}
