package kiranamegatara.com.kipas.Controller;

import android.app.Application;
import android.os.StrictMode;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by vemfiska on 12/10/16.
 */
public class BaseApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .schemaVersion(0)
                .migration(new DataMigration()).build();
        Realm.setDefaultConfiguration(config);
    }

    private class DataMigration implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();
            if (oldVersion == 0){
                schema.create("SrtJalan")
                        .addField("number",String.class)
                        .addField("plant",String.class);
/*
                        .addField("gudang",String.class)
                        .addField("fullname", String.class)
                        .addField("is_scaned",String.class)
                        .addField("date_scaned",String.class)
                        .addField("date_received",String.class);
*/
                oldVersion++;
            }
        }
    }
}
