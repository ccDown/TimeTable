package kuan.com.timetable.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import kuan.com.timetable.activity.AlarmAlert;

/**
 * Created by kuan on 2016/12/3.
 */

public class AlarmReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlarmAlert.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
}
