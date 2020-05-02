package Models;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "Angajat")
public class Angajat implements Comparable<Angajat>,Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "AngajatId")
    private Integer id;
    @Column(name = "User")
    private String username;
    @Column(name = "Pass")
    private String password;

    public Angajat(){

    }

    public Angajat(Integer id,String user,String pass){
        this.id=id;
        this.username=user;
        this.password=pass;
    }


    @Override
    public String toString() {
        String string="Angajat "+username+" cu id-ul "+id;
        return string;
    }


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer integer) {
        this.id=integer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DTOAngajat convert(){
        DTOAngajat result=new DTOAngajat(this.username,this.password);
        return result;
    }

    @Override
    public int compareTo(Angajat o) {
        return this.username.compareTo(o.username);
    }
}
