package server;

import Models.*;
import Service.Service;
import Services.IObserver;
import Services.IServices;
import Services.ServerException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImplementation implements IServices {

    private Service service;
    private Iterable<Angajat> angajati;
    private Iterable<DTOBJCursa> cursedisp;
    private Map<String,IObserver> loggedEmployees;

    private final int noOfThreads=5;

    public ServicesImplementation(Service service1){
        service=service1;
        angajati=service1.findAllEmployees();
        cursedisp=service1.GroupByCapacitate();
        loggedEmployees=new ConcurrentHashMap<>();
    }


    public synchronized void login(DTOAngajat angajat, IObserver client) throws ServerException {
        boolean isEmployee=this.service.LocalLogin(angajat.getUsername(),angajat.getPassword());
        if(isEmployee){
            if(loggedEmployees.get(angajat.getUsername())!=null){
                throw new ServerException("User is already logged in");
            }
            loggedEmployees.put(angajat.getUsername(),client);
        }else{
            System.out.println("Authentiocation failed");
            throw new ServerException("Wrong username or password");
        }

    }


    private void notifyEmployeeSubmitted(DTOAngajat angajat) throws ServerException{
        System.out.println("S-a apelat notifyEmployeeSubmitted");
        Iterable<Angajat> employees=this.service.findAllEmployees();
        this.cursedisp=this.service.GroupByCapacitate();
        DTOBJCursa[] result=convert(this.cursedisp);
        ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
        for(Angajat ang : employees){

            IObserver client=loggedEmployees.get(ang.getUsername());
            System.out.println(client);
            if(client!=null){
            executor.execute(()->{
                try{
                    System.out.println("Notifiying employee "+ang.getUsername()+" employee "+angajat.getUsername()+" submitted");
                    client.AngajatSubmitted(result);
                    System.out.println("Employee "+ang.getUsername()+" notified");
                }catch (ServerException |RemoteException e){
                    System.err.println("Error notifying about submit");
                }
            });
            }else System.out.println("Error gettting logged in employees");
        }
        executor.shutdown();
    }
    public DTOBJCursa[] convert(Iterable<DTOBJCursa> source){
        ArrayList<DTOBJCursa> result=new ArrayList<>();
        for (DTOBJCursa c : source){
            result.add(c);
        }
        return result.toArray(new DTOBJCursa[result.size()]);
    }
    public synchronized void logout(DTOAngajat angajat, IObserver client) throws ServerException {
        IObserver localClient=loggedEmployees.remove(angajat.getUsername());
        if(localClient==null){
            throw new ServerException("User "+angajat.getUsername()+" is not logged in");
        }
    }


    public synchronized void submitInscriere(DTOInfoSubmit infoSubmit) throws ServerException {
        System.out.println("Submitting by "+infoSubmit.getUserWho()+" ....");
        try{
            this.service.InscriereParticipant(infoSubmit.getCapacitate(),infoSubmit.getNumePart(),infoSubmit.getNumeEchipa());
            System.out.println("New submit saved in database");
            notifyEmployeeSubmitted(infoSubmit.getWho());
        }catch(ServerException e){
            throw new ServerException("Could not submit ..."+e);
        }
    }



    public synchronized DTOBJCursa[] getCurseDisp() throws ServerException {
        ArrayList<DTOBJCursa> result=new ArrayList<>();
        for(DTOBJCursa c :this.cursedisp){
            result.add(c);
        }
        return result.toArray(new DTOBJCursa[result.size()]);
    }


    public synchronized DTOBJPartCapa[] searchByTeam(String team) throws ServerException {
        Iterable<DTOBJPartCapa> result=this.service.cautare(team);
        ArrayList<DTOBJPartCapa> ret=new ArrayList<>();
        for(DTOBJPartCapa part : result){
            ret.add(part);
        }

        return ret.toArray(new DTOBJPartCapa[ret.size()]);
    }
}
