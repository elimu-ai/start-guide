package ai.elimu.startguide.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
//        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) && StartPrefsHelper.startAfterBoot(context)) {
//            Intent bootIntent = new Intent(context, SwipeUpDownActivity.class);
//            bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(bootIntent);
//        }
    }
}
