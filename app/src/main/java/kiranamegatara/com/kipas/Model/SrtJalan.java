package kiranamegatara.com.kipas.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vemfiska on 10/11/16.
 */

public class SrtJalan extends RealmObject {
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

    @PrimaryKey
    private String nosurat;
    private String plant;
}
