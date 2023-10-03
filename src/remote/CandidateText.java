package remote;

import java.rmi.RemoteException;

public class CandidateText extends Candidate{
    String pitch;
    public CandidateText(String firstName, String lastName, int rank, String pitch) throws RemoteException {
        super(firstName, lastName, rank);
        this.pitch = pitch;
    }
}
