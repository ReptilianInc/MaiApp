package com.mai.nix.maiapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mai.nix.maiapp.model.SubjectBody;
import com.mai.nix.maiapp.model.SubjectHeader;

import java.util.List;

@Dao
public interface SubjectsDao {

    @Query("SELECT * FROM days")
    List<SubjectHeader> getAllSubjectHeaders();

    @Query("SELECT * FROM subjects")
    List<SubjectBody> getAllSubjectBodies();

    @Query("SELECT * FROM subjects WHERE subject_id = :id")
    List<SubjectBody> getById(long id);

    @Insert
    long insertSubjectHeader(SubjectHeader subjectHeader);

    @Insert
    void insertSubjectBody(SubjectBody subjectBody);

    @Delete
    void clearSubjectBodies(List<SubjectBody> subjectBodies);

    @Delete
    void clearSubjectHeaders(List<SubjectHeader> subjectHeaders);
}
