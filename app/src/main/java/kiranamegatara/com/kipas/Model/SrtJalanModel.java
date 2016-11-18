package kiranamegatara.com.kipas.Model;

/**
 * Created by vemfiska on 10/11/16.
 */

public class SrtJalanModel {

    String nosurat;
    String plant;
    String gudang;
    String fullname;
    String is_scaned;
    String date_scaned;
    String date_received;

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

    /*
    public SrtJalanModel(String nosurat, String plant) {
        this.nosurat = nosurat;
        this.plant = plant;
    }
*/

    public SrtJalanModel(String nosurat, String plant, String gudang,
                         String fullname, String is_scaned, String date_scaned,String date_received) {
        this.nosurat = nosurat;
        this.plant = plant;
        this.gudang = gudang;
        this.fullname = fullname;
        this.is_scaned = is_scaned;
        this.date_scaned = date_scaned;
        this.date_received = date_received;
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
