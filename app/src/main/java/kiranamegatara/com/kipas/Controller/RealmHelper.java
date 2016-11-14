package kiranamegatara.com.kipas.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import kiranamegatara.com.kipas.Model.SrtJalan;
import kiranamegatara.com.kipas.Model.SrtJalanModel;
import kiranamegatara.com.kipas.Model.SuratJalan;
import kiranamegatara.com.kipas.Model.SuratJalanModel;

/**
 * Created by vemfiska on 12/10/16.
 */
public class RealmHelper {
    private Realm realm;
    private RealmResults<SrtJalan> realmResults;
    private Context c;

    public RealmHelper(Context c){
        this.c = c;
        this.realm = realm.getInstance(c);
    }

    public void addBarcode(String number, String plant){

        SrtJalan srtJalan = new SrtJalan();
        srtJalan.setNosurat(number);
        srtJalan.setPlant(plant);

        realm.beginTransaction();
        realm.copyToRealm(srtJalan);
        realm.commitTransaction();

        //Toast.makeText(c,"Berhasil Simpan",Toast.LENGTH_LONG).show();
    }

    public ArrayList<SrtJalanModel> findAll(){
        ArrayList<SrtJalanModel> data = new ArrayList<>();
        realmResults = realm.where(SrtJalan.class).findAll();
        if (realmResults.size() > 0){
            for (int i = 0; i < realmResults.size(); i++){
                String number;
                String pabrik;

                number = realmResults.get(i).getNosurat();
                pabrik = realmResults.get(i).getPlant();

                Log.d("number",number);
                Log.d("pabrik",pabrik);


                data.add(new SrtJalanModel(number,pabrik));
            }
        }else {
            Toast.makeText(c,"Tidak ada data disimpan",Toast.LENGTH_LONG).show();
        }

        return data;
    }

    public ArrayList<SrtJalanModel> deleteRealm(){
        ArrayList<SrtJalanModel> data = new ArrayList<>();
        realmResults = realm.where(SrtJalan.class).findAll();
        realm.beginTransaction();
        realmResults.clear();
        realm.commitTransaction();
        return data;
    }
}
