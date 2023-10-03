package remote;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;

public class CandidateVideo extends Candidate{
    String pitchPath;
    public CandidateVideo(String firstName, String lastName, int rank, String pitchPath) throws RemoteException {
        super(firstName, lastName, rank);
        this.pitchPath = pitchPath;
    }
    public byte[] downloadPitch() throws RemoteException {
        try {
            Path path = Paths.get(pitchPath);
            return Files.readAllBytes(path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error downloading file", e);
        }
    }
}
