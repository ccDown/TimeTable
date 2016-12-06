package kuan.com.timetable.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyachi.stepview.VerticalStepView;

import java.util.ArrayList;
import java.util.List;

import kuan.com.timetable.R;
import kuan.com.timetable.database.MySqlHelper;

/**
 * Created by kys-34 on 2016/12/4 0004.
 */

public class HomeWorkstep extends Activity implements View.OnClickListener {
    private List<String> list = new ArrayList<>();
    private MySqlHelper mySqlHelper;
    private SQLiteDatabase sqLiteDatabase;
    private VerticalStepView stepView;
    private AlertDialog dialog;
    private EditText homework, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        TextView add_homework = (TextView) findViewById(R.id.add_homework);
        TextView back_main = (TextView) findViewById(R.id.back_main);
        add_homework.setOnClickListener(this);
        back_main.setOnClickListener(this);
        stepView = (VerticalStepView) findViewById(R.id.step_view);
        mySqlHelper = new MySqlHelper(HomeWorkstep.this);
        sqLiteDatabase = mySqlHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * From home", null);
        if (cursor != null) {
            int i = 0;
            while (cursor.moveToNext()) {
                list.add(cursor.getString(1));
                i++;
            }
        }
        stepView.setStepViewTexts(list)//总步骤
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(HomeWorkstep.this, android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(HomeWorkstep.this, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(HomeWorkstep.this, android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(HomeWorkstep.this, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(HomeWorkstep.this, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(HomeWorkstep.this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(HomeWorkstep.this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_main:
                finish();
                break;
            case R.id.add_homework:
                View itemview = LayoutInflater.from(HomeWorkstep.this).inflate(R.layout.activity_tabletime, null);
                homework = (EditText) itemview.findViewById(R.id.homework);
                time = (EditText) itemview.findViewById(R.id.time);
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeWorkstep.this);
                dialog = builder.setView(itemview).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        list.add("作业:" + homework.getText().toString() + "\n提交时间:" + time.getText().toString());
                        TextView add_thiswork = (TextView) findViewById(R.id.add_thiswork);
                        add_thiswork.setText("作业:" + homework.getText().toString() + "\n提交时间:" + time.getText().toString());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("WORK", "作业:" + homework.getText().toString() + "\n提交时间:" + time.getText().toString());
                        sqLiteDatabase.insert("home", null, contentValues);
                        cancledialog();
                        Toast.makeText(HomeWorkstep.this, "作业编写完成,记得来看时间啊", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancledialog();
                    }
                }).show();

                break;
        }
    }

    public void cancledialog() {
        dialog.dismiss();
    }

}
