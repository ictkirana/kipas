package kiranamegatara.com.kipas.Model;

import io.realm.RealmObject;

/**
 * Created by vemfiska on 14/11/16.
 */

public class LoginUser{
    private String nik;
    private String fullname;
    private String plant;

    public String getGudang() {
        return gudang;
    }

    public void setGudang(String gudang) {
        this.gudang = gudang;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    private String gudang;
}
