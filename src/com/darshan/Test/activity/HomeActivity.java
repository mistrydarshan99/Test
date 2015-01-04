package com.darshan.Test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.darshan.Test.R;
import com.darshan.Test.TestApp;

public class HomeActivity extends Activity {

    private TestApp testObjApp;
    private String userName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        testObjApp = (TestApp) this.getApplicationContext();

        if (getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
        }
    }

   public void doUserList(View view){
       Intent intent = new Intent(HomeActivity.this,UserListActivity.class);
       intent.putExtra("UserName",userName);
       startActivity(intent);
   }

    public void doLogout(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        testObjApp.setLoginPreference(false);
        final Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}
