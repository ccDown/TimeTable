package kuan.com.timetable.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import kuan.com.timetable.PanelActivity;
import kuan.com.timetable.R;
import kuan.com.timetable.database.MySqlHelper;

/**
 * Created by kys-34 on 2016/12/3 0003.
 */

public class LoadActicity extends Activity {
    private static final int LOAD_DISPLAY_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bmob.initialize(this, "b57cd38e5c9d9052bf340fbc207041d4");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 允许用户使用应用
                    startActivity(new Intent(LoadActicity.this, PanelActivity.class));
                    finish();
                }
            },LOAD_DISPLAY_TIME);
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MySqlHelper mySqlHelper=new MySqlHelper(LoadActicity.this);
                    mySqlHelper.getWritableDatabase();
                    for (int i=0;i<28;i++){
                        mySqlHelper.toinsert(i,"","","","","","");
                    }
                    startActivity(new Intent(LoadActicity.this, LoginActivity.class));
                    finish();
                }
            },LOAD_DISPLAY_TIME);
        }


    }
}
