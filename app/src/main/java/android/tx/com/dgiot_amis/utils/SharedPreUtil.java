package android.tx.com.dgiot_amis.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Morphine on 2017/10/12.
 */

public class SharedPreUtil {

    public static void saveData(Context context, String spName, String data) {
        SharedPreferences.Editor edit = context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit();
        edit.putString(spName, data).apply();
    }
    public static void saveData(Context context, String spName, boolean data) {
        SharedPreferences.Editor edit = context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit();
        edit.putBoolean(spName, data).apply();
    }
    public static void saveData(Context context , String spName ,int data){
        SharedPreferences.Editor edit = context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit();
        edit.putInt(spName, data).apply();
    }


    public static void addData(Context context, String spName, String data) {
        String data1 = getData(context, spName);
        saveData(context, spName, data1 + "," + data);
    }

    public static void addUniqeData(Context context, String spName, String data) {
        deleteData(context, spName, data);
        addData(context, spName, data);
    }

    public static void deleteData(Context context, String spName, String data) {
        String data1 = getData(context, spName);
        if (TextUtils.isEmpty(data1)) {
            return;
        }
        List<String> list = Arrays.asList(data1.split(","));
        if (!list.contains(data)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(data)) {
                continue;
            }
            sb.append(list.get(i));
            sb.append(",");
        }
        saveData(context, spName, sb.substring(0, sb.length() - 1));
    }

    public static String getData(Context context, String spName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(spName, "");
    }
    public static boolean getBooleanData(Context context, String spName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(spName, false);
    }
    public static int getIntData( Context context , String spName ){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(spName, 0);
    }

    public static void clearData(Context context, String spName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

}
