package cn.edu.scujcc.diandian;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 用来处理Shared Preference中保存的数据，简化代码。
 */
public class MyPreference {
    private static MyPreference INSTANCE = null;
    private Context context = null;
    private SharedPreferences prefs = null;

    private MyPreference() {
    }

    public static MyPreference getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new MyPreference();
        }
        return INSTANCE;
    }

    public void setup(Context ctx) {
        context = ctx;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveUser(String username, String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(UserLab.USER_CURRENT, username);
        editor.putString(UserLab.USER_TOKEN, token);
        editor.apply();
    }

    public String currentUser() {
        return prefs.getString(UserLab.USER_CURRENT, "未登录");
    }

    public String currentToken() {
        return prefs.getString(UserLab.USER_TOKEN, "");
    }
}
