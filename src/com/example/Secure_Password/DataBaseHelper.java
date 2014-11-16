package com.example.Secure_Password;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Camisado on 25.05.2014.
 */
public class DataBaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_NAME = "passwords.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "reminder";
    public static final String UID = "_id";
    public static final String NAME = "name";
    public static final String RESOURCE_LINK = "resourceLink";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String DESCRIPTION = "description";

    private static final String SQL_CREATE_ENTRIES = "create table " + TABLE_NAME + " ("
            + DataBaseHelper._ID + " integer primary key autoincrement,"
            + NAME + " text not null,"
            + RESOURCE_LINK + " text not null,"
            + LOGIN + " text not null,"
            + PASSWORD + " text not null,"
            + DESCRIPTION + " text" + ");";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    public  DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("LOG_TAG", "Обновление базы данных с версии " + oldVersion
                + " до версии " + newVersion + ", которое удалит все старые данные");
        // Удаляем предыдущую таблицу при апгрейде
        db.execSQL(SQL_DELETE_ENTRIES);
        // Создаём новый экземпляр таблицы
        onCreate(db);
    }
}
