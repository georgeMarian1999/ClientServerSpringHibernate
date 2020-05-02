package Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Entity(name = "DTOBJPartCapa")
@Table(name = "DTOBJPartCapa")
public class DTOBJPartCapa implements Serializable {
    @Id
    private Integer id;
    @Column(name = "nume")
    private String nume;
    @Column(name = "capacitate")
    private int capactiate;

    public DTOBJPartCapa(){

    }

    public DTOBJPartCapa(Integer ID,String Nume,int cap){
        this.id=ID;
        this.nume=Nume;
        this.capactiate=cap;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getCapactiate() {
        return capactiate;
    }

    public void setCapactiate(int capactiate) {
        this.capactiate = capactiate;
    }


    public Integer getId() {
        return this.id;
    }


    public void setID(Integer integer) {
        this.id=integer;
    }
}
