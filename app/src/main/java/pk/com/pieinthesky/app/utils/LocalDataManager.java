package pk.com.pieinthesky.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalDataManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    private static final String PREF_NAME = "PitsLocalData";

    private static LocalDataManager instance = null;

    private LocalDataManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public static LocalDataManager createInstance(Context context) {
        if (instance == null) {
            instance = new LocalDataManager(context);
        }
        return instance;
    }

    public static LocalDataManager getInstance() {
        return instance;
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key) {
        return pref.getFloat(key, 0);
    }

    public void putDouble(String key, double value) {
        editor.putFloat(key, Float.valueOf(String.valueOf(value)));
        editor.commit();
    }

    public double getDouble(String key) {
        return pref.getFloat(key, 0);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public Long getLong(String key) {
        return pref.getLong(key, 0);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
