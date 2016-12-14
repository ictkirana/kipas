package kiranamegatara.com.kipas.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;
import kiranamegatara.com.kipas.Controller.SessionManager;
import kiranamegatara.com.kipas.Model.SrtJalan;
import kiranamegatara.com.kipas.Model.SuratJalan;
import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.RealmHelper;

public class ScanResultActivity extends AppCompatActivity {
    TextView number,plant,tglKirim,tglTerima,nopol,jam;
    Button simpan,kembali,btnChangeTime;
    private Calendar calendar;
    private int year, month, day;
    private int hour,minute;
    String email;
    AQuery a;
    SessionManager session;
    int year1, month1, date, hour1, minute1, second;

    RealmHelper realmHelper;

    String nosurat,tanggalKirim,pabrik,polisi_no,fullname,
            nik,gudang,date_scaned,getTanggalKirim,is_scaned;
    String setTglTerima,setJamTerima;
    Realm realm,getRealm;

    static final int TIME_DIALOG_ID = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        number = (TextView)findViewById(R.id.noSuratJalan);
        plant = (TextView)findViewById(R.id.Plant);
        tglKirim = (TextView)findViewById(R.id.tanggalKirim);
        tglTerima = (TextView)findViewById(R.id.tanggalTerima);
        nopol = (TextView)findViewById(R.id.noPol);
        jam = (TextView)findViewById(R.id.jamTerima);

        simpan = (Button)findViewById(R.id.btnTerima);
        kembali = (Button)findViewById(R.id.btnRescan);
        btnChangeTime = (Button) findViewById(R.id.btnPickTime);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        Intent intent = getIntent();
        nosurat = intent.getStringExtra("surat_jalan_no");
        getTanggalKirim = intent.getStringExtra("tglKirim");
        tanggalKirim = getTanggalKirim.substring(0,10);
        Log.d("tanggal kirim", tanggalKirim);
        pabrik = intent.getStringExtra("plant");
        polisi_no = intent.getStringExtra("polisi_no");
        is_scaned = intent.getStringExtra("is_scaned");
        gudang = intent.getStringExtra("gudang");

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.keyEmail);
        fullname = user.get(SessionManager.keyFullName);
        nik = user.get(SessionManager.keyNik);

        number.setText(nosurat);
        plant.setText(pabrik);
        tglKirim.setText(tanggalKirim.substring(8,10) + "-"
                        + tanggalKirim.substring(5,7) + "-"
                        + tanggalKirim.substring(0,4));
        nopol.setText(polisi_no);

        tglTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //realmHelper = new RealmHelper(ScanResultActivity.this);
                //realmHelper.addBarcode(nosurat,pabrik,vndr);
                SaveSuratJalan();

                realm = Realm.getDefaultInstance();
                getRealm = Realm.getDefaultInstance();

                RealmResults<SrtJalan> srtJalen = realm.where(SrtJalan.class).findAll();
                realm.beginTransaction();
                srtJalen.clear();
                realm.commitTransaction();
                RealmResults<SuratJalan> history = getRealm.where(SuratJalan.class).findAll();
                getRealm.beginTransaction();
                history.clear();
                getRealm.commitTransaction();
                Intent intent1 = new Intent(ScanResultActivity.this,Main2Activity.class);
                startActivity(intent1);
                finish();
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ScanResultActivity.this,ScanActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        btnChangeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(TIME_DIALOG_ID);
            }
        });
    }

    private void SaveSuratJalan() {
        a = new AQuery(ScanResultActivity.this);
        //String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/scan_sj";
        String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/scan_sj";

        java.util.TimeZone tz = calendar.getTimeZone();
//        Log.d("Time zone","="+tz.getDisplayName());
//        date_scaned = String.valueOf(Calendar.DAY_OF_MONTH);
//        android.text.format.DateFormat df = new android.text.format.DateFormat();
//        df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());
//        Date date = new Date();
//        date_scaned = String.valueOf(date.getTime());


        final String SPACE = " ";
        Calendar c = Calendar.getInstance();
        year1 = c.get(Calendar.YEAR);
        month1 = c.get(Calendar.MONTH) + 1;
        date = c.get(Calendar.DATE);
        hour1 = c.get(Calendar.HOUR_OF_DAY);
        minute1 = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);

        date_scaned = year1 + "-" + month1 + "-" + date + SPACE + hour1 + ":" + minute1 + ":" + second;

        setTglTerima = date_scaned + setJamTerima;
        Log.d("date_received",""+ tglTerima.getText().toString());
        Log.d("srt_jln_no",""+ nosurat);
        Log.d("date_scaned",""+ date_scaned);
        Log.d("date_scaned jam",""+ setJamTerima);
        Log.d("user_full_name",""+ fullname);
        Log.d("plant_code",""+ pabrik);
        Log.d("nik",""+ nik);
        Log.d("gudang",""+ gudang);

        String tglterima = tglTerima.getText().toString();
        String terima = tglterima.substring(6,10)+"-" + tglterima.substring(3,5) + "-"+tglterima.substring(0,2)
                +SPACE+ jam.getText().toString();
        Log.d("terima", ""+ terima);


        HashMap<String,String> params = new HashMap<String, String>();
        params.put("srt_jln_no",nosurat);
        params.put("date_scaned",setTglTerima);
        params.put("user_full_name",fullname);
        params.put("plant_code",pabrik);
        params.put("date_received",terima);
        params.put("nik",nik);
        params.put("warehouse_code",gudang);

        ProgressDialog progress = new ProgressDialog(getApplicationContext());
        progress.setMessage("simpan data...");
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
                        Log.d("surat_jalan","hasil " + hasil);
                        if (hasil.equalsIgnoreCase("true")){
                            Toast.makeText(getApplicationContext(),"Tersimpan",Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void showDate(int year, int i, int day) {

        //tglTerima.setText(new StringBuilder().append(year).append("-")
        //        .append(month+1).append("-").append("0").append(day));
        String hari = String.valueOf(day);
        if (hari.length() == 1){
            tglTerima.setText(new StringBuilder().append("0").append(day).append("-").append(month + 1)
                    .append("-").append(year));
        }else {
            tglTerima.setText(new StringBuilder().append(day).append("-").append(month + 1)
                    .append("-").append(year));
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        //akan menampilkan teks ketika kalendar muncul setelah menekan tombol
        Toast.makeText(getApplicationContext(), "Pilih Tangal", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        String tahun,bulan,hari;
        int thn,bln,hri;
        if (id == 999) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            Log.d("date scaned pilih",""+ date_scaned);
            Log.d("is scaned pilih",""+ is_scaned);
            if (is_scaned.equalsIgnoreCase("0") ) {
                tahun = tanggalKirim.substring(0,4);
                bulan = tanggalKirim.substring(5,7);
                hari = tanggalKirim.substring(8,10);
                thn = Integer.parseInt(tahun);
                bln = Integer.parseInt(bulan);
                hri = Integer.parseInt(hari);
                Log.d("tanggal",""+ thn +"-"+bln+"-"+hri);
                calendar.set(thn,bln-1,hri);
                //calendar.isWeekDateSupported();
            } else {
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, -3);
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
            }
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            showDate(i,i1 + 1,i2);
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(intent);
        super.onBackPressed();
    }


    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);

        }
        return null;
    }



    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            // set current time into textview
            jam.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));

            setJamTerima = jam.getText().toString();

        }
    };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
