import remote.VotingSystem;
import remote.VotingSystemInterface;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) throws IOException {
        Registry registry = LocateRegistry.createRegistry(1099);
        VotingSystemInterface votingSystem = new VotingSystem();
        try {
            registry.bind("votingSystem",votingSystem);
        } catch (AlreadyBoundException e) {
            System.out.println("Error with the binding of the voting system in the registry");
        }
        System.out.println("Server side is launched !");
    }
}
