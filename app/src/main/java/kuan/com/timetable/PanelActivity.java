package kuan.com.timetable;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import kuan.com.timetable.activity.HomeWorkstep;
import kuan.com.timetable.activity.LoginActivity;
import kuan.com.timetable.adapter.MyPanelAdapter;
import kuan.com.timetable.base.BaseActivity;
import kuan.com.timetable.database.MySqlHelper;
import kuan.com.timetable.view.ScrollablePanel;

/**
 * Created by kys-34 on 2016/12/6 0006.
 */

public class PanelActivity extends BaseActivity implements View.OnClickListener {
    private TextView main_exit, main_setting;
    public static ScrollablePanel main_gridview;
    private MyPanelAdapter myGridAdapter;
    private List<String> panelTime, panelData;
    private List<List<List<String>>> panelrow;
    private List<String> panelinfo;
    private List<List<String>> panelcolumn;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            cursor();
            startActivity(new Intent(PanelActivity.this, PanelActivity.class));
            finish();
            Log.e("进行","开始");
//            myGridAdapter = new MyPanelAdapter(handler, PanelActivity.this, "时间/星期", panelTime, panelData, panelrow);
//            main_gridview.setPanelAdapter(myGridAdapter);
            Log.e("进行","结束");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        initViews();
        initData();
    }

    public void initViews() {
        main_gridview = (ScrollablePanel) findViewById(R.id.main_panelView);
        main_setting = (TextView) findViewById(R.id.setting);
        main_exit = (TextView) findViewById(R.id.exit);
    }

    public void initData() {
        panelTime = new ArrayList<>();
        panelTime.add("8:00");
        panelTime.add("10:00");
        panelTime.add("14:00");
        panelTime.add("16:00");

        panelData = new ArrayList<>();
        panelData.add("日");
        panelData.add("一");
        panelData.add("二");
        panelData.add("三");
        panelData.add("四");
        panelData.add("五");
        panelData.add("六");
        main_exit.setOnClickListener(this);
        main_setting.setOnClickListener(this);
        cursor();
        myGridAdapter = new MyPanelAdapter(handler, PanelActivity.this, "时间/星期", panelTime, panelData, panelrow);
        main_gridview.setPanelAdapter(myGridAdapter);
    }

    public void cursor() {
        MySqlHelper mySqlHelper = new MySqlHelper(PanelActivity.this);
        SQLiteDatabase sqLiteDatabase = mySqlHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * From info ", null);
        panelrow = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToNext();
                int row = 0;
                while (row < 4) {
                    int column = 0;
                    panelcolumn = new ArrayList<>();
                    while (column < 7) {
                        panelinfo = new ArrayList<>();
                        panelinfo.add(cursor.getString(1));
                        panelinfo.add(cursor.getString(2));
                        panelinfo.add(cursor.getString(3));
                        panelinfo.add(cursor.getString(4));
                        panelinfo.add(cursor.getString(5));
                        panelinfo.add(cursor.getString(6));
                        column++;
                        cursor.moveToNext();
                        panelcolumn.add(panelinfo);
                    }
                    panelrow.add(panelcolumn);
                    row++;
                }
            cursor.close();
        }
    }

    //点击左侧时间栏
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.setting:
                startActivity(new Intent(PanelActivity.this, HomeWorkstep.class));
                break;

            case R.id.exit:
                BmobUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                startActivity(new Intent(PanelActivity.this, LoginActivity.class));
                finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myGridAdapter = new MyPanelAdapter(handler, PanelActivity.this, "时间/星期", panelTime, panelData, panelrow);
        main_gridview.setPanelAdapter(myGridAdapter);
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