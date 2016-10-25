package kiranamegatara.com.kipas.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.RealmHelper;

public class ScanResultActivity extends AppCompatActivity {
    TextView number,plant,vendor;
    Button simpan,kembali;

    RealmHelper realmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        number = (TextView)findViewById(R.id.textNo);
        plant = (TextView)findViewById(R.id.textPlant);
        vendor = (TextView)findViewById(R.id.textVendor);

        simpan = (Button)findViewById(R.id.btnSimpan);
        kembali = (Button)findViewById(R.id.btnBackScan);

        Intent intent = getIntent();
        final String nosurat = intent.getStringExtra("barcode");
        final String pabrik = "PT. Djambi Waras";
        final String vndr = "PT. Angkut Barang";
        number.setText(nosurat);
        plant.setText(pabrik);
        vendor.setText(vndr);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realmHelper = new RealmHelper(ScanResultActivity.this);
                realmHelper.addBarcode(nosurat,pabrik,vndr);
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
}
