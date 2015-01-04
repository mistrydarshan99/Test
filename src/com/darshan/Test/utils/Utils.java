package com.darshan.Test.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.darshan.Test.constants.Constants;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    public static void printLog(final int which, final String message) {
        if(which == 5) {
            Log.e(Constants.TAG, message);
        }

        if(Constants.DEBUG) {
            if(which == 1) {
                Log.v(Constants.TAG, message);
            } else if(which == 2) {
                Log.d(Constants.TAG, message);
            } else if(which == 3) {
                Log.i(Constants.TAG, message);
            } else if(which == 4) {
                Log.w(Constants.TAG, message);
            }
        }
    }

    public static String getTwoDigit(int number) {
        String result = "";
        if(number >= 0 && number < 10) {
            result = "0" + number;
        } else {
            result = String.valueOf(number);
        }

        return result;
    }

    public static String getToday(String format) {
        final Calendar calendar = new GregorianCalendar();
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(calendar.getTime());
    }

    public static String getDateInFormat(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String convertToDateFormat(String date, String fromFormat,
        String toFormat){
        SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat);
        String convertedDate;
        try {
            Date parseDate = dateFormat.parse(date);
            SimpleDateFormat fmtOut = new SimpleDateFormat(toFormat);
            convertedDate = fmtOut.format(parseDate);
        } catch (ParseException e) {
            convertedDate = null;
        }

        return convertedDate;
    }

    public static double round2(double d) {
        return Math.round(d * 100) / 100.0;
    }

    public static boolean haveNetworkConnection(Context act) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) act
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static String getDeviceIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)
            context.getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager != null) {
            return telephonyManager.getDeviceId();
        }

        return "000000000000000";
    }

    public static boolean copyFile(File source, File destination) {
        boolean isCopied = false;
        try {
            FileChannel inChannel = new FileInputStream(source).getChannel();
            FileChannel outChannel = new FileOutputStream(destination).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
            isCopied = true;
        } catch (IOException e) {
            isCopied = false;
        }

        return isCopied;
    }

    public static byte[] convertDatabaseFile(String mPath) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            File pathFile = new File(mPath);
            inputStream = new BufferedInputStream(new FileInputStream(pathFile));
            byteArrayOutputStream = new ByteArrayOutputStream();
            while (inputStream.available() > 0) {
                byteArrayOutputStream.write(inputStream.read());
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return byteArrayOutputStream == null ? null :
            byteArrayOutputStream.toByteArray();
    }

    public static boolean isSendingDatabaseCorrupted(Context context,
        final String fileName) {
        boolean isCorrupted = false;
        String mPath = "/data/data/" + context.getPackageName()
                + "/databases/" + fileName;
        File pathFile = new File(mPath);
        if (pathFile.exists()) {
            SQLiteDatabase sqliteDatabase = SQLiteDatabase.openDatabase(mPath, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            Cursor cursor =
                sqliteDatabase
                    .rawQuery("SELECT MAX(intGlCode) AS intGlCode FROM Login", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                final int count = cursor.getInt(cursor.getColumnIndex("intGlCode"));
                if (count <= 0) {
                    isCorrupted = true;
                } else {
                    isCorrupted = false;
                }
            }
            cursor.close();

            if (sqliteDatabase != null) {
                sqliteDatabase.close();
            }
        }
        return isCorrupted;
    }

    public static String getUUId() {
        UUID uniqueId = UUID.randomUUID();
        return String.valueOf(uniqueId);
    }

    static String PNG = "png";
    static String JPG = "jpg";
    static String JPEG = "jpeg";
    static String BMP = "bmp";
    static String GIF = "gif";
    static String THREEGP = "3gp";
    static String MP4 = "mp4";
    static String PDF = "pdf";

    public static boolean isSDCardMounted() {
        boolean isMounted = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            isMounted = true;
        } else if (Environment.MEDIA_BAD_REMOVAL.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_CHECKING.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_NOFS.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_REMOVED.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_UNMOUNTABLE.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_UNMOUNTED.equals(state)) {
            isMounted = false;
        }

        return isMounted;
    }

    public static boolean isDirectoryExists(final String filePath) {
        boolean isDirectoryExists = false;
        File mFilePath = new File(filePath);
        if (mFilePath.exists()) {
            isDirectoryExists = true;
        } else {
            isDirectoryExists = mFilePath.mkdirs();
        }

        return isDirectoryExists;
    }

    public static boolean directoryCreated(final String directoryName) {
        boolean isDirectoryCreated = false;
        if (isSDCardMounted()) {
            final File createdDirectory = new File(directoryName);
            if (!createdDirectory.exists()) {
                isDirectoryCreated = createdDirectory.mkdirs();
            } else {
                isDirectoryCreated = true;
            }
        } else {
            isDirectoryCreated = false;
        }

        return isDirectoryCreated;
    }

    public static String getDataPath(final String mDirName) {
        String returnedPath = null;
        if (isSDCardMounted()) {
            final String mSDCardDirPath = Environment.getExternalStorageDirectory() + "/"
                + mDirName;
            if (isDirectoryExists(mSDCardDirPath)) { return mSDCardDirPath; }
        }

        return returnedPath;
    }

    public static boolean deleteFile(final String filePath) {
        boolean isFileExists = false;
        File mFilePath = new File(filePath);
        if (mFilePath.exists()) {
            mFilePath.delete();
            isFileExists = true;
        }

        return isFileExists;
    }

    public static String removeSpaces(String passedString) {
        passedString = passedString.trim();
        int index;
        String returnedString = "";
        while ((index = passedString.indexOf(" ")) != -1) {
            returnedString += passedString.substring(0, index);
            returnedString += "";
            passedString = passedString.substring(index + 1, passedString.length());
        }
        if (returnedString == "") {
            returnedString = passedString;
        } else {
            returnedString += passedString;
        }

        return returnedString;
    }

    public static boolean writeToFile(final Context context, final String fileName,
        final String fileContent, final String directoryName) {
        boolean isFileCreated = false;
        if (directoryCreated(directoryName)) {
            final String filePath = directoryName + File.separator + fileName;
            try {
                final FileWriter fileWriter = new FileWriter(new File(filePath), false);
                final BufferedWriter writer = new BufferedWriter(fileWriter);
                writer.write(fileContent);
                writer.newLine();
                writer.flush();
                writer.close();
                isFileCreated = true;
            } catch (IOException e) {
                isFileCreated = false;
            }
        }

        return isFileCreated;
    }

    public static ArrayList<String> returnAllFileNames(final String directoryName) {
        ArrayList<String> fileNameList = new ArrayList<String>();
        final File dir = new File(directoryName);
        for (final File imgFile : dir.listFiles()) {
            if (accept(imgFile)) {
                fileNameList.add(imgFile.getName());
            }
        }

        return fileNameList;
    }

    public static String returnFileName(final String filePath) {
        String fileName = "";
        final File imgFile = new File(filePath);
        if(accept(imgFile)) {
            fileName = imgFile.getName();
        }

        return fileName;
    }

    private static boolean accept(File file) {
        if (file != null) {
            if (file.isDirectory()) { return false; }
            String extension = getExtension(file);
            if (extension != null && isAndroidSupported(extension)) { return true; }
        }

        return false;
    }

    public static String getExtension(File file) {
        if (file != null) {
            String filename = file.getName();
            int dot = filename.lastIndexOf('.');
            if (dot > 0 && dot < filename.length() - 1)
                return filename.substring(dot + 1).toLowerCase();
        }

        return null;
    }

    private static boolean isAndroidSupported(final String extension) {
        return extension.equalsIgnoreCase(PNG)
            || extension.equalsIgnoreCase(JPG)
            || extension.equalsIgnoreCase(BMP)
            || extension.equalsIgnoreCase(JPEG)
            || extension.equalsIgnoreCase(GIF)
            || extension.equalsIgnoreCase(PDF)
            || extension.equalsIgnoreCase(THREEGP)
            || extension.equalsIgnoreCase(MP4);
    }

    public static String getMimeType(String filePath) {
        File file = new File(filePath);
        String extension = getExtension(file);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }



    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


}