package Models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Echipa")
public class Echipa implements Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "EchipaId")
    private Integer id;
    @Column(name = "nume")
    private String nume;

    public Echipa(){

    }



    public Echipa(Integer id,String nume){
        this.id=id;
        this.nume=nume;
    }

    @OneToMany(cascade = {
            CascadeType.ALL,
    })
    @JoinColumn(name= "EchipaId")
    private List<Participant> participanti=new ArrayList<>();

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

    public List<Participant> getParticipanti(){
        return this.participanti;
    }

}
