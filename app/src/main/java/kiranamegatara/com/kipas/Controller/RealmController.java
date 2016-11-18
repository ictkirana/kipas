package kiranamegatara.com.kipas.Controller;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;
import kiranamegatara.com.kipas.Fragment.Outstanding;
import kiranamegatara.com.kipas.Model.SrtJalan;

/**
 * Created by vemfiska on 16/11/16.
 */

    public class RealmController {

        private static RealmController instance;
        private final Realm realm;

        public RealmController(Application application) {
            realm = Realm.getDefaultInstance();
        }

        public static RealmController with(Outstanding fragment) {

            if (instance == null) {
                instance = new RealmController(fragment.getActivity().getApplication());
            }
            return instance;
        }

        public static RealmController with(Activity activity) {

            if (instance == null) {
                instance = new RealmController(activity.getApplication());
            }
            return instance;
        }

        public static RealmController with(Application application) {

            if (instance == null) {
                instance = new RealmController(application);
            }
            return instance;
        }

        public static RealmController getInstance() {

            return instance;
        }

        public Realm getRealm() {

            return realm;
        }

        //Refresh the realm istance
        public void refresh() {

            realm.refresh();
        }

        //clear all objects from Book.class
        public void clearAll() {

            realm.beginTransaction();
            realm.clear(SrtJalan.class);
            realm.commitTransaction();
        }

        //find all objects in the Book.class
        public RealmResults<SrtJalan> getBooks() {

            return realm.where(SrtJalan.class).findAll();
        }

        //query a single item with the given id
        public RealmResults<SrtJalan> getHistory(String id) {

            return realm.where(SrtJalan.class).equalTo("is_scaned", "1").findAll();
        }

        //check if Book.class is empty
        public boolean hasBooks() {

            return !realm.allObjects(SrtJalan.class).isEmpty();
        }

        //query example
        public RealmResults<SrtJalan> getOutstanding() {

            return realm.where(SrtJalan.class)
                    .equalTo("is_scaned","0")
                    .findAll();

        }

}
