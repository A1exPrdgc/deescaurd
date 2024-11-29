package deescaurd;
import java.util.ArrayList;

import deescaurd.ihm.connexion.FrameJoin;
import deescaurd.reseau.ClientChat;

public class Controleur 
{
    private FrameJoin ihm;
    private String pseudo;
    private String hostname;
    private int port;

    private ArrayList<Integer> messageRct;

    public Controleur()
    {
        this.ihm = new FrameJoin(this);
    }

    public Exception joinChannel()
    {
        try {
            new ClientChat(this.hostname, this.port, this);
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        new Controleur();
    }

    

}
