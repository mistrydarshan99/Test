package com.darshan.Test.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import com.darshan.Test.R;
import com.darshan.Test.TestApp;
import com.darshan.Test.adapter.UserListAdapter;
import com.darshan.Test.databases.DBAdapter;
import com.darshan.Test.model.UserModel;
import com.darshan.Test.service.ServiceHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UserListActivity extends Activity {

    private TestApp loginObj;
    private String userName = "";
    private ListView mailListview;
    private UserListAdapter adapter;
    private ArrayList<UserModel> messageDemo;
    private DBAdapter db;
    private ProgressDialog pDialog;
    private static String url = "http://test.fortalented.com/app_api/get_all_photos.php?talent_id=1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        loginObj = (TestApp)this.getApplicationContext();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
        }
        db = new DBAdapter(this);

        mailListview = (ListView)findViewById(R.id.mailListview);
        new BaseTask().execute();

    }

    public class BaseTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            db.open();
            messageDemo = db.getList();
            db.close();
//            parseJson();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            adapter = new UserListAdapter(UserListActivity.this, messageDemo);
            mailListview.setAdapter(adapter);
        }
    }

    private void parseJson(){
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.GET);
        if (jsonStr != null && jsonStr.length() > 0){
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                String status = jsonObject.optString("status");
                if (status.equalsIgnoreCase("success")){
                    JSONArray jsonArray = jsonObject.optJSONArray("photos");
                    if (jsonArray != null && jsonArray.length() > 0){
                        messageDemo = new ArrayList<UserModel>();
                        for(int index = 0; index < jsonArray.length(); index++){
                            JSONObject subObj = jsonArray.optJSONObject(index);
                            UserModel obj = new UserModel();
                            obj.setAttachmentName(subObj.optString("thumbnail"));
                            obj.setEmailName(subObj.optString("candidate_name"));
                            messageDemo.add(obj);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
