package kuan.com.timetable.adapter;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import kuan.com.timetable.R;
import kuan.com.timetable.activity.DialogActivity;
import kuan.com.timetable.database.MySqlHelper;
import kuan.com.timetable.receiver.AlarmReceive;
import kuan.com.timetable.view.PanelAdapter;

import static kuan.com.timetable.R.id.tphone;
import static kuan.com.timetable.activity.AlarmAlert.clockposition;

/**
 * Created by kys-34 on 2016/12/6 0006.
 */
public class MyPanelAdapter extends PanelAdapter{
    private Context context;
    private String title;
    private List<String> time;
    private List<String> date;
    private List<List<List<String>>> info;

    private AlertDialog firstdialog, cinfodialog;
    private int position;

    private Handler handler;

    final int[] hour = {0};
    final int[] minute = {0};

    private static final int TIME_TYPE = 0;//第一列
    private static final int DATE_TYPE = 1;//第一行
    private static final int INFO_TYPE = 2;//实际数据
    private static final int TITLE_TYPE = 4;//左上角

    public MyPanelAdapter(Handler handler, Context context, String title, List<String> time, List<String> date, List<List<List<String>>> info) {
        this.handler = handler;
        this.context = context;
        this.title = title;
        this.time = time;
        this.date = date;
        this.info = info;
    }
//
//    private int clickrow,clickcolumn;
//    private InfoViewHolder infoViewHolder;
    @Override
    public int getRowCount() {
        return time.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return date.size() + 1;
    }

    @Override
    public int getItemViewType(int row, int column) {
        if (column == 0 && row == 0) {
            return TITLE_TYPE;
        }
        if (column == 0) {
            return TIME_TYPE;
        }
        if (row == 0) {
            return DATE_TYPE;
        }
        return INFO_TYPE;
    }

