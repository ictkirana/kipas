package kiranamegatara.com.kipas.Model;

/**
 * Created by vemfiska on 10/11/16.
 */

public class SrtJalanModel {
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

    public SrtJalanModel(String nosurat, String plant) {
        this.nosurat = nosurat;
        this.plant = plant;
    }

    String nosurat;
    String plant;
}
