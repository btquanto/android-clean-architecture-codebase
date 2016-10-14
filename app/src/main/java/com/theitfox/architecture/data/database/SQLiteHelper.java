package com.theitfox.architecture.data.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.theitfox.architecture.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by btquanto on 05/08/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "com.interush.irisstudio.db";
    private static final int VERSION = 1;
    private Context context;

    /* Private constructors force using `newInstance` method */
    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.context = context;
    }

    /**
     * Create a new SQLiteHelper instance
     * This method is more convenient than using the constructor
     *
     * @param context the context
     * @return the sq lite helper
     */
    public static SQLiteHelper newInstance(Context context) {
        return new SQLiteHelper(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Upgrade from version 0 to current version
        onUpgrade(sqLiteDatabase, 0, VERSION);
    }

    /**
     * When doing database migration, follow these steps:
     * 1. Increase the version number
     * 2. Write migration code in assets/db/<version_number>.sql using sql
     * 3. Sometimes the migration involves some coding, implement them in the switch block
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AssetManager am = this.context.getAssets();
        try {
            db.beginTransaction();
            for (int version = oldVersion + 1; version <= newVersion; version++) {
                InputStream is = null;

                // Execute any migration scripts in assets/db/
                // The scripts should be named `{version number}.sql`
                // For example: 2.sql, 3.sql...
                try {
                    is = am.open(String.format("db/%d.sql", version));
                } catch (IOException e) {
                }
                if (is != null) {
                    String sql = FileUtils.readStream(is);
                    db.execSQL(sql);
                }

                // Skeleton for migrating database, to be added if necessary
                switch (version) {
                    case 2:
                        // Code to upgrade from version 1 to 2
                        break;
                    case 3:
                        // Code to upgrade from version 2 to 3
                        break;
                    case 4:
                        // Code to upgrade from version 3 to 4
                        break;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
