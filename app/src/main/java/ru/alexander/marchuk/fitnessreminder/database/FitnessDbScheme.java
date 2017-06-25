package ru.alexander.marchuk.fitnessreminder.database;

public class FitnessDbScheme {

    public static final class FitnessTable {
        public static final String NAME = "fitness";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String STATUS = "status";
            public static final String TIMESTAMP = "time_stamp";
        }
    }
}
