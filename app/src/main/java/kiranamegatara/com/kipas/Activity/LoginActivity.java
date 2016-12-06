package kiranamegatara.com.kipas.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import kiranamegatara.com.kipas.Controller.Helper;
import kiranamegatara.com.kipas.Controller.RealmHelper;
import kiranamegatara.com.kipas.Model.LoginUser;
import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.SessionManager;

public class LoginActivity extends AppCompatActivity {
    MaterialEditText username,password;
    Button btnLogin;
    TextView textForgot;

    AQuery a;
    SessionManager session;

    final ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();

    String id,full_name,pwd,mail,nik,company_code,plant,plant_code,
            plant_name,company_name,authorized_warehouse,is_active,is_kirana,
            is_reset,fail_counter,date_last_login,date_created,date_updated,
            is_deleted,dateCrt,dateUpd;
    List<String> listWh;
    List<String> listPlant;
    String email,passwd;
    //private String userId;


    RealmHelper realmHelper;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (MaterialEditText)findViewById(R.id.inputUser);
        password = (MaterialEditText)findViewById(R.id.inputPass);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        textForgot = (TextView) findViewById(R.id.txtForgot);

        email = username.getText().toString();
        passwd = password.getText().toString();
//        realmHelper = new RealmHelper(LoginActivity.this);
        realm = Realm.getDefaultInstance();

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));
            }
        });

        session = new SessionManager(getApplicationContext());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
    }

    private void LoginUser() {
        username.setError(null);
        password.setError(null);

        if (Helper.isEmpty(username)){
            username.setError("Mohon isi username");
            username.requestFocus();
        }else if (Helper.isEmpty(password)){
            password.setError("Mohon isi password");
            password.requestFocus();
        }else{

            a = new AQuery(LoginActivity.this);
            //String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_login";
            String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/get_login";

            HashMap<String,String> params = new HashMap<String, String>();
            params.put("email",username.getText().toString());
            params.put("password",password.getText().toString());

            Log.d("email",""+email);
            Log.d("password",""+passwd);

            ProgressDialog progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("masuk aplikasi...");
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
                            Toast.makeText(getApplicationContext(),hasil,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),Main2Activity.class);

                            if (hasil.equalsIgnoreCase("true")){
                                JSONArray jsonarray = jsonObject.getJSONArray("data");
                                int length = jsonarray.length();
                                Log.d("jumlah data", "" + length);
                                Log.d("pesan",pesan);
                                for (int i = 0; i < jsonarray.length(); i++){
                                    if (i == 0){
                                        JSONObject b = jsonarray.getJSONObject(i);
                                        id = b.getString("id");
                                        full_name = b.getString("full_name");
                                        pwd = b.getString("password");
                                        mail = b.getString("email");
                                        nik = b.getString("nik");
                                        is_kirana = b.getString("is_kirana");
                                        company_code = b.getString("company_code");
                                        company_name = b.getString("company_name");
                                        plant_code = b.getString("plant_code");
                                        plant_name = b.getString("plant_name");
                                        authorized_warehouse = b.getString("authorized_warehouse");
                                        is_active = b.getString("is_active");
                                        is_reset = b.getString("is_active");
                                        is_deleted = b.getString("is_deleted");
                                        fail_counter = b.getString("fail_counter");
                                        date_created = b.getString("date_created");

                                        /*
                                        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                                        try {
                                            dateCrt = String.valueOf(curFormater.parse(date_created));
                                        } catch (android.net.ParseException e) {
                                            e.printStackTrace();
                                        } catch (java.text.ParseException e) {
                                            e.printStackTrace();
                                        }

                                        date_updated = b.getString("date_updated");
                                        try {
                                            dateUpd = String.valueOf(curFormater.parse(date_updated));
                                        } catch (android.net.ParseException e) {
                                            e.printStackTrace();
                                        } catch (java.text.ParseException e) {
                                            e.printStackTrace();
                                        }*/

                                        Calendar c = Calendar.getInstance();
                                        date_last_login = String.valueOf(c.getTime());
                                    }
                                }
                                JSONArray jsonWh = jsonObject.getJSONArray("data_wh");
                                int lengthWh = jsonWh.length();
                                Log.d("jumlah data wh", "" + lengthWh);
                                plant_code="";
                                authorized_warehouse="";
                                for (int i = 0; i < jsonWh.length(); i++){
                                    JSONObject wh = jsonWh.getJSONObject(i);
                                    if (plant_code!=""){
                                        plant_code=plant_code+";"+wh.getString("plant_code");
                                    }else {
                                        plant_code=wh.getString("plant_code");
                                    }
                                    if (authorized_warehouse!=""){
                                        authorized_warehouse=authorized_warehouse+";"+wh.getString("code");
                                    }else {
                                        authorized_warehouse=wh.getString("code");
                                    }
                                }
                                Log.d("arr plant",plant_code+" - "+authorized_warehouse);
                                try {
                                    session.createSession(mail, plant_code, company_code, authorized_warehouse,nik,full_name);
                                }catch (NullPointerException n){

                                }
                                    realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        LoginUser loginUser = realm.createObject(LoginUser.class);
                                        loginUser.setNik(nik);
                                        loginUser.setPlant(plant_code);
                                        loginUser.setFullname(full_name);
                                        loginUser.setGudang(authorized_warehouse);
                                    }
                                });

                                intent.putExtra("email",username.getText().toString());
                                intent.putExtra("fullname",full_name);
                                intent.putExtra("password",password.getText().toString());
                                intent.putExtra("plant",plant_code);
                                intent.putExtra("nik",nik);
                                intent.putExtra("gudang",authorized_warehouse);
                                //realmHelper.addUser(nik,full_name,plant_code,authorized_warehouse);
                                startActivity(intent);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
            });

        }
    }
}