    //设置数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        int viewType = getItemViewType(row, column);
        switch (viewType) {
            case TITLE_TYPE:
                setTitleView((TitleViewHolder) holder);
                break;
            case DATE_TYPE:
                setDateView(column, (DateViewHolder) holder);
                break;
            case TIME_TYPE:
                setTimeView(row, (TimeViewHolder) holder);
                break;
            case INFO_TYPE:
                setInfoView(row, column, (InfoViewHolder) holder);
                break;
            default:
                setInfoView(row, column, (InfoViewHolder) holder);
        }
    }

    //设置标题内容
    private void setTitleView(TitleViewHolder holder) {
        holder.titleTextView.setText(title);
    }

    //设置第一行内容
    private void setDateView(int column, DateViewHolder holder) {
        if (column > 0) {
            holder.textview.setText(date.get(column - 1));
        }
    }

    //设置第一列内容
    private void setTimeView(final int row,final  TimeViewHolder holder) {
        if (row > 0) {
            holder.textview.setText(time.get(row - 1));
            holder.textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (row) {
                        case 1:
                            hour[0] = 7;
                            minute[0] = 30;
                            setTimeImage(holder,1);
                            break;
                        case 2:
                            hour[0] = 9;
                            minute[0] = 30;
                            setTimeImage(holder,2);
                            break;
                        case 3:
                            hour[0] = 13;
                            minute[0] = 30;
                            setTimeImage(holder,3);
                            break;
                        case 4:
                            hour[0] = 15;
                            minute[0] = 30;
                            setTimeImage(holder,4);
                            break;
                    }
                }
            });
        }
    }

    //设置实际内容的数据
    private void setInfoView(final int row, final int column, final InfoViewHolder holder) {
        if (row > 0 && column > 0) {
            Log.e("数据","row:"+row+"   column"+column);

            holder.maingrid_cname.setText(info.get(row - 1).get(column - 1).get(0));
            holder.maingrid_caddress.setText(info.get(row - 1).get(column - 1).get(1));
            holder.maingrid_tname.setText(info.get(row - 1).get(column - 1).get(2));
            holder.maingrid_tphone.setText(info.get(row - 1).get(column - 1).get(3));
            holder.maingrid_temail.setText(info.get(row - 1).get(column - 1).get(4));
            holder.maingrid_taddress.setText(info.get(row - 1).get(column - 1).get(5));

            holder.maingrid_linout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //如果界面信息为空
                    position=(row-1)*7+column-1;
//                    clickrow=row;
//                    clickcolumn=column;
//                    infoViewHolder=holder;
                    Log.e("修改",position+"");

                    View optionview = LayoutInflater.from(context).inflate(R.layout.dialog_option, null);

                    CardView indate = (CardView) optionview.findViewById(R.id.indate);
                    CardView call = (CardView) optionview.findViewById(R.id.call);
                    CardView email = (CardView) optionview.findViewById(R.id.email);
                    indate.setOnClickListener(new MyPanelAdapter.MyGridItem(1,row,column));
                    call.setOnClickListener(new MyPanelAdapter.MyGridItem(2,row,column));
                    email.setOnClickListener(new MyPanelAdapter.MyGridItem(3,row,column));

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(optionview);
                    firstdialog = builder.show();
                }
            });
            // 执行放大缩小操作
            holder.maingrid_linout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    context.startActivity(new Intent(context, DialogActivity.class));
                    return true;
                }
            });
        }
    }

    //根据不同的类型进行初始化View
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE_TYPE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.titleitem, parent, false));
            case TIME_TYPE:
                return new TimeViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.titleitem, parent, false));
            case DATE_TYPE:
                return new DateViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.titleitem, parent, false));
            case INFO_TYPE:
                return new InfoViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_maingrid, parent, false));
            default:
                break;
        }
        return new InfoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_maingrid, parent, false));
    }

    //对第一行View进行初始化
    private static class DateViewHolder extends RecyclerView.ViewHolder {
        public TextView textview;

        public DateViewHolder(View itemView) {
            super(itemView);
            this.textview = (TextView) itemView.findViewById(R.id.textview);
        }
    }

    //对第一列View进行初始化
    private static class TimeViewHolder extends RecyclerView.ViewHolder {
        public TextView textview;

        public TimeViewHolder(View itemView) {
            super(itemView);
            this.textview = (TextView) itemView.findViewById(R.id.textview);
        }
    }

    //对实际内容View进行初始化
    private static class InfoViewHolder extends RecyclerView.ViewHolder {
        LinearLayout maingrid_linout;
        TextView maingrid_cname, maingrid_caddress, maingrid_tname, maingrid_tphone, maingrid_temail, maingrid_taddress;

        public InfoViewHolder(View itemView) {
            super(itemView);
            this.maingrid_cname = (TextView) itemView.findViewById(R.id.maingrid_cname);
            this.maingrid_caddress = (TextView) itemView.findViewById(R.id.maingrid_caddress);
            this.maingrid_tname = (TextView) itemView.findViewById(R.id.maingrid_tname);
            this.maingrid_tphone = (TextView) itemView.findViewById(R.id.maingrid_tphone);
            this.maingrid_temail = (TextView) itemView.findViewById(R.id.maingrid_temail);
            this.maingrid_taddress = (TextView) itemView.findViewById(R.id.maingrid_taddress);
            this.maingrid_linout = (LinearLayout) itemView.findViewById(R.id.maingrid_linout);
        }
    }

    //对标题View进行初始化
    private static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public TitleViewHolder(View itemView) {
            super(itemView);
            this.titleTextView = (TextView) itemView.findViewById(R.id.textview);
        }
    }

    public void setTimeImage(TimeViewHolder holder,int i) {
        boolean isring = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences("TimeTable", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        i--;
        if (!sharedPreferences.getBoolean("time" + (i), false)) {
            holder.textview.setBackgroundResource(R.mipmap.clock);
            isring = true;
            editor.putBoolean("time" + (i), true);
        } else {
            holder.textview.setBackgroundResource(0);
            isring = false;
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
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceive.class);
            intent.setAction("ALARM_ACTION" + i);
            clockposition = i;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10 * 1000, pendingIntent);
            Toast.makeText(context, "将于" + hour[0] + ":" + minute[0] + "闹钟响起", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(context, AlarmReceive.class);
            intent.setAction("ALARM_ACTION" + i);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            Toast.makeText(context, "取消闹钟", Toast.LENGTH_SHORT).show();
        }
    }
    public class MyGridItem implements View.OnClickListener {
        private int option;
        private int row;
        private int column;
        public MyGridItem(int option,int row,int column) {
            this.option = option;
            this.row= row;
            this.column=column;
        }

        @Override
        public void onClick(View view) {
            firstdialog.dismiss();
            switch (option) {
                case 1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View classinfo = LayoutInflater.from(context).inflate(R.layout.adapter_gridclassinfo, null);
                    final EditText infocname = (EditText) classinfo.findViewById(R.id.cname);
                    final EditText infocaddress = (EditText) classinfo.findViewById(R.id.caddress);
                    final EditText infotname = (EditText) classinfo.findViewById(R.id.tname);
                    final EditText infotphone = (EditText) classinfo.findViewById(tphone);
                    final EditText infotemail = (EditText) classinfo.findViewById(R.id.temail);
                    final EditText infotaddress = (EditText) classinfo.findViewById(R.id.taddress);
                    Button infocommit = (Button) classinfo.findViewById(R.id.class_commit);
                    Button infocancel = (Button) classinfo.findViewById(R.id.class_cancel);
                    cinfodialog = builder.setView(classinfo).show();
                    infocommit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MySqlHelper mySqlHelper = new MySqlHelper(context);
                            SQLiteDatabase sqLiteDatabase = mySqlHelper.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("cname", infocname.getText().toString());
                            contentValues.put("caddress", infocaddress.getText().toString());
                            contentValues.put("tname", infotname.getText().toString());
                            contentValues.put("tphone", infotphone.getText().toString());
                            contentValues.put("temail", infotemail.getText().toString());
                            contentValues.put("taddress", infotaddress.getText().toString());
                            sqLiteDatabase.update("info", contentValues, "id" + "= ?",
                                    new String[]{Integer.toString(position)});
                            cinfodialog.dismiss();
                            handler.sendEmptyMessage(1);
//                            setInfoView(clickrow,clickcolumn,infoViewHolder);
//                            infoViewHolder.maingrid_cname.setText(infocname.getText().toString());
//                            infoViewHolder.maingrid_caddress.setText(infocaddress.getText().toString());
//                            infoViewHolder.maingrid_tname.setText(infotname.getText().toString());
//                            infoViewHolder.maingrid_tphone.setText(infotphone.getText().toString());
//                            infoViewHolder.maingrid_temail.setText(infotemail.getText().toString());
//                            infoViewHolder.maingrid_taddress.setText(infotaddress.getText().toString());
                        }
                    });
                    infocancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cinfodialog.dismiss();
                        }
                    });

                    break;
                case 2:
                    Log.e("电话",info.get(row-1).get(column-1).get(3)+"");
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + info.get(row-1).get(column-1).get(3)));
                    context.startActivity(intent);

                    break;
                case 3:
                    Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
                    context.startActivity(Intent.createChooser(myIntent, "请选择发送邮件的方式"));
                    break;
            }
        }
    }
}