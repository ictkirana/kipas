package kiranamegatara.com.kipas.Controller;

import android.widget.EditText;

/**
 * Created by vemfiska on 12/10/16.
 */
public class Helper {
    public static boolean isEmpty(EditText text){
        if(text.getText().toString().trim().length() > 0){
            return false;
        }else {
            return true;
        }
    }

    public static boolean isCompare(EditText text1,EditText text2){
        String a = text1.getText().toString();
        String b = text2.getText().toString();
        if (a.equals(b)){
            return false;
        }
        else {
            return true;
        }
    }
}
