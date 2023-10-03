package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Candidate extends UnicastRemoteObject implements CandidateInterface{
    private String firstName;
    private String lastName;
    private int rank;
    private int score;

    private PitchInterface pitch;

    public Candidate(String firstName, String lastName, int rank, String pitchType, String pitchElement) throws RemoteException {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.rank = rank;
        this.score = 0;
        if(!pitchType.isEmpty()) {
            this.pitch = new Pitch(pitchType,pitchElement);
        }
        else {
            this.pitch = null;
        }
    }

    public String getPresentation() throws RemoteException {
        return firstName + " " + lastName + " (rank " + rank + ")";
    }

    public void addVote(int number) throws RemoteException {
        this.score += number;
        System.out.println(" - Added "+number+" point to the candidate "+this+" , currently have a score of "+score);
    }

    public void deleteVote(int number) throws RemoteException{
        this.score -= number;
        System.out.println(" - Removed "+number+" point to the candidate "+this+" , currently have a score of "+score);
    }

    public int getScore() throws RemoteException {
        return this.score;
    }

    public int getRank() throws RemoteException {
        return this.rank;
    }

    public PitchInterface getPitch() throws RemoteException {
        return this.pitch;
    }
}
