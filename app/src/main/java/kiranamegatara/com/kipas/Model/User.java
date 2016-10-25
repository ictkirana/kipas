package kiranamegatara.com.kipas.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by vemfiska on 17/10/16.
 */
@IgnoreExtraProperties
public class User implements Parcelable{
    String full_name;
    String password;
    String email;
    String nik;
    String company_id;

    public User(String full_name, String password, String email, String nik, String company_id, String company_name, String is_active, String is_reset, String fail_counter, String date_last_login, String date_created, String date_updated) {
        this.full_name = full_name;
        this.password = password;
        this.email = email;
        this.nik = nik;
        this.company_id = company_id;
        this.company_name = company_name;
        this.is_active = is_active;
        this.is_reset = is_reset;
        this.fail_counter = fail_counter;
        this.date_last_login = date_last_login;
        this.date_created = date_created;
        this.date_updated = date_updated;
    }

    String company_name;
    String is_active;
    String is_reset;
    String fail_counter;
    String date_last_login;
    String date_created;

    public User() {

    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getDate_last_login() {
        return date_last_login;
    }

    public void setDate_last_login(String date_last_login) {
        this.date_last_login = date_last_login;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    String date_updated;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString("full_name");
        parcel.writeString("password");
        parcel.writeString("email");
        parcel.writeString("nik");
        parcel.writeString("company_id");
        parcel.writeString("company_name");
        parcel.writeString("is_active");
        parcel.writeString("is_reset");
        parcel.writeString("fail_counter");
        parcel.writeString("date_last_login");
        parcel.writeString("date_created");
        parcel.writeString("date_updated");
    }

    public User(Parcel in){
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        full_name = in.readString();
        password = in.readString();
        email = in.readString();
        nik = in.readString();
        company_id = in.readString();
        company_name = in.readString();
        is_active = in.readString();
        is_reset = in.readString();
        fail_counter = in.readString();
        date_last_login = in.readString();
        date_created = in.readString();
        date_updated = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public User createFromParcel(Parcel in) {
                    return new User(in);
                }

                public User[] newArray(int size) {
                    return new User[size];
                }
            };
}

