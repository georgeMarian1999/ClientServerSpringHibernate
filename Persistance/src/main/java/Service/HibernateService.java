package Service;

import Models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class HibernateService {
    private static SessionFactory sessionFactory;
    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.out.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    public void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public HibernateService(){
        initialize();
    }
    public Iterable<Angajat> getAllEmployees(){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="SELECT new Angajat(A.id,A.username,A.password) FROM Angajat A";
            Query query=session.createQuery(hql);
            List result=query.getResultList();
            List<Angajat> employees=new ArrayList<>();
            for (var object:result
                 ) {
                employees.add((Angajat)object);
            }
            return employees;
        }
    }
    public Angajat getAngajat(String username){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="SELECT new Angajat(A.id,A.username,A.password) FROM Angajat A where A.username=:user";
            Query query=session.createQuery(hql);
            query.setParameter("user",username);
            List result=query.setMaxResults(1).getResultList();
            session.close();
            return (Angajat)result.get(0);
        }
    }

    public List<Participant> getPartFromEchipa(String nume){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="SELECT new Participant(P.id,P.nume,P.EchipaId) FROM Participant P  "+
                    "INNER JOIN Echipa E on P.EchipaId=E.id";
            Query query=session.createQuery(hql);
            List result=query.getResultList();
            List<Participant> participants=new ArrayList<>();
            for (var o:result
                 ) {
                participants.add((Participant)o);
            }
            return participants;
        }
    }
    public int findIdCursaByCapacitate(int capacitate){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="SELECT C.id FROM Cursa C WHERE C.capacitate=:cap";
            Query query=session.createQuery(hql);
            query.setParameter("cap",capacitate);
            List result=query.setMaxResults(1).getResultList();
            int id=(int)result.get(0);
            return id;
        }
    }
    public int findMaxParticipantId(){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="SELECT MAX(P.id) FROM Participant P";
            Query query=session.createQuery(hql);
            List result=query.setMaxResults(1).getResultList();
            return (int)result.get(0);
        }
    }
    public int findEchipaIdByName(String numeEchipa){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="SELECT E.id FROM Echipa E WHERE E.nume=:nume";
            Query query=session.createQuery(hql);
            query.setParameter("nume",numeEchipa);
            List result=query.setMaxResults(1).getResultList();
            return (int)result.get(0);
        }
    }
    public void InscriereParticipant(int capacitate,String numeParticipant,String numeEchipa){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            int idCursa,idEchipa,idParticipant,idInscriere;
            idParticipant=findMaxParticipantId()+1;
            idEchipa=findEchipaIdByName(numeEchipa);
            idCursa=findIdCursaByCapacitate(capacitate);
            Participant Nou=new Participant(idParticipant,numeParticipant,idEchipa);
            Add(Nou);
            Inscriere Noua=new Inscriere(idParticipant,idCursa);
            Add(Noua);
        }
    }

    public void Add(Object o){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            session.save(o);
            session.getTransaction().commit();
        }
    }
    public Iterable<DTOBJCursa> GroupByCapacitate(){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="SELECT new DTOBJCursa(C.id,C.capacitate,COUNT(P.id)) "+
                    "FROM Participant P INNER JOIN Inscriere I on P.id=I.ParticipantId "+
                    "INNER JOIN Cursa C on I.CursaId=C.id GROUP BY C.capacitate";
            Query query=session.createQuery(hql);
            List result=query.getResultList();
            List<DTOBJCursa> curse=new ArrayList<>();
            for (var o:result
                 ) {
                curse.add((DTOBJCursa)o);
            }
            return curse;
        }
    }
    public Iterable<DTOBJPartCapa> searchByTeam(String team){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="SELECT new DTOBJPartCapa(P.id,P.nume,C.capacitate) "+
                    "FROM Participant P INNER JOIN Inscriere I ON P.id=I.ParticipantId "+
                    "INNER JOIN Cursa C on I.CursaId=C.id INNER JOIN Echipa E on P.EchipaId=E.id "+
                    "WHERE E.nume= :team";
            Query query=session.createQuery(hql);
            query.setParameter("team",team);
            List result=query.getResultList();
            List<DTOBJPartCapa> re=new ArrayList<>();
            for (var c:result
                 ) {
                re.add((DTOBJPartCapa)c);
            }
            return re;
        }
    }
    public String[] getAllTeams(){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="SELECT E.nume FROM Echipa E";
            Query query=session.createQuery(hql);
            List result=query.getResultList();
            List<String> allteams=new ArrayList<>();
            for (var obj:result
                 ) {
                allteams.add((String)obj);
            }
            return allteams.toArray(new String[allteams.size()]);
        }
    }
    public Echipa getEchipa(String name){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="FROM Echipa E where E.nume= :nume";
            Query query= session.createQuery(hql);
            query.setParameter("nume",name);
            List result=query.setMaxResults(1).getResultList();
            Echipa e=(Echipa)result.get(0);
            return e;
        }
    }
    public boolean LocalLogin(String username,String password){
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            String hql="FROM Angajat A where A.username= :username "+
                        "and A.password= :password";
            Query query= session.createQuery(hql);
            query.setParameter("username",username);
            query.setParameter("password",password);
            List result=query.setMaxResults(1).getResultList();
            if(result.isEmpty())
                return false;
            else return true;
        }
    }
}
