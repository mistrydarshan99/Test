package com.darshan.Test.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.darshan.Test.R;
import com.darshan.Test.TestApp;
import com.darshan.Test.utils.Utils;

import java.io.*;

public class Sign_UpActivity extends Activity {

    private EditText edtUser, edtPass, edtMail;
    private ImageView userImage;

    private TestApp testObjApp;
    private String filePath1 = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        testObjApp = (TestApp) this.getApplicationContext();
        initComponent();
    }

    private void initComponent() {
        edtUser = (EditText) findViewById(R.id.etUsername);
        edtPass = (EditText) findViewById(R.id.etPassword);
        edtMail = (EditText) findViewById(R.id.etEmail);
        userImage = (ImageView) findViewById(R.id.ivUserImage);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Sign_UpActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    userImage.setImageBitmap(bitmap);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
//                    File finalFile = new File(getPath(tempUri));
                    filePath1 = getPath(tempUri);

                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(filePath1, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                filePath1 = picturePath;
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");
                userImage.setImageBitmap(thumbnail);

            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void timerTask() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    public void doAccount(View view) {
        if (edtUser.getText().toString().trim().length() > 0 && edtPass.getText().toString().trim().length() > 0 && edtMail.getText().toString().trim().length() > 0) {
            if (!Utils.isValidEmail(edtMail.getText().toString().trim())) {
                int userExistCount = testObjApp.getTAPLDatabase().countUser(edtUser.getText().toString().trim());
                testObjApp.closeTAPLDataBase();
                if (userExistCount == 0) {
                    testObjApp.getTAPLDatabase().insertLogin(edtUser.getText().toString().trim(), edtPass.getText().toString().trim(), edtMail.getText().toString().trim(), filePath1);
                    testObjApp.closeTAPLDataBase();
                    edtMail.setText("");
                    edtUser.setText("");
                    edtPass.setText("");
                    Toast.makeText(Sign_UpActivity.this, "Account Create Successfully", Toast.LENGTH_SHORT).show();
                    timerTask();
                } else {
                    Toast.makeText(Sign_UpActivity.this, "UserName is already exist", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(Sign_UpActivity.this, "Enter Valid email", Toast.LENGTH_SHORT).show();
                edtMail.setText("");
            }
        } else {
            if (edtUser.getText().toString().trim().length() == 0) {
                Toast.makeText(Sign_UpActivity.this, "Enter Valid UserName", Toast.LENGTH_SHORT).show();
            } else if (edtPass.getText().toString().trim().length() == 0) {
                Toast.makeText(Sign_UpActivity.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Sign_UpActivity.this, "Enter Valid email please", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
