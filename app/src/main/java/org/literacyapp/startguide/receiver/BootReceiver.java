package org.literacyapp.startguide.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.literacyapp.startguide.content.swipe.SwipeUpDownActivity;
import org.literacyapp.startguide.util.StartPrefsHelper;


public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) && !StartPrefsHelper.inactivateStartGuide()) {
            Intent bootIntent = new Intent(context, SwipeUpDownActivity.class);
            bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(bootIntent);
        }
    }
}
