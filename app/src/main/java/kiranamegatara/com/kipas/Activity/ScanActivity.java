package kiranamegatara.com.kipas.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.RealmHelper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView mScannerView;
    RealmHelper realmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        QrScanner();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        String barcode = rawResult.getText().toString();
        Log.d("result1",rawResult.getText());
        Log.d("result2",rawResult.getText().toString());

        Intent intent = new Intent(ScanActivity.this,ScanResultActivity.class);
        intent.putExtra("barcode",barcode);
        startActivity(intent);

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
