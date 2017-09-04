package com.mai.nix.maiapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mai.nix.maiapp.database.CacheBaseHelper;
import com.mai.nix.maiapp.database.DatabaseCursorWrapper;
import com.mai.nix.maiapp.database.ExamDbSchema.ExamTable;
import com.mai.nix.maiapp.model.ExamModel;
import com.mai.nix.maiapp.model.SubjectBody;
import com.mai.nix.maiapp.model.SubjectHeader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static com.mai.nix.maiapp.database.SubjectsDbSchema.*;

/**
 * Created by Nix on 28.08.2017.
 */

public class DataLab {
    private static DataLab sDataLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private DataLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CacheBaseHelper(mContext).getWritableDatabase();
    }
    public static DataLab get(Context context){
        if (sDataLab == null) {
            sDataLab = new DataLab(context);
        }
        return sDataLab;
    }
    public void addExam(ExamModel model){
        ContentValues contentValues = getExamContentValues(model);
        mDatabase.insert(ExamTable.NAME, null, contentValues);
    }
    public void addExams(List<ExamModel> models){
        for (ExamModel model : models){
            ContentValues c = getExamContentValues(model);
            mDatabase.insert(ExamTable.NAME, null, c);
        }
    }
    public void addHeaders(List<SubjectHeader> models){
        for(SubjectHeader header : models){
            ContentValues c = getHeadersContentValues(header);
            mDatabase.insert(HeadersTable.NAME, null, c);
        }
    }
    public void addHeader(SubjectHeader model){
        ContentValues contentValues = getHeadersContentValues(model);
        mDatabase.insert(HeadersTable.NAME, null, contentValues);
    }
    public void addBodies(List<SubjectBody> models){
        for(SubjectBody body : models){
            ContentValues c = getBodiesContentValues(body);
            mDatabase.insert(BodiesTable.NAME, null, c);
        }
    }
    public void clearExamsCache(){
        mDatabase.delete(ExamTable.NAME, null, null);
    }
    public void clearSubjectsCache(){
        mDatabase.delete(HeadersTable.NAME, null, null);
        mDatabase.delete(BodiesTable.NAME, null, null);
    }
    public List<ExamModel> getExams(){
        List<ExamModel> models = new ArrayList<>();
        DatabaseCursorWrapper cursor = queryExams(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                models.add(cursor.getExam());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return models;
    }
    public boolean isSubjectsTablesEmpty(){
        List<SubjectHeader> models = new ArrayList<>();
        DatabaseCursorWrapper cursor = queryHeaders(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                models.add(cursor.getHeader());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        if(models.size() > 0){
            return false;
        }else{
            return true;
        }
    }
    public boolean isExamsTableEmpty(){
        List<ExamModel> models = new ArrayList<>();
        DatabaseCursorWrapper cursor = queryExams(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                models.add(cursor.getExam());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        if(models.size() > 0){
            return false;
        }else{
            return true;
        }
    }
    public ArrayList<SubjectHeader> getHeaders(){
        ArrayList<SubjectHeader> models = new ArrayList<>();
        DatabaseCursorWrapper cursor = queryHeaders(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                models.add(cursor.getHeader());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return models;
    }
    public ArrayList<SubjectBody> getBodies(){
        ArrayList<SubjectBody> models = new ArrayList<>();
        DatabaseCursorWrapper cursor = queryBodies(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                models.add(cursor.getBody());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return models;
    }
    public ArrayList<SubjectBody> getBodies(UUID id){
        ArrayList<SubjectBody> models = new ArrayList<>();
        DatabaseCursorWrapper cursor = queryBodies(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if(cursor.getBody().getUuid() == id){
                    models.add(cursor.getBody());
                    cursor.moveToNext();
                }else{
                    cursor.moveToNext();
                }
                models.add(cursor.getBody());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return models;
    }
    private static ContentValues getExamContentValues(ExamModel model){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExamTable.Cols.DATE, model.getDate());
        contentValues.put(ExamTable.Cols.DAY, model.getDay());
        contentValues.put(ExamTable.Cols.TIME, model.getTime());
        contentValues.put(ExamTable.Cols.TITLE, model.getTitle());
        contentValues.put(ExamTable.Cols.TEACHER, model.getTeacher());
        contentValues.put(ExamTable.Cols.ROOM, model.getRoom());
        return contentValues;
    }
    private static ContentValues getHeadersContentValues(SubjectHeader header){
        ContentValues contentValues = new ContentValues();
        contentValues.put(HeadersTable.Cols.UUID, header.getUuid().toString());
        contentValues.put(HeadersTable.Cols.DATE, header.getDate());
        contentValues.put(HeadersTable.Cols.DAY, header.getDay());
        return contentValues;
    }
   private static ContentValues getBodiesContentValues(SubjectBody body){
        ContentValues contentValues = new ContentValues();
        contentValues.put(BodiesTable.Cols.HEADER_UUID, body.getUuid().toString());
        contentValues.put(BodiesTable.Cols.TIME, body.getTime());
        contentValues.put(BodiesTable.Cols.TYPE, body.getType());
        contentValues.put(BodiesTable.Cols.TITLE, body.getTitle());
        contentValues.put(BodiesTable.Cols.TEACHER, body.getTeacher());
        contentValues.put(BodiesTable.Cols.ROOM, body.getRoom());
        return contentValues;
    }
    private DatabaseCursorWrapper queryExams(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ExamTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        cursor.close();
        return new DatabaseCursorWrapper(cursor);
    }
    private DatabaseCursorWrapper queryHeaders(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                HeadersTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        //cursor.close();
        return new DatabaseCursorWrapper(cursor);
    }
    private DatabaseCursorWrapper queryBodies(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                BodiesTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        cursor.close();
        return new DatabaseCursorWrapper(cursor);
    }
}
