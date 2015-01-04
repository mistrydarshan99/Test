package com.darshan.Test.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.darshan.Test.constants.DatabaseConstants;
import com.darshan.Test.model.UserModel;
import com.darshan.Test.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;

public class DBAdapter {

    private final Context mContext;
    private final DataBaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public DBAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DBAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToCreateDatabase");
        }

        return this;
    }

    public DBAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Utils.printLog(5, mSQLException.toString());
            throw mSQLException;
        }

        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void insertLogin(String userName, String password, String email, String fileName){
        ContentValues cv =new ContentValues();
        cv.put("varUsername",userName);
        cv.put("varPassword",password);
        cv.put("varEmail",email);
        cv.put("varImageName", fileName);
        mDb.insert(DatabaseConstants.TABLE_REGISTER, null, cv);
    }

    public int countUser(String userName){
        int count = 0;
        Cursor cursor = mDb.rawQuery("SELECT count(intGlCode) as count FROM Register WHERE varUsername='"+userName+"'",null);
        if (cursor != null && cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }
        }
        return count;
    }

    public int loginCompare(String userName, String passWord){
        int loginCount = 0;
        Cursor cursor = mDb.rawQuery("SELECT\n" +
                "  count(intGlCode) as count \n" +
                "FROM Register\n" +
                "WHERE varUsername = '"+userName+"' AND varPassword = '"+passWord+"'",null);
        if (cursor != null && cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                loginCount = cursor.getInt(cursor.getColumnIndex("count"));
            }
        }

        return loginCount;
    }

    public ArrayList<UserModel> getList(){
        ArrayList<UserModel> listLogin = null;
        Cursor cursor = mDb.rawQuery("select * from Register",null);
        if (cursor != null && cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                listLogin = new ArrayList<UserModel>();
                do {
                    UserModel obj = new UserModel();
                    obj.setAttachmentName(cursor.getString(cursor.getColumnIndex("varImageName")));
                    obj.setEmailName(cursor.getString(cursor.getColumnIndex("varUsername")));
                    listLogin.add(obj);
                }while (cursor.moveToNext());
            }
        }

        return listLogin;
    }

}