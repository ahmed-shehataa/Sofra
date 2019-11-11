package com.ashehata.sofra.data.local.shared;

import android.app.Activity;
import android.content.SharedPreferences;

import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.google.gson.Gson;

public class SharedPreferencesManger {

    private static SharedPreferences sharedPreferences = null;
    private static String RESTAURANT_USER_DATA = "USER_DATA";
    public static String USER_API_TOKEN = "USER_API_TOKEN";
  /*
    private static String USER_ID = "USER_ID";
    private static String USER_EMAIL = "USER_EMAIL";
    private static String USER_NAME = "USER_NAME";
    private static String USER_BID = "USER_BID";
    private static String USER_PHONE = "USER_PHONE";
    private static String USER_DLD = "USER_DLD";
    private static String USER_PIN_CODE = "USER_PIN_CODE";
    private static String USER_CITY_ID = "USER_CITY_ID";
    private static String USER_CITY_NAME = "USER_CITY_NAME";
    private static String USER_GOVERMENT_NAME = "USER_GOVERMENT_NAME";
    private static String USER_GOVERMENT_ID = "USER_GOVERMENT_ID";
    private static String USER_BLOOD_TYPE_ID = "USER_BLOOD_TYPE_ID";
    private static String USER_BLOOD_TYPE_NAME = "USER_BLOOD_TYPE_NAME";

   */
  //nYntJTubK4ai27SkecCVpIICxAl2DfTgiGanCNXXBA1QdhQx3bFiuJpyDe7j
  //mine : Zc3CHfa2Slem4zXMzsP33HRN4k4bq0KAQROROhDUhLFiSbtWRVBcNLZcJJpo
    public static String USER_PASSWORD = "USER_PASSWORD";
    public static String REMEMBER_RESTAURANT    = "REMEMBER_rest";
    public static String REMEMBER_CLIENT    = "REMEMBER_client";

    /*
    public static String getReadyApiToken(){
        return "nYntJTubK4ai27SkecCVpIICxAl2DfTgiGanCNXXBA1QdhQx3bFiuJpyDe7j";
    }

     */

    public static void setSharedPreferences(Activity activity) {
        if (sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences(
                    "Blood", activity.MODE_PRIVATE);
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

}
