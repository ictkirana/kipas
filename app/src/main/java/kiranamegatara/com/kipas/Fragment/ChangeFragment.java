package kiranamegatara.com.kipas.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kiranamegatara.com.kipas.Activity.Main2Activity;
import kiranamegatara.com.kipas.Controller.Helper;
import kiranamegatara.com.kipas.Controller.SessionManager;
import kiranamegatara.com.kipas.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeFragment extends Fragment {
    SessionManager session;
    TextView txtemail;
    Button btnubah;
    AQuery a;
    public ChangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change,null);
        final EditText newpwd1 = (EditText)view.findViewById(R.id.inp_newpwd1);
        final EditText newpwd2 = (EditText)view.findViewById(R.id.inp_newpwd2);
        final EditText oldpwd = (EditText)view.findViewById(R.id.inp_oldpwd);
        txtemail= (TextView) view.findViewById(R.id.txtEmailUser);
        btnubah= (Button) view.findViewById(R.id.btn_Ubah);
        session = new SessionManager(getContext().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(SessionManager.keyEmail);

        txtemail.setText(email);
        btnubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=oldpwd.getText().toString();
                if (Helper.isCompare(newpwd1,newpwd2)){

                    newpwd1.setText("");
                    newpwd2.setText("");
                    newpwd1.requestFocus();
                    Toast.makeText(getContext(),"Password yang anda masukkan tidak sama.",Toast.LENGTH_LONG).show();
                }else {
                    Change_Password(email,pass,newpwd1,newpwd2);
                }


            }
        });
        return view;
    }

    private void Change_Password(String mail,String pass,EditText newpwd1, EditText newpwd2) {
        String pwd1 = newpwd1.getText().toString();
        String pwd2 = newpwd2.getText().toString();
        Log.d("pwd1",""+pwd1);
        Log.d("pwd2",""+pwd2);
        //if (pwd1 == pwd2){
        a = new AQuery(this.getActivity());
        //String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/change_password";
        String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/change_password";
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("email",mail);
        params.put("password_lama",pass);
        params.put("password_baru",pwd1);
        Log.d("Param chg",params.toString());
        ProgressDialog progress = new ProgressDialog(this.getActivity());
        progress.setCancelable(false);
        progress.setIndeterminate(false);
        a.progress(progress).ajax(url,params,String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null){
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        String hasil = jsonObject.getString("result");
                        String pesan = jsonObject.getString("msg");

                        Log.d("pesan",pesan);
                        if (hasil.equalsIgnoreCase("true")){
                            Toast.makeText(getContext(),"Password anda telah berhasil diubah.",Toast.LENGTH_LONG).show();
                            Log.d("pesan",pesan);
                        }else {
                            Toast.makeText(getContext(),"Gagal ubah password, mohon diperiksa kembali password lama anda.",Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
