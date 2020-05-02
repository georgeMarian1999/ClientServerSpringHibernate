package Services;

import Models.DTOAngajat;
import Models.DTOBJCursa;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {
    void AngajatSubmitted(DTOBJCursa[] curse) throws ServerException,RemoteException;
}
