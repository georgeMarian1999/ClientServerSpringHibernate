package Models;

import com.sun.jdi.ArrayReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Cursa")
public class Cursa implements Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "CursaId")
    private Integer CursaId;
    @Column(name = "capacitate")
    private int capacitate;



    public Cursa(){

    }
    public Cursa(Integer id,Integer capa){
        this.CursaId=id;
        this.capacitate=capa;
    }
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "Inscriere",joinColumns = @JoinColumn(name="CursaId"),inverseJoinColumns = @JoinColumn(name="ParticipantId"))
    private Set<Participant> participanti =new HashSet<>();

    public void addParticipant(Participant p){
        participanti.add(p);
        p.getCurse().add(this);
    }
    public void removeParticipant(Participant p){
        participanti.remove(p);
        p.getCurse().remove(this);
    }

    public Integer getCursaId() {
        return this.CursaId;
    }

    public void setId(Integer integer) {
        this.CursaId=integer;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(Integer capacitate) {
        this.capacitate = capacitate;
    }
}
