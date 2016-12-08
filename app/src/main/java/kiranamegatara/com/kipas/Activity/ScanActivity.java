package kiranamegatara.com.kipas.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import kiranamegatara.com.kipas.Controller.SessionManager;
import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.RealmHelper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView mScannerView;
    RealmHelper realmHelper;
    AQuery a;
    String email,plant,nopol,fullname, nik,gudang,date_scaned,is_scaned;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        plant = user.get(SessionManager.keyPlant);
        gudang = user.get(SessionManager.keyGudang);


        Intent inte = getIntent();
        email = inte.getStringExtra("email_user");
        //plant = inte.getStringExtra("plant");
        fullname = inte.getStringExtra("fullname");
        nik = inte.getStringExtra("nik");
        //gudang = inte.getStringExtra("gudang");
        Log.d("email",""+email);
        Log.d("plant",""+plant);
        QrScanner();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        final String barcode = rawResult.getText().toString();
        Log.d("result1",rawResult.getText());
        Log.d("result2",rawResult.getText().toString());

        Toast.makeText(getApplicationContext(),rawResult.getText().toString(),Toast.LENGTH_LONG).show();

        a = new AQuery(ScanActivity.this);
        //String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_sj";
        String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/get_sj";
//        String url = "http://10.0.9.35/ci/index.php/cls_ws_sir/get_sj";


        //final String[] plant = new String[1];
        final String[] tglKirim = new String[1];
        final String[] datescan = new String[1];
        final String[] isscan = new String[1];

        Log.d("barcode",""+barcode);
        Log.d("plant",""+plant);

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("srt_jln_no",barcode);
        params.put("plant_code",plant);
        params.put("authorized_warehouse",gudang);

        ProgressDialog progress = new ProgressDialog(ScanActivity.this);
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
                        final Intent intent = new Intent(getApplicationContext(),ScanResultActivity.class);
                        Log.d("pesan",pesan);
                        if (hasil.equalsIgnoreCase("true")){
                            JSONArray jsonarray = jsonObject.getJSONArray("data");
                            int length = jsonarray.length();
                            Log.d("jumlah data",""+length);
                            for (int i = 0; i < jsonarray.length(); i++){
                                if (i == 0) {
                                    JSONObject b = jsonarray.getJSONObject(i);
                                    //plant[0] = b.getString("plant_code");
                                    plant = b.getString("plant_code");
                                    tglKirim[0] = b.getString("date_sent");
                                    nopol = b.getString("polisi_no");
                                    isscan[0] = b.getString("is_scaned");
                                    datescan[0] = b.getString("date_scaned");

                                    //date_scaned = b.getString("date_scaned");

                                }
                            }

                            intent.putExtra("surat_jalan_no",barcode);
                            //intent.putExtra("plant", plant[0]);
                            intent.putExtra("plant", plant);
                            intent.putExtra("tglKirim", tglKirim[0]);
                            intent.putExtra("email_user",email);
                            intent.putExtra("polisi_no",nopol);
                            intent.putExtra("fullname",fullname);
                            intent.putExtra("nik",nik);
                            intent.putExtra("gudang",gudang);
                            intent.putExtra("is_scaned",isscan[0]);
                            intent.putExtra("date_scaned",datescan[0]);
                            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date date = dateFormat.parse(datescan[0]);
                                String formattime = String.valueOf(date);
                                Log.d("format tanggal",""+ date);
                                Log.d("format tanggal stirng",""+ formattime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Log.d("date_scaned di scan",""+datescan[0]);
                            if (isscan[0].equalsIgnoreCase("0")) {
                                startActivity(intent);
                                finish();
                            }else {
                                final Dialog dialog = new Dialog(ScanActivity.this);
                                dialog.setContentView(R.layout.alert_scan);
                                Button btnya = (Button)dialog.findViewById(R.id.button2);
                                Button btntdk = (Button)dialog.findViewById(R.id.button1);
                                btnya.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                btntdk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                                        finish();
                                    }
                                });
                                dialog.show();

                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"No Surat Jalan tidak valid!",Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void QrScanner() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    110);
        } else {
            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
            setContentView(mScannerView);

            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();         // Start camera
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mScannerView.stopCamera();
        }catch (NullPointerException n){

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                startActivity(new Intent(getApplicationContext(),Main2Activity.class));
        }
        return super.onKeyDown(keyCode, event);
    }
}
