package pk.com.pieinthesky.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    private static final String PREF_NAME = "PitsPref";
    private static final String KEY_ISLOGIN = "IsLogin";
    public static final String KEY_USERNAME = "Username";
    public static final String KEY_PASSWORD = "Password";

    private static SessionManager instance = null;

    private SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public static SessionManager createInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public void login(String username, String password) {
        editor.putBoolean(KEY_ISLOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }

    public HashMap<String, String> getLoginDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_ISLOGIN, false);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getPassword() {
        return pref.getString(KEY_PASSWORD, null);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

}
