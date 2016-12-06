package kuan.com.timetable;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import kuan.com.timetable.activity.DialogActivity;
import kuan.com.timetable.activity.HomeWorkstep;
import kuan.com.timetable.adapter.MyGridAdapter;
import kuan.com.timetable.base.BaseActivity;
import kuan.com.timetable.database.MySqlHelper;
import kuan.com.timetable.receiver.AlarmReceive;

import static kuan.com.timetable.activity.AlarmAlert.clockposition;

public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    private TextView[] main_text;
    private TextView main_howtouse, main_setting, main_time1, main_time2, main_time3,
            main_time4, main_time5, main_time6, main_time7, main_time8, main_time9;
    public static GridView main_gridview;
    private MyGridAdapter myGridAdapter;
    private String[] cname, caddress, tname, tphone, temail, taddress;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            cursor();
            myGridAdapter = new MyGridAdapter(handler, MainActivity.this, cname, caddress, tname, tphone, temail, taddress);
            main_gridview.setAdapter(myGridAdapter);
        }
    };
    private Dialog howtousedialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
    }

    public void initViews() {
        main_time1 = (TextView) findViewById(R.id.main_time1);
        main_time2 = (TextView) findViewById(R.id.main_time2);
        main_time3 = (TextView) findViewById(R.id.main_time3);
        main_time4 = (TextView) findViewById(R.id.main_time4);
        main_time5 = (TextView) findViewById(R.id.main_time5);
        main_time6 = (TextView) findViewById(R.id.main_time6);
        main_time7 = (TextView) findViewById(R.id.main_time7);
        main_time8 = (TextView) findViewById(R.id.main_time8);
        main_time9 = (TextView) findViewById(R.id.main_time9);
        main_gridview = (GridView) findViewById(R.id.main_gridView);
        main_setting = (TextView) findViewById(R.id.setting);
        main_howtouse = (TextView) findViewById(R.id.howtouse);

    }

    public void initData() {
        main_text = new TextView[]{main_time1, main_time2, main_time3, main_time4, main_time5,
                main_time6, main_time7, main_time8, main_time9};
        for (TextView textview : main_text
                ) {
            textview.setOnClickListener(this);
        }
        main_howtouse.setOnClickListener(this);
        main_setting.setOnClickListener(this);
        cursor();
        myGridAdapter = new MyGridAdapter(handler, this, cname, caddress, tname, tphone, temail, taddress);
        main_gridview.setAdapter(myGridAdapter);
        main_gridview.setOnTouchListener(this);
    }

    public void cursor() {
        MySqlHelper mySqlHelper = new MySqlHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = mySqlHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * From info ", null);
        cname = new String[63];
        caddress = new String[63];
        tname = new String[63];
        tphone = new String[63];
        temail = new String[63];
        taddress = new String[63];
        if (cursor != null) {
            int i = 0;
            while (cursor.moveToNext()) {
                cname[i] = cursor.getString(1);
                caddress[i] = cursor.getString(2);
                tname[i] = cursor.getString(3);
                tphone[i] = cursor.getString(4);
                temail[i] = cursor.getString(5);
                taddress[i] = cursor.getString(6);
                Log.e("数据是", cname[i] + caddress[i] + tname[i] + taddress[i] + "");
                i++;
            }
            cursor.close();
        }
    }

    final int[] hour = {0};
    final int[] minute = {0};

    //点击左侧时间栏
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_time1:
                hour[0] = 8;
                minute[0] = 30;
                setTimeImage(1);
                break;
            case R.id.main_time2:
                hour[0] = 9;
                minute[0] = 30;
                setTimeImage(2);
                break;
            case R.id.main_time3:
                hour[0] = 10;
                minute[0] = 30;
                setTimeImage(3);
                break;
            case R.id.main_time4:

                hour[0] = 11;
                minute[0] = 30;
                setTimeImage(4);

                break;
            case R.id.main_time5:

                hour[0] = 12;
                minute[0] = 30;
                setTimeImage(5);

                break;
            case R.id.main_time6:

                hour[0] = 13;
                minute[0] = 30;
                setTimeImage(6);

                break;
            case R.id.main_time7:

                hour[0] = 14;
                minute[0] = 30;
                setTimeImage(7);

                break;
            case R.id.main_time8:

                hour[0] = 15;
                minute[0] = 30;
                setTimeImage(8);

                break;
            case R.id.main_time9:

                hour[0] = 16;
                minute[0] = 30;
                setTimeImage(9);

                break;
            case R.id.setting:
                startActivity(new Intent(MainActivity.this, HomeWorkstep.class));
                break;

            case R.id.howtouse:

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                View howtouse= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_howtouse,null);
                Button button=(Button)howtouse.findViewById(R.id.howtouse_cancel);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        howtousedialog.dismiss();
                    }
                });
                howtousedialog=builder.setView(howtouse).show();
        }

    }

    public void setTimeImage(int i) {
        boolean isring=false;
        SharedPreferences sharedPreferences = getSharedPreferences("TimeTable", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        i--;
        if (!sharedPreferences.getBoolean("time" + (i), false)) {
            main_text[i].setBackgroundResource(R.mipmap.clock);
            isring=true;
            editor.putBoolean("time" + (i), true);
        } else {
            main_text[i].setBackgroundResource(0);
            isring=false;
            editor.putBoolean("time" + (i), false);
        }
        editor.commit();
        if (isring) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour[0]);
            calendar.set(Calendar.MINUTE, minute[0]);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            }
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, AlarmReceive.class);
            intent.setAction("ALARM_ACTION"+i);
            clockposition=i;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),10 * 1000, pendingIntent);
            Toast.makeText(MainActivity.this, "将于"+hour[0]+":"+minute[0]+"闹钟响起", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(MainActivity.this, AlarmReceive.class);
            intent.setAction("ALARM_ACTION"+i);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            Toast.makeText(MainActivity.this, "取消闹钟", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_POINTER_UP:
                startActivity(new Intent(MainActivity.this, DialogActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        myGridAdapter = new MyGridAdapter(handler, MainActivity.this, cname, caddress, tname, tphone, temail, taddress);
        main_gridview.setAdapter(myGridAdapter);

    }
    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("Folder", "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }
}