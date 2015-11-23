package potboiler.client.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import potboiler.client.model.User;


public class PreferenceUtils {

    public static void saveUser(Context context, User user){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", user.id);
        editor.putInt("server_id", user.serverId);
        editor.putString("username", user.username);
        editor.putString("avatar", user.avatar);
        editor.putString("secretkey", user.secretkey);
        editor.commit();
    }

    public static void removeUser(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("id");
        editor.remove("server_id");
        editor.remove("secretkey");
        editor.remove("avatar");
        editor.remove("password");
        editor.remove("phone");
        editor.commit();
    }

    public static User getUser(Context context){
        User result = new User();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        result.id=preferences.getInt("id", -1);
        result.serverId=preferences.getInt("server_id", -1);
        result.username=preferences.getString("username", "");
        result.avatar=preferences.getString("avatar", "");
        result.secretkey=preferences.getString("secretkey", "");
        return result;
    }

}
