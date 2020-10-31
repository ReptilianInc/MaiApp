package com.mai.nix.maiapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.mai.nix.maiapp.model.ExamModel;
import com.mai.nix.maiapp.model.Subject;
import com.mai.nix.maiapp.model.Day;
import static com.mai.nix.maiapp.database.ExamDbSchema.*;
import static com.mai.nix.maiapp.database.SubjectsDbSchema.*;

/**
 * Created by Nix on 28.08.2017.
 */
@Deprecated
public class DatabaseCursorWrapper extends CursorWrapper {
    public DatabaseCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public ExamModel getExam(){
        ExamModel examModel = new ExamModel();
        examModel.setDate(getString(getColumnIndex(ExamTable.Cols.DATE)));
        examModel.setDay(getString(getColumnIndex(ExamTable.Cols.DAY)));
        examModel.setTime(getString(getColumnIndex(ExamTable.Cols.TIME)));
        examModel.setTitle(getString(getColumnIndex(ExamTable.Cols.TITLE)));
        examModel.setTeacher(getString(getColumnIndex(ExamTable.Cols.TEACHER)));
        examModel.setRoom(getString(getColumnIndex(ExamTable.Cols.ROOM)));
        return examModel;
    }
    public Day getHeader(){
        Day header = new Day();
        //header.setDate(getString(getColumnIndex(HeadersTable.Cols.DATE)));
        //header.setDay(getString(getColumnIndex(HeadersTable.Cols.DAY)));
        return header;
    }
    public Subject getBody(){
        Subject body = new Subject();
        //body.setUuid(UUID.fromString(getString(getColumnIndex(BodiesTable.Cols.HEADER_UUID))));
        //body.setTime(getString(getColumnIndex(BodiesTable.Cols.TIME)));
        //body.setType(getString(getColumnIndex(BodiesTable.Cols.TYPE)));
        //body.setTitle(getString(getColumnIndex(BodiesTable.Cols.TITLE)));
        //body.setTeacher(getString(getColumnIndex(BodiesTable.Cols.TEACHER)));
        //body.setRoom(getString(getColumnIndex(BodiesTable.Cols.ROOM)));
        return body;
    }
}
