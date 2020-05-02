package Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Entity
@Table(name = "Inscriere")
public class Inscriere implements Serializable {
    @Id
    @Column(name = "CursaId")
    private int CursaId;
    @Id
    @Column(name = "ParticipantId")
    private int ParticipantId;

    public Inscriere(){

    }

    public Inscriere(Integer idParticipant,Integer idCursa){
        this.ParticipantId=idParticipant;
        this.CursaId=idCursa;
    }

    public int getCursaId() {
        return CursaId;
    }

    public void setCursaId(int cursaId) {
        CursaId = cursaId;
    }

    public int getParticipantId() {
        return ParticipantId;
    }

    public void setParticipantId(int participantId) {
        ParticipantId = participantId;
    }
}
