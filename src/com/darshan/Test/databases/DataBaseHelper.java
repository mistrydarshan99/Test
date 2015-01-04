package com.darshan.Test.databases;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.darshan.Test.constants.DatabaseConstants;
import com.darshan.Test.utils.Utils;

import java.io.File;
import java.io.IOException;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mDataBase;

    public DataBaseHelper(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null,
                DatabaseConstants.DATABASE_VERSION);
        DatabaseConstants.DATABASE_PATH
                = "/data/data/" + context.getPackageName() + "/databases/";
    }

    public void createDataBase() throws IOException {
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase mCheckDataBase = null;
        try {
            String mPath = DatabaseConstants.DATABASE_PATH
                    + DatabaseConstants.DATABASE_NAME;
            File pathFile = new File(mPath);
            if (pathFile.exists()) {
                mCheckDataBase = SQLiteDatabase.openDatabase(mPath, null,
                        SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            }
        } catch (SQLiteException mSQLiteException) {
            Utils.printLog(5, "DatabaseNotFound " + mSQLiteException.toString());
        }

        if (mCheckDataBase != null) {
            mCheckDataBase.close();
        }

        return mCheckDataBase != null;
    }

    public boolean openDataBase() throws SQLException {
        String mPath = DatabaseConstants.DATABASE_PATH
                + DatabaseConstants.DATABASE_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        super.close();
        if (mDataBase != null) {
            mDataBase.close();
        }
        SQLiteDatabase.releaseMemory();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DatabaseConstants.DROP + DatabaseConstants.TABLE_REGISTER);
        db.execSQL(DatabaseConstants.CREATE + DatabaseConstants.TABLE_REGISTER + "( "
                + DatabaseConstants.COLUMN_INTGLCODE + " INTEGER PRIMARY KEY, "
                + DatabaseConstants.COLUMN_USERNAME + " TEXT, "
                + DatabaseConstants.COLUMN_PASSWORD + " TEXT, "
                + DatabaseConstants.COLUMN_EMAIL + " TEXT, "
                + DatabaseConstants.COLUMN_VARIMAGENAME + " TEXT  )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}