package kiranamegatara.com.kipas.Controller;

import android.app.Application;
import android.os.StrictMode;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import io.realm.annotations.RealmModule;
import kiranamegatara.com.kipas.Model.LoginUser;
import kiranamegatara.com.kipas.Model.SrtJalan;
import kiranamegatara.com.kipas.Model.SuratJalan;

/**
 * Created by vemfiska on 12/10/16.
 */
public class BaseApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        /*
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .schemaVersion(2)
                .migration(new DataMigration()).build();
        Realm.setDefaultConfiguration(config);

        */
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .setModules(new MyRealmModule())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }

    @RealmModule(classes = {SrtJalan.class, LoginUser.class, SuratJalan.class})
    public class MyRealmModule{}

    private class DataMigration implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();
            if (oldVersion == 0){
                schema.create("SrtJalan")
                        .addField("number",String.class)
                        .addField("plant",String.class);
                oldVersion++;
            }else if (oldVersion == 1){
                schema.create("SrtJalan")
                        .addField("number",String.class)
                        .addField("plant",String.class)
                        .addField("gudang",String.class)
                        .addField("fullname", String.class)
                        .addField("is_scaned",String.class)
                        .addField("date_scaned",String.class)
                        .addField("date_received",String.class);
                oldVersion++;
            }
        }
    }

}
