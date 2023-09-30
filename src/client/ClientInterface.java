package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    public String fetchStudentNumber() throws RemoteException;
    public String fetchPassword() throws RemoteException;
}
