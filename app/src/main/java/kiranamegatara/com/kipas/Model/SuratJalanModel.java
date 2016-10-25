package kiranamegatara.com.kipas.Model;

/**
 * Created by vemfiska on 12/10/16.
 */
public class SuratJalanModel {
    public SuratJalanModel(int id, String surat_jalan_no, String plant_code, String plant_description, String user_id, String user_full_name, String is_scanned, int update_counter, String date_sent, String date_received, String date_scaned, String date_synchronized, String date_created, String date_updated, String is_deleted) {
        this.id = id;
        this.surat_jalan_no = surat_jalan_no;
        this.plant_code = plant_code;
        this.plant_description = plant_description;
        this.user_id = user_id;
        this.user_full_name = user_full_name;
        this.is_scanned = is_scanned;
        this.update_counter = update_counter;
        this.date_sent = date_sent;
        this.date_received = date_received;
        this.date_scaned = date_scaned;
        this.date_synchronized = date_synchronized;
        this.date_created = date_created;
        this.date_updated = date_updated;
        this.is_deleted = is_deleted;
    }

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

    int id;
    String surat_jalan_no;
    String plant_code;
    String plant_description;
    String user_id;
    String user_full_name;
    String is_scanned;
    int update_counter;
    String date_sent;
    String date_received;
    String date_scaned;
    String date_synchronized;
    String date_created;
    String date_updated;
    String is_deleted;
}
