package org.maktab.photogallery.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class QueryPreferences {

    private static final String PREF_KEY_SEARCH = "searchQuery";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";

    public static String getSearchQuery(Context context) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        return preferences.getString(PREF_KEY_SEARCH, null);
    }

    public static void setSearchQuery(Context context, String query) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        preferences
                .edit()
                .putString(PREF_KEY_SEARCH, query)
                .apply();
    }

    public static String getLastResultId(Context context) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        return preferences.getString(PREF_LAST_RESULT_ID, null);
    }

    public static void setLastResultId(Context context, String lastResultId) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        preferences
                .edit()
                .putString(PREF_LAST_RESULT_ID, lastResultId)
                .apply();;
    }

    private static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }
}
