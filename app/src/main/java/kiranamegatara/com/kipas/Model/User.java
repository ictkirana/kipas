package kiranamegatara.com.kipas.Model;

import android.os.Parcel;
import android.os.Parcelable;

//import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by vemfiska on 17/10/16.
 */

//@IgnoreExtraProperties
public class User{
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getIs_kirana() {
        return is_kirana;
    }

    public void setIs_kirana(String is_kirana) {
        this.is_kirana = is_kirana;
    }

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPlant_code() {
        return plant_code;
    }

    public void setPlant_code(String plant_code) {
        this.plant_code = plant_code;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getAuthorized_warehouse() {
        return authorized_warehouse;
    }

    public void setAuthorized_warehouse(String authorized_warehouse) {
        this.authorized_warehouse = authorized_warehouse;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getIs_reset() {
        return is_reset;
    }

    public void setIs_reset(String is_reset) {
        this.is_reset = is_reset;
    }

    public String getFail_counter() {
        return fail_counter;
    }

    public void setFail_counter(String fail_counter) {
        this.fail_counter = fail_counter;
    }

    public String getDate_last_login() {
        return date_last_login;
    }

    public void setDate_last_login(String date_last_login) {
        this.date_last_login = date_last_login;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    String id;
    String full_name;
    String password;

    public User() {
    }

    String email;
    String nik;


    public User(String id, String full_name, String password, String email, String nik,
                String is_kirana, String company_code, String company_name, String plant_code,
                String plant_name, String authorized_warehouse, String is_active, String is_reset,
                String fail_counter, String date_last_login, String date_updated, String date_created, String is_deleted) {
        this.id = id;
        this.full_name = full_name;
        this.password = password;
        this.email = email;
        this.nik = nik;
        this.is_kirana = is_kirana;
        this.company_code = company_code;
        this.company_name = company_name;
        this.plant_code = plant_code;
        this.plant_name = plant_name;
        this.authorized_warehouse = authorized_warehouse;
        this.is_active = is_active;
        this.is_reset = is_reset;
        this.fail_counter = fail_counter;
        this.date_last_login = date_last_login;
        this.date_updated = date_updated;
        this.date_created = date_created;
        this.is_deleted = is_deleted;
    }

    String is_kirana;
    String company_code;
    String company_name;
    String plant_code;
    String plant_name;
    String authorized_warehouse;
    String is_active;
    String is_reset;
    String fail_counter;
    String date_last_login;
    String date_updated;
    String date_created;
    String is_deleted;

}

