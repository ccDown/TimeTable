package kuan.com.timetable.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kys-34 on 2016/12/4 0004.
 */

public class MySqlHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TimeTable.db";
    public String TABLE_NAME = " info ";
    public String ID = " id ";
    public String Cname = " cname ";
    public String Caddress = " caddress ";
    public String Tname = " tname ";
    public String Tphone = " tphone ";
    public String Temail = " temail ";
    public String Taddress = " taddress ";

    public MySqlHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String timesql = "Create Table" + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Cname + " TEXT ,"+ Caddress +" TEXT ," + Tname + " TEXT ," + Tphone + " TEXT ," + Temail + " TEXT ," + Taddress + " TEXT );";
        String homesql= "Create Table home (ID  INTEGER PRIMARY KEY AUTOINCREMENT,WORk TEXt)";
        sqLiteDatabase.execSQL(timesql);
        sqLiteDatabase.execSQL(homesql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long toinsert(int id,String cname,String caddress,String tname,String tphone,String temail,String taddress){
        SQLiteDatabase db=MySqlHelper.this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID,id);
        cv.put(Cname, cname);
        cv.put(Caddress,caddress);
        cv.put(Tname,tname);
        cv.put(Tphone,tphone);
        cv.put(Temail,temail);
        cv.put(Taddress,taddress);
        long row=db.insert("info",null,cv);
        return row;
    }
}
