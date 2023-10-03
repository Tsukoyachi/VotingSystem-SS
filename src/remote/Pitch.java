package remote;

import java.awt.*;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Pitch extends UnicastRemoteObject implements PitchInterface{
    private String type;
    private Object element;

    public Pitch(String type, String element) throws RemoteException {
        super();
        this.type = type;
        this.element = element;
    }

    public String getType() {
        return this.type;
    }

    public String getTextElement() {
        return (String) this.element;
    }

    public byte[] getVideoElement() throws IllegalAccessException, RemoteException {
        if(type.equals("text")) {
            throw new IllegalAccessException("The pitch isn't a video");
        }
        try {
            Path path = Paths.get((String) this.element);
            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RemoteException("Error downloading file", e);
        }
    }
}
