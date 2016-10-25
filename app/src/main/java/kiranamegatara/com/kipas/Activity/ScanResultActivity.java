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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.RealmHelper;

public class ScanResultActivity extends AppCompatActivity {
    TextView number,plant,tglKirim,tglTerima,nopol;
    Button simpan,kembali;
    private Calendar calendar;
    private int year, month, day;
    String email;
    AQuery a;

    RealmHelper realmHelper;

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
        final String nosurat = intent.getStringExtra("surat_jalan_no");
        final String tanggalKirim = intent.getStringExtra("tglKirim");
        final String pabrik = intent.getStringExtra("Plant");
        email = intent.getStringExtra("email_user");

        number.setText(nosurat);
        plant.setText(pabrik);
        tglKirim.setText(tanggalKirim);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //realmHelper = new RealmHelper(ScanResultActivity.this);
                //realmHelper.addBarcode(nosurat,pabrik,vndr);
                SaveSuratJalan();
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

    }


    private void showDate(int year, int i, int day) {
        tglTerima.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
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
        if (id == 999){
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,myDateListener, year, month,day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            calendar.add(Calendar.DAY_OF_MONTH,-7);
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
}
