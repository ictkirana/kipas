package kiranamegatara.com.kipas.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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

    public SessionManager(Context context){
        this.c = context;
        preferences = context.getSharedPreferences(pref_name,mode);
        editor = preferences.edit();
    }

    public void createSession(String username){
        editor.putBoolean(isLogin,true);
        editor.putString(keyEmail,username);
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
}
