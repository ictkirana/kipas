package kiranamegatara.com.kipas.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vemfiska on 12/10/16.
 */
public class SuratJalan extends RealmObject {
    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurat_jalan_no() {
        return surat_jalan_no;
    }

    public void setSurat_jalan_no(String surat_jalan_no) {
        this.surat_jalan_no = surat_jalan_no;
    }

    public String getPlant_code() {
        return plant_code;
    }

    public void setPlant_code(String plant_code) {
        this.plant_code = plant_code;
    }

    public String getPlant_description() {
        return plant_description;
    }

    public void setPlant_description(String plant_description) {
        this.plant_description = plant_description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
    }

    public String getIs_scanned() {
        return is_scanned;
    }

    public void setIs_scanned(String is_scanned) {
        this.is_scanned = is_scanned;
    }

    public int getUpdate_counter() {
        return update_counter;
    }

    public void setUpdate_counter(int update_counter) {
        this.update_counter = update_counter;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public String getDate_received() {
        return date_received;
    }

    public void setDate_received(String date_received) {
        this.date_received = date_received;
    }

    public String getDate_scaned() {
        return date_scaned;
    }

    public void setDate_scaned(String date_scaned) {
        this.date_scaned = date_scaned;
    }

    public String getDate_synchronized() {
        return date_synchronized;
    }

    public void setDate_synchronized(String date_synchronized) {
        this.date_synchronized = date_synchronized;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }


    private String surat_jalan_no;

    private String plant_code;
    private String plant_description;
    private String user_id;
    private String user_full_name;
    private String is_scanned;
    private int update_counter;
    private String date_sent;
    private String date_received;
    private String date_scaned;
    private String date_synchronized;
    private String date_created;
    private String date_updated;
    private String is_deleted;
}
