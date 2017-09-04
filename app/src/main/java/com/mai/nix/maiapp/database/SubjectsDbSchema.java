package com.mai.nix.maiapp.database;

/**
 * Created by Nix on 30.08.2017.
 */

public class SubjectsDbSchema {
    public static final class HeadersTable{

        public static final String NAME = "headers";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String DAY = "day";
        }
    }

    public static final class BodiesTable{

        public static final String NAME = "bodies";

        public static final class Cols{
            public static final String HEADER_UUID = "header_uuid";
            public static final String TIME = "time";
            public static final String TYPE = "type";
            public static final String TITLE = "title";
            public static final String TEACHER = "teacher";
            public static final String ROOM = "room";
        }
    }
}
