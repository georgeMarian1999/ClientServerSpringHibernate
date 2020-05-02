package Models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Participant")
public class Participant implements Serializable{
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ParticipantId")
    private Integer id;
    @Column(name = "nume")
    private String nume;
    @Column(name = "EchipaId")
    private int EchipaId;

    public Participant(){

    }

    public Participant(Integer ID,String name,int echipaId){
        this.id=ID;
        this.nume=name;
        this.EchipaId=echipaId;

    }


    public Integer getId() {
        return this.id;
    }
    public void setId(Integer integer) {
        this.id=integer;
    }

    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getEchipaId() {
        return EchipaId;
    }

    public void setEchipaId(int echipaId) {
        EchipaId = echipaId;
    }

    @ManyToMany(mappedBy = "participanti")
    private Set<Cursa> curse=new HashSet<>();

    public Set<Cursa> getCurse(){
        return curse;
    }

}
