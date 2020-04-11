package Models;

import java.io.Serializable;

public class Cursa implements Entity<Integer>, Serializable {
    private Integer id;
    private int capacitate;

    public Cursa(Integer id,Integer capa){
        this.id=id;
        this.capacitate=capa;

    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setID(Integer integer) {
        this.id=integer;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(Integer capacitate) {
        this.capacitate = capacitate;
    }
}
