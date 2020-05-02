package Service;

import Models.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class HibernateTesting {
    public static void main(String ... args){
        HibernateService service=new HibernateService();
        Angajat a=new Angajat(1,"mgar1992","12234");
        System.out.println(service.LocalLogin(a.getUsername(),a.getPassword()));
        service.InscriereParticipant(125,"Ariel","Audi");




        /*Iterable<DTOBJPartCapa> result=service.searchByTeam("Honda");
         List<DTOBJPartCapa> partCapas=new ArrayList<>();
        for (DTOBJPartCapa c:result
             ) {
            partCapas.add(c);
        }
        for (DTOBJPartCapa c:partCapas
             ) {
            System.out.println(c.getNume()+" "+c.getCapactiate());
        }*/
        /*Iterable<DTOBJCursa> curse=service.GroupByCapacitate();
        for (DTOBJCursa c:curse
             ) {
            System.out.println(c.getId()+" "+c.getCapacitate()+" "+c.getNrinscrisi());
        }*/



        //System.out.println("Echipa este");
        // System.out.println(service.getEchipa("Suzuki").getId()+service.getEchipa("Suzuki").getNume());
        service.close();
    }
}
