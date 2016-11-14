package kiranamegatara.com.kipas.Controller;

import android.app.Application;

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
        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .schemaVersion(0)
                .migration(new DataMigration()).build();
        Realm.setDefaultConfiguration(configuration);
    }

    private class DataMigration implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();
            if (oldVersion == 0){
                schema.create("SrtJalan")
                        .addField("number",String.class)
                        .addField("plant",String.class);
                oldVersion++;
            }
        }
    }
}
