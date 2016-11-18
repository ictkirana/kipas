package kiranamegatara.com.kipas.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vemfiska on 10/11/16.
 */

public class SrtJalan extends RealmObject {

    private String nosurat;
    private String plant;
    private String gudang;
    private String fullname;
    private String is_scaned;
    private String date_scaned;
    private String date_received;

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public String getPolisi_no() {
        return polisi_no;
    }

    public void setPolisi_no(String polisi_no) {
        this.polisi_no = polisi_no;
    }

    private String date_sent;
    private String polisi_no;

    public String getNosurat() {
        return nosurat;
    }

    public void setNosurat(String nosurat) {
        this.nosurat = nosurat;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getGudang() {
        return gudang;
    }

    public void setGudang(String gudang) {
        this.gudang = gudang;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIs_scaned() {
        return is_scaned;
    }

    public void setIs_scaned(String is_scaned) {
        this.is_scaned = is_scaned;
    }

    public String getDate_scaned() {
        return date_scaned;
    }

    public void setDate_scaned(String date_scaned) {
        this.date_scaned = date_scaned;
    }

    public String getDate_received() {
        return date_received;
    }

    public void setDate_received(String date_received) {
        this.date_received = date_received;
    }


}
