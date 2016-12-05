package kiranamegatara.com.kipas.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import kiranamegatara.com.kipas.Controller.SessionManager;
import kiranamegatara.com.kipas.Model.SrtJalan;
import kiranamegatara.com.kipas.Model.SuratJalan;
import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.RealmHelper;

public class ScanResultActivity extends AppCompatActivity {
    TextView number,plant,tglKirim,tglTerima,nopol;
    Button simpan,kembali;
    private Calendar calendar;
    private int year, month, day;
    String email;
    AQuery a;
    SessionManager session;

    RealmHelper realmHelper;

    String nosurat,tanggalKirim,pabrik,polisi_no,fullname,
            nik,gudang,date_scaned,getTanggalKirim,is_scaned;
    Realm realm,getRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        number = (TextView)findViewById(R.id.noSuratJalan);
        plant = (TextView)findViewById(R.id.Plant);
        tglKirim = (TextView)findViewById(R.id.tanggalKirim);
        tglTerima = (TextView)findViewById(R.id.tanggalTerima);
        nopol = (TextView)findViewById(R.id.noPol); 

        simpan = (Button)findViewById(R.id.btnTerima);
        kembali = (Button)findViewById(R.id.btnRescan);
        
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        Intent intent = getIntent();
        nosurat = intent.getStringExtra("surat_jalan_no");
        getTanggalKirim = intent.getStringExtra("tglKirim");
        tanggalKirim = getTanggalKirim.substring(0,10);
       // tanggalKirim = "2016-11-22";
        Log.d("tanggal kirim", tanggalKirim);
        pabrik = intent.getStringExtra("plant");
        polisi_no = intent.getStringExtra("polisi_no");
        email = intent.getStringExtra("email_user");
        fullname = intent.getStringExtra("fullname");
        nik = intent.getStringExtra("nik");
        gudang = intent.getStringExtra("gudang");
        is_scaned = intent.getStringExtra("is_scaned");
        date_scaned = intent.getStringExtra("date_scaned");

        number.setText(nosurat);
        plant.setText(pabrik);
        tglKirim.setText(tanggalKirim.substring(8,10) + "-"
                        + tanggalKirim.substring(5,7) + "-"
                        + tanggalKirim.substring(8,10));
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
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ScanResultActivity.this,ScanActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void SaveSuratJalan() {
        a = new AQuery(ScanResultActivity.this);
        //String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/scan_sj";
        String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/scan_sj";

        date_scaned = String.valueOf(Calendar.DAY_OF_MONTH);

        Log.d("date_received",""+ tglTerima.getText().toString());
        Log.d("srt_jln_no",""+ nosurat);
        Log.d("date_scaned",""+ date_scaned);
        Log.d("user_full_name",""+ fullname);
        Log.d("plant_code",""+ pabrik);
        Log.d("nik",""+ nik);
        Log.d("gudang",""+ gudang);

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("srt_jln_no",nosurat);
        params.put("date_scaned",date_scaned);
        params.put("user_full_name",fullname);
        params.put("plant_code",pabrik);
        params.put("date_received",tglTerima.getText().toString());
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
                        //    JSONArray jsonarray = jsonObject.getJSONArray("data");
                        //    int length = jsonarray.length();
                        //    Log.d("jumlah surat jalan", "" + length);
                       //     Log.d("pesan",pesan);
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
        //        .append(month+1).append("-").append(day));
        tglTerima.setText(new StringBuilder().append(day).append("-").append(month + 1)
                    .append("-").append(year));
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
                Log.d("tahun",tahun);
                Log.d("bulan",bulan);
                Log.d("hari",hari);
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
}
