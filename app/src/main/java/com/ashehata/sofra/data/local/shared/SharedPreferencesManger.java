package com.ashehata.sofra.data.local.shared;

import android.app.Activity;
import android.content.SharedPreferences;

import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.google.gson.Gson;

public class SharedPreferencesManger {

    private static SharedPreferences sharedPreferences = null;
    private static String RESTAURANT_USER_DATA = "USER_DATA";
    public static String USER_API_TOKEN = "USER_API_TOKEN";

  //nYntJTubK4ai27SkecCVpIICxAl2DfTgiGanCNXXBA1QdhQx3bFiuJpyDe7j
  //mine : Zc3CHfa2Slem4zXMzsP33HRN4k4bq0KAQROROhDUhLFiSbtWRVBcNLZcJJpo
    public static String USER_PASSWORD = "USER_PASSWORD";
    public static String REMEMBER_RESTAURANT  = "REMEMBER_rest";
    public static String REMEMBER_CLIENT  = "REMEMBER_client";
    private static String USER_TYPE  = "TYPE";
    public static String TYPE_CLIENT = "CLIENT";
    public static String TYPE_RESTAURANT = "RESTAURANT";

    /*
    public static String getReadyApiToken(){
        return "nYntJTubK4ai27SkecCVpIICxAl2DfTgiGanCNXXBA1QdhQx3bFiuJpyDe7j";
    }

     */

    public static void setSharedPreferences(Activity activity) {
        if (sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences(
                    "Sofra", activity.MODE_PRIVATE);
        }
    }

    public static void SaveData(Activity activity, String data_Key, String data_Value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(data_Key, data_Value);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }
    }

    public static void SaveData(Activity activity, String data_Key, boolean data_Value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(data_Key, data_Value);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }
    }

    public static String LoadData(Activity activity, String data_Key) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
        } else {
            setSharedPreferences(activity);
        }

        return sharedPreferences.getString(data_Key, null);
    }


    public static boolean LoadBoolean(Activity activity, String data_Key) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
        } else {
            setSharedPreferences(activity);
        }

        return sharedPreferences.getBoolean(data_Key, false);
    }

    public static void SaveData(Activity activity, String data_Key, Object data_Value) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String StringData = gson.toJson(data_Value);
            editor.putString(data_Key, StringData);
            editor.commit();
        }
    }

    public static void saveUserData(Activity activity, User userData) {
        SaveData(activity, RESTAURANT_USER_DATA, userData);
    }

    public static User loadUserData(Activity activity) {
        User userData = null;

        Gson gson = new Gson();
        userData = gson.fromJson(LoadData(activity, RESTAURANT_USER_DATA), User.class);

        return userData;
    }

    public static void clean(Activity activity) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }
    public static void SaveUserType(Activity activity,String user_type){
        SaveData(activity,USER_TYPE,user_type);
    }
    public static String LoadUserType(Activity activity){
        return LoadData(activity,USER_TYPE);
    }

}
