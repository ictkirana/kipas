package kiranamegatara.com.kipas.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.icu.text.SimpleDateFormat;
import android.os.Parcelable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import kiranamegatara.com.kipas.Controller.Helper;
import kiranamegatara.com.kipas.Model.User;
import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.SessionManager;

public class LoginActivity extends AppCompatActivity {
    MaterialEditText username,password;
    Button btnLogin;
    TextView textForgot;

    //private DatabaseReference mFirebaseDatabase;
    //private FirebaseDatabase mFirebaseInstance;
    //private static final String TAG = MainActivity.class.getSimpleName();

    AQuery a;
    SessionManager session;

    final ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();

    String id,full_name,pwd,mail,nik,company_code,plant,plant_code,
            plant_name,company_name,authorized_warehouse,is_active,is_kirana,
            is_reset,fail_counter,date_last_login,date_created,date_updated,
            is_deleted,dateCrt,dateUpd;
    String email,passwd;
    //private String userId;

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

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));
            }
        });
        /*
        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
        */

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

            /*
            userId = mFirebaseDatabase.push().getKey();

            mFirebaseDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot != null && dataSnapshot.getValue() != null) {
                        User user = dataSnapshot.getValue(User.class);
                        String usernm = user.getName();
                        String pass = user.getEmail();
                        String usrnm = username.getText().toString();
                        String pwd = password.getText().toString();
                        //Toast.makeText(getApplicationContext(),usernm,Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(),pass,Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(),usrnm,Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(),pwd,Toast.LENGTH_LONG).show();
                        //if (usernm == usrnm && pass == pwd){
                            try {
                                session.createSession(username.getText().toString());
                            }catch (NullPointerException n){

                            }
                            startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                            finish();
                        //}
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */

            a = new AQuery(LoginActivity.this);
            String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_login";

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
                                try {
                                    Log.d("mail",""+mail);
                                    Log.d("plant",""+plant_code);
                                    Log.d("company",""+company_code);
                                    Log.d("gudang",""+authorized_warehouse);
                                    session.createSession(mail,plant_code,company_code,authorized_warehouse);
                                }catch (NullPointerException n){

                                }

                                intent.putExtra("email",username.getText().toString());
                                intent.putExtra("fullname",full_name);
                                intent.putExtra("password",password.getText().toString());
                                intent.putExtra("plant",plant_code);
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
