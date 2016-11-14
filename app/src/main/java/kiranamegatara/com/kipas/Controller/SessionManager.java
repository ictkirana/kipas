package kiranamegatara.com.kipas.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import kiranamegatara.com.kipas.Activity.LoginActivity;
import kiranamegatara.com.kipas.Activity.MainActivity;

/**
 * Created by vemfiska on 12/10/16.
 */
public class SessionManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Context c;

    int mode = 0;

    public static final String pref_name = "kipaspref";

    public static final String isLogin = "isLogin";
    public static final String keyEmail = "keyEmail";
    public static final String keyPlant = "keyPlant";
    public static final String keyCompany = "keyCompany";
    public static final String keyGudang = "keyGudang";

    public SessionManager(Context context){
        this.c = context;
        preferences = context.getSharedPreferences(pref_name,mode);
        editor = preferences.edit();
    }


    public void createSession(String username,String plant,String company,String gudang){
        editor.putBoolean(isLogin,true);
        editor.putString(keyEmail,username);
        editor.putString(keyPlant,plant);
        editor.putString(keyCompany,company);
        editor.putString(keyGudang,gudang);
        editor.commit();
    }

    public void checkLogin(){
        if (!this.is_login()){
            Intent i = new Intent(c,LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(i);
        }else{
            Intent i = new Intent(c,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(i);
        }
    }

    private boolean is_login() {
        return preferences.getBoolean(isLogin,false);
    }

    public void logout(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(c,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(keyEmail, preferences.getString(keyEmail, null));

        // user plant
        user.put(keyPlant, preferences.getString(keyPlant, null));

        // user company
        user.put(keyCompany, preferences.getString(keyCompany, null));

        // user gudang
        user.put(keyGudang, preferences.getString(keyGudang, null));

        // return user
        return user;
    }
}
