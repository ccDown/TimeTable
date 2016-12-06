package kuan.com.timetable.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import kuan.com.timetable.R;
import kuan.com.timetable.receiver.AlarmReceive;

public class AlarmAlert extends Activity {
    private MediaPlayer mediaPlayer = null;
    private Dialog builder;
    public static int clockposition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.start();
        builder = new AlertDialog.Builder(this).setTitle("闹钟时间到了！！！")
                .setMessage("上课时间到了！！！")
                .setCancelable(false)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancel();
                    }
                })
                .show();
    }

    public void cancel() {
        builder.dismiss();
        Intent intent = new Intent(AlarmAlert.this, AlarmReceive.class);
        intent.setAction("ALARM_ACTION"+clockposition);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmAlert.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        mediaPlayer.stop();
        finish();
    }
}