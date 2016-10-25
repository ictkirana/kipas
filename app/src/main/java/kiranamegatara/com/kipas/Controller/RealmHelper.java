package kiranamegatara.com.kipas.Controller;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import kiranamegatara.com.kipas.Model.SuratJalan;
import kiranamegatara.com.kipas.Model.SuratJalanModel;

/**
 * Created by vemfiska on 12/10/16.
 */
public class RealmHelper {
    private Realm realm;
    private RealmResults<SuratJalan> realmResults;
    private Context c;

    public RealmHelper(Context c){
        this.c = c;
        this.realm = realm.getInstance(c);
    }

    public void addBarcode(String number, String plant, String vendor){
/*
        SuratJalan srtJalan = new SuratJalan();
        srtJalan.setId((int)(System.currentTimeMillis()/1000));
        srtJalan.setNosurat(number);
        srtJalan.setPlant(plant);
        srtJalan.setVendor(vendor);

        realm.beginTransaction();
        realm.copyToRealm(srtJalan);
        realm.commitTransaction();
*/
        Toast.makeText(c,"Berhasil Simpan",Toast.LENGTH_LONG).show();
    }

    public ArrayList<SuratJalanModel> findAll(){
        ArrayList<SuratJalanModel> data = new ArrayList<>();
        realmResults = realm.where(SuratJalan.class).findAll();
        if (realmResults.size() > 0){
            for (int i = 0; i < realmResults.size(); i++){
                String number;
                String pabrik;
                String vdr;
                int id;
/*
                number = realmResults.get(i).getNosurat();
                id = realmResults.get(i).getId();
                pabrik = realmResults.get(i).getPlant();
                vdr = realmResults.get(i).getVendor();


                Log.d("id", String.valueOf(id));
                Log.d("number",number);
                Log.d("pabrik",pabrik);
                Log.d("vendor",vdr);


                data.add(new SuratJalanModel(id,number,pabrik,vdr));
                */
            }
        }else {
            Toast.makeText(c,"Tidak ada data disimpan",Toast.LENGTH_LONG).show();
        }

        return data;
    }
}
