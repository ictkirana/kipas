package kiranamegatara.com.kipas.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.RealmHelper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView mScannerView;
    RealmHelper realmHelper;
    AQuery a;
    String email,plant,nopol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Intent inte = getIntent();
        email = inte.getStringExtra("email_user");
        plant = inte.getStringExtra("plant");
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
        String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_sj";


        //final String[] plant = new String[1];
        final String[] tglKirim = new String[1];

        Log.d("barcode",""+barcode);
        Log.d("plant",""+plant);

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("srt_jln_no",barcode);
        params.put("plant_code",plant);

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
                        Intent intent = new Intent(getApplicationContext(),ScanResultActivity.class);
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
                                }
                            }

                            intent.putExtra("surat_jalan_no",barcode);
                            //intent.putExtra("plant", plant[0]);
                            intent.putExtra("plant", plant);
                            intent.putExtra("tglKirim", tglKirim[0]);
                            intent.putExtra("email_user",email);
                            intent.putExtra("polisi_no",nopol);
                            startActivity(intent);
                        }else {
                            /*
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setMessage("No Surat Jalan tidak valid!").setTitle("Error");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            */
                            Toast.makeText(getApplicationContext(),"No Surat Jalan tidak valid!",Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        /*
        Intent intent = new Intent(ScanActivity.this,ScanResultActivity.class);
        intent.putExtra("surat_jalan_no",barcode);
        intent.putExtra("plant", plant);
        startActivity(intent);
        */

        /*
        realmHelper = new RealmHelper(BarcodeScanner.this);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = txtBarcode.getText().toString();
                Toast.makeText(getApplicationContext(),number,Toast.LENGTH_LONG).show();
                realmHelper.addBarcode(number);
                dialog.dismiss();
            }
        });
        dialog.show();
        */
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
}
