package kiranamegatara.com.kipas.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import kiranamegatara.com.kipas.Model.SrtJalan;
import kiranamegatara.com.kipas.Model.SrtJalanModel;

/**
 * Created by vemfiska on 12/10/16.
 */
public class RealmHelper {
    private Realm realm;
    private RealmResults<SrtJalan> realmResults;
    private Context c;

    public RealmHelper(Context c) {
        this.c = c;
        this.realm = realm.getInstance(c);
    }
/*
    public void addBarcode(String number, String plant){

        SrtJalan srtJalan = new SrtJalan();
        srtJalan.setNosurat(number);
        srtJalan.setPlant(plant);

        realm.beginTransaction();
        realm.copyToRealm(srtJalan);
        realm.commitTransaction();

        //Toast.makeText(c,"Berhasil Simpan",Toast.LENGTH_LONG).show();
    }
*/

    public void addBarcode(String number, String plant,String gudang,String fullname,
                           String is_scaned, String date_scaned,String date_received){

        SrtJalan srtJalan = new SrtJalan();
        srtJalan.setNosurat(number);
        srtJalan.setPlant(plant);
        srtJalan.setGudang(gudang);
        srtJalan.setFullname(fullname);
        srtJalan.setIs_scaned(is_scaned);
        srtJalan.setDate_scaned(date_scaned);
        srtJalan.setDate_received(date_received);

        realm.beginTransaction();
        realm.copyToRealm(srtJalan);
        realm.commitTransaction();

        //Toast.makeText(c,"Berhasil Simpan",Toast.LENGTH_LONG).show();
    }

/*
    public void addUser(String nik,String fullname,String plant,String gudang){
        LoginUser user = new LoginUser();
        user.setNik(nik);
        user.setFullname(fullname);
        user.setPlant(plant);
        user.setGudang(gudang);

        realm.beginTransaction();
        realm.copyFromRealm(user);
        realm.commitTransaction();
    }
*/
    public ArrayList<SrtJalanModel> findAll(){
        ArrayList<SrtJalanModel> data = new ArrayList<>();
        realmResults = realm.where(SrtJalan.class).findAll();
        if (realmResults.size() > 0){
            for (int i = 0; i < realmResults.size(); i++){
                String number,pabrik,gudang,fullname,is_scaned,date_scaned,date_received;

                number = realmResults.get(i).getNosurat();
                pabrik = realmResults.get(i).getPlant();
                gudang = realmResults.get(i).getGudang();
                fullname = realmResults.get(i).getFullname();
                is_scaned = realmResults.get(i).getIs_scaned();
                date_scaned = realmResults.get(i).getDate_scaned();
                date_received = realmResults.get(i).getDate_received();

                Log.d("number",number);
                Log.d("pabrik",pabrik);


                data.add(new SrtJalanModel(number,pabrik,gudang,fullname,is_scaned,date_scaned,date_received));
               // data.add(new SrtJalanModel(number,pabrik));
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
