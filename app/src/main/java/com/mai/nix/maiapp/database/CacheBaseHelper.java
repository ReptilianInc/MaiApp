package com.mai.nix.maiapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mai.nix.maiapp.database.ExamDbSchema.ExamTable;
import com.mai.nix.maiapp.database.SubjectsDbSchema.BodiesTable;
import com.mai.nix.maiapp.database.SubjectsDbSchema.HeadersTable;

/**
 * Created by Nix on 28.08.2017.
 */

public class CacheBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "cacheBase.db";
    public CacheBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + ExamTable.NAME + "(" +
        " _id integer primary key autoincrement, " +
        ExamTable.Cols.DATE + ", " +
        ExamTable.Cols.DAY + ", " +
        ExamTable.Cols.TIME + ", " +
        ExamTable.Cols.TITLE + ", " +
        ExamTable.Cols.TEACHER + ", " +
        ExamTable.Cols.ROOM + ")");

        sqLiteDatabase.execSQL(
        "create table " + HeadersTable.NAME + "(" +
        " _id integer primary key autoincrement, " +
        HeadersTable.Cols.UUID + ", " +
        HeadersTable.Cols.DATE + ", " +
        HeadersTable.Cols.DAY + ")");

        sqLiteDatabase.execSQL("create table " + BodiesTable.NAME + "(" +
        " _id integer primary key autoincrement, " +
        BodiesTable.Cols.HEADER_UUID + ", " +
        BodiesTable.Cols.TIME + ", " +
        BodiesTable.Cols.TYPE+ ", " +
        BodiesTable.Cols.TITLE + ", " +
        BodiesTable.Cols.TEACHER + ", " +
        BodiesTable.Cols.ROOM + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
