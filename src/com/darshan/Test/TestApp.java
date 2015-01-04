package com.darshan.Test;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.darshan.Test.constants.Constants;
import com.darshan.Test.databases.DBAdapter;

public class TestApp extends Application {

    private int androidVersion = 4;
    private DBAdapter dbAdapter = null;
    private SharedPreferences login = null;
    private SharedPreferences.Editor prefEditor = null;

    @Override
    public void onCreate() {
        super.onCreate();

        setAndroidVersion(android.os.Build.VERSION.SDK_INT);
        if(dbAdapter == null) {
            dbAdapter = new DBAdapter(getBaseContext());
            dbAdapter.createDatabase();
        }

        if(login == null) {
            login = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
            prefEditor = login.edit();
        }
    }

    public int getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(int androidVersion) {
        this.androidVersion = androidVersion;
    }

    public DBAdapter getTAPLDatabase() {
        dbAdapter.open();
        return dbAdapter;
    }

    public void closeTAPLDataBase() {
        dbAdapter.close();
    }

    public void setLoginPreference(final boolean isAlreadyLogin) {
        if(prefEditor != null) {
            prefEditor.putBoolean(Constants.PREF_LOGIN_KEY, isAlreadyLogin);
            prefEditor.commit();
        }
    }

    public boolean getLoginPreference() {
        if(login == null) {
            return false;
        } else {
            return login.getBoolean(Constants.PREF_LOGIN_KEY, false);
        }
    }

}
