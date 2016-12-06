package kuan.com.timetable.adapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kuan.com.timetable.R;
import kuan.com.timetable.database.MySqlHelper;

import static kuan.com.timetable.MainActivity.getViewBitmap;
import static kuan.com.timetable.R.id.maingrid_tname;

/**
 * Created by kys-34 on 2016/12/2 0002.
 */

public class MyGridAdapter extends BaseAdapter {
    private Context context;
    private String[] cname, caddress, tname, tphone, temail, taddress;
    private AlertDialog firstdialog, cinfodialog, emaildialog,longdialog;
    private int position;
    private ViewHolder viewHolder;
    private Handler handler;


    public MyGridAdapter(Handler handler, Context context, String[] cname, String[] caddress, String[] tname, String[] tphone, String[] temail, String[] taddress) {
        this.handler = handler;
        this.context = context;
        this.cname = cname;
        this.caddress = caddress;
        this.tname = tname;
        this.tphone = tphone;
        this.temail = temail;
        this.taddress = taddress;
    }

    @Override
    public int getCount() {
        return cname.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_maingrid, null);
            viewHolder.maingrid_cname = (TextView) view.findViewById(R.id.maingrid_cname);
            viewHolder.maingrid_caddress = (TextView) view.findViewById(R.id.maingrid_caddress);
            viewHolder.maingrid_tname = (TextView) view.findViewById(maingrid_tname);
            viewHolder.maingrid_tphone = (TextView) view.findViewById(R.id.maingrid_tphone);
            viewHolder.maingrid_temail = (TextView) view.findViewById(R.id.maingrid_temail);
            viewHolder.maingrid_taddress = (TextView) view.findViewById(R.id.maingrid_taddress);
            viewHolder.maingrid_linout = (LinearLayout) view.findViewById(R.id.maingrid_linout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.maingrid_cname.setText(cname[i]);
        viewHolder.maingrid_caddress.setText(caddress[i]);
        viewHolder.maingrid_tname.setText(tname[i]);
        viewHolder.maingrid_tphone.setText(tphone[i]);
        viewHolder.maingrid_temail.setText(temail[i]);
        viewHolder.maingrid_taddress.setText(taddress[i]);

        viewHolder.maingrid_linout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果界面信息为空
                Log.e("顺序", i + "");
                position = i;
                View optionview = LayoutInflater.from(context).inflate(R.layout.dialog_option, null);

                CardView indate = (CardView) optionview.findViewById(R.id.indate);
                CardView call = (CardView) optionview.findViewById(R.id.call);
                CardView email = (CardView) optionview.findViewById(R.id.email);
                indate.setOnClickListener(new MyGridItem(1));
                call.setOnClickListener(new MyGridItem(2));
                email.setOnClickListener(new MyGridItem(3));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(optionview);
                firstdialog = builder.show();
            }
        });

       // 执行放大缩小操作
        viewHolder.maingrid_linout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View longclickview = LayoutInflater.from(context).inflate(R.layout.dialog_longclick, null);
                ImageView imageView = (ImageView) longclickview.findViewById(R.id.image);
                Button button=(Button)longclickview.findViewById(R.id.long_commit);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        longdialog.dismiss();
                    }
                });
                imageView.setBackground(new BitmapDrawable(getViewBitmap(view)));
                longdialog=new AlertDialog.Builder(context).setView(longclickview).show();
                return true;
            }
        });

        return view;
    }

    public class ViewHolder {
        LinearLayout maingrid_linout;
        TextView maingrid_cname, maingrid_caddress, maingrid_tname, maingrid_tphone, maingrid_temail, maingrid_taddress;
    }

    private class MyGridItem implements View.OnClickListener {
        private int option;

        public MyGridItem(int option) {
            this.option = option;
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
                    final EditText infotphone = (EditText) classinfo.findViewById(R.id.tphone);
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
                            int a = sqLiteDatabase.update("info", contentValues, "id" + "= ?", new String[]{Integer.toString(position)});
                            Log.e("信息", "" + a);
                            cinfodialog.dismiss();
                            handler.sendEmptyMessage(1);

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
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tphone[position]));
                    context.startActivity(intent);

                    break;
                case 3:

                    AlertDialog.Builder emailbuilder = new AlertDialog.Builder(context);
                    View emailinfi = LayoutInflater.from(context).inflate(R.layout.activity_mainitem, null);
                    emailbuilder.setView(emailinfi);
                    final EditText emailreceiver = (EditText) emailinfi.findViewById(R.id.email_receiver);
                    final EditText email_main = (EditText) emailinfi.findViewById(R.id.email_main);
                    final EditText email_body = (EditText) emailinfi.findViewById(R.id.email_body);
                    final EditText email_sender = (EditText) emailinfi.findViewById(R.id.email_sender);

                    Button cancel_email = (Button) emailinfi.findViewById(R.id.cancel_email);
                    Button commit_email = (Button) emailinfi.findViewById(R.id.commit_email);
                    emaildialog = emailbuilder.show();
                    cancel_email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            emaildialog.dismiss();
                        }
                    });
                    commit_email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String[] reciver = new String[]{emailreceiver.getText().toString()};
                            String[] mySbuject = new String[]{email_main.getText().toString()};
                            String myCc = email_sender.getText().toString();
                            String mybody = email_body.getText().toString();
                            Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
                            myIntent.setType("plain/text");
                            myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
                            myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
                            myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);
                            myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);
                            context.startActivity(Intent.createChooser(myIntent, "请选择发送邮件的方式"));
                            emaildialog.dismiss();
                        }
                    });

                    break;
            }
        }
    }
}
