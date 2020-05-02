package server;

import Models.*;
import Service.HibernateService;
import Services.IObserver;
import Services.IServices;
import Services.ServerException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImplementationHibernate implements IServices {

    private HibernateService service;
    private Iterable<DTOBJCursa> cursedisp;
    private Map<String,IObserver> loggedEmployees;

    private final int noOfThreads=5;

    public ServicesImplementationHibernate(HibernateService service1){
        this.service=service1;
        this.cursedisp=service1.GroupByCapacitate();
        this.loggedEmployees=new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(DTOAngajat angajat, IObserver client) throws ServerException {
        boolean isEmployee=this.service.LocalLogin(angajat.getUsername(),angajat.getPassword());
        if(isEmployee){
            if(loggedEmployees.get(angajat.getUsername())!=null){
                throw new ServerException("User is already logged in");
            }
            loggedEmployees.put(angajat.getUsername(),client);
        }
        else throw new ServerException("Wrong username or password");
    }

    @Override
    public synchronized void logout(DTOAngajat angajat, IObserver client) throws ServerException {
        IObserver localclient=loggedEmployees.remove(angajat.getUsername());
        if(localclient==null){
            throw new ServerException("User "+angajat.getUsername()+" is not logged in");
        }
    }
    public DTOBJCursa[] convert(Iterable<DTOBJCursa> source){
        ArrayList<DTOBJCursa> result=new ArrayList<>();
        for (DTOBJCursa c : source){
            result.add(c);
        }
        return result.toArray(new DTOBJCursa[result.size()]);
    }
    public DTOBJPartCapa[] convert2(Iterable<DTOBJPartCapa> source){
        ArrayList<DTOBJPartCapa> result=new ArrayList<>();
        for (DTOBJPartCapa c:source
             ) {
            result.add(c);
        }
        return result.toArray(new DTOBJPartCapa[result.size()]);
    }
    private void notifyEmployeeSubmitted() throws ServerException{
        Iterable<Angajat> allEmployees=this.service.getAllEmployees();
        this.cursedisp=this.service.GroupByCapacitate();
        DTOBJCursa[] result=convert(this.cursedisp);
        ExecutorService executor= Executors.newFixedThreadPool(noOfThreads);
        for (Angajat a:allEmployees
             ) {
            IObserver local=loggedEmployees.get(a.getUsername());
            if(local!=null){
                executor.execute(()->{
                    try{
                        local.AngajatSubmitted(result);
                    }catch (ServerException | RemoteException e){
                        System.err.println("Error notifying about submit");
                    }
                });
            }
        }
        executor.shutdown();
    }
    @Override
    public void submitInscriere(DTOInfoSubmit infoSubmit) throws ServerException {
        try{
            this.service.InscriereParticipant(infoSubmit.getCapacitate(),infoSubmit.getNumePart(),infoSubmit.getNumeEchipa());
            notifyEmployeeSubmitted();
        }catch (ServerException e){
            throw new ServerException("Could not submit... "+e);
        }
    }

    @Override
    public DTOBJCursa[] getCurseDisp() throws ServerException {
        return convert(this.service.GroupByCapacitate());
    }

    @Override
    public DTOBJPartCapa[] searchByTeam(String team) throws ServerException {
        return convert2(this.service.searchByTeam(team));
    }

    @Override
    public String[] getAllTeams() throws ServerException {
        return this.service.getAllTeams();
    }
}
