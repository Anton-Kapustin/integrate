package com.dev.toxa.integrate.db;

import android.provider.BaseColumns;

public class DbReaderContract {

    public DbReaderContract() {}

    public static abstract class dbEntry implements BaseColumns {
        public static final String TABLE_NAME = "DATA";
        public static final String SERVER_NAME = "SERVER_NAME";
        public static final String IP_ADDRESS = "IP";
        public static final String DISTR_NAME = "DISTR";
        public static final String FAVORITE = "FAVORITE";
        public static final String LAST_USE = "LAST_USE";
    }

    public static abstract class settingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "SETTINGS";
        public static final String NOTIFY_ENABLED = "NOTIFY_ENABLED";
    }
}
