package Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Entity(name="DTOBJCursa")
@Table(name = "DTOBJCursa")
public class DTOBJCursa implements Serializable{
    @Id
    private int id;
    @Column(name = "capacitate")
    private int capacitate;
    @Column(name = "Nrinscrisi")
    private int Nrinscrisi;

    public DTOBJCursa(){

    }

    public DTOBJCursa(int idcursa,int cap,long nr){
        this.id=idcursa;
        this.capacitate=cap;
        this.Nrinscrisi=(int)nr;
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer integer) {
        this.id=integer;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(int capacitate) {
        this.capacitate = capacitate;
    }

    public int getNrinscrisi() {
        return Nrinscrisi;
    }

    public void setNrinscrisi(int nrinscrisi) {
        Nrinscrisi = nrinscrisi;
    }
}
