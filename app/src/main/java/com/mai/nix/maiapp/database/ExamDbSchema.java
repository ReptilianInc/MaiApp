package com.mai.nix.maiapp.database;

/**
 * Created by Nix on 28.08.2017.
 */

@Deprecated
public class ExamDbSchema {
    public static final class ExamTable{
        public static final String NAME = "exams";

        public static final class Cols{
            public static final String DATE = "date";
            public static final String DAY = "day";
            public static final String TIME = "time";
            public static final String TITLE = "title";
            public static final String TEACHER = "teacher";
            public static final String ROOM = "room";
        }
    }
}
