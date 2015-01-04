package com.darshan.Test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.darshan.Test.R;
import com.darshan.Test.TestApp;

public class LoginActivity extends Activity {

    private EditText edtUser, edtPassword;

    private TestApp testObjApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        testObjApp = (TestApp) this.getApplicationContext();

        boolean isLogin = testObjApp.getLoginPreference();
        if (isLogin) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        initComponent();
    }

    private void initComponent() {
        edtUser = (EditText) findViewById(R.id.etUsername);
        edtPassword = (EditText) findViewById(R.id.etPassword);
    }

    public void doSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this,Sign_UpActivity.class);
        startActivity(intent);
    }

    private void callHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("UserName", edtUser.getText().toString().trim());
        startActivity(intent);
    }

    public void doLogin(View view) {
        if (edtUser.getText().toString().trim().length() > 0 && edtPassword.getText().toString().trim().length() > 0) {
            int loginCount = testObjApp.getTAPLDatabase().loginCompare(edtUser.getText().toString().trim(), edtPassword.getText().toString().trim());
            if (loginCount > 0) {
                testObjApp.setLoginPreference(true);
                callHome();
            } else {
                Toast.makeText(LoginActivity.this, "Enter Valid UserName and Password", Toast.LENGTH_SHORT).show();
            }
            testObjApp.closeTAPLDataBase();
        } else {
            if (edtUser.getText().toString().trim().length() == 0) {
                Toast.makeText(LoginActivity.this, "Enter Valid UserName", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Enter Valid email", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
