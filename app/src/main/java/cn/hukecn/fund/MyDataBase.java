package cn.hukecn.fund;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kelson on 2015/11/9.
 */
public class MyDataBase extends SQLiteOpenHelper {

    private static String DBNAME = "FUNDDB";
    private static String ID="id";
    private static String TABLE_NAME = "myfund";
    private static String FUNDID = "fundid";
    private static String FUNDNAME = "fundname";

    private static String MONEY = "money";
    private static int DB_VERSION = 3;
    private static String CREAT_TABLE = "CREATE TABLE "+TABLE_NAME+" (_id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,fundid TEXT  NOT NULL,money TEXT  NOT NULL,fundname TEXT  NOT NULL)";
    private SQLiteDatabase db;

    public MyDataBase(Context context) {
        this(context, DBNAME, null,DB_VERSION);
    }

    public MyDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBNAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist "+TABLE_NAME);
        onCreate(db);
    }

    public long insert(InsertBDBean bean)
    {
        db = getWritableDatabase();
        ContentValues valus = new ContentValues();
        valus.put(FUNDID,bean.fundid);
        valus.put(MONEY, bean.money);
        valus.put(FUNDNAME,bean.fundname);
        long ret = db.insert(TABLE_NAME, null, valus);
        db.close();
        return ret;
    }

    public List<InsertBDBean> query()
    {
        db = getReadableDatabase();
        List<InsertBDBean> list = new ArrayList<InsertBDBean>();

        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{"_id", FUNDID, MONEY,FUNDNAME},
                null,
                null, null, null, "_id desc");
        while (cursor.moveToNext())
        {
            InsertBDBean bean = new InsertBDBean();
            bean.fundid = cursor.getString(1);
            bean.money = cursor.getString(2);
            bean.fundname = cursor.getString(3);
            list.add(bean);
        }

        if(list.size() == 0)
            return null;
        cursor.close();
        db.close();
        Collections.reverse(list);
        return list;
    }

    public float quary(String fundid)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor =db.rawQuery("select * from " + TABLE_NAME + " where " + FUNDID + " like ?", new String[]{"%" + fundid + "%"});
        String money = null;
        while (cursor.moveToNext()) {
            money = cursor.getString(2);
        }

        cursor.close();

        if(money != null)
            return Float.valueOf(money);
        else
            return 0f;
    }

    public int delete(String fundid)
    {
        SQLiteDatabase db = getWritableDatabase();

        int return_code = db.delete(TABLE_NAME, FUNDID+"=?", new String[]{fundid});

        db.close();

        return  return_code;
    }
}
