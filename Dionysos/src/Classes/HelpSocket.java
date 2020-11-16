/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Thomas
 * This class represent an element of the HelpSocketVector (A person who agree to receive "I need help" notification
 *
 */
public class HelpSocket 
{
    private Socket client;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private int ID; //The ID of the client
    private int position; //The position of the client IRL. For now it will be an integer.
    
    public HelpSocket ()
    {
        this.client=null;
        this.ois=null;
        this.oos =null;
        this.ID = 0; //The ID of the client
        this.position = 0; //The position of the client IRL. For now it will be an integer.
    }
    
    public HelpSocket (Socket clientp, ObjectInputStream oisp, ObjectOutputStream oosp, int IDp, int positionp)
    {
        this.client=clientp;
        this.ois=oisp;
        this.oos =oosp;
        this.ID = IDp; //The ID of the client
        this.position = positionp; //The position of the client IRL. For now it will be an integer.
    }

    /**
     * @return the client
     */
    public Socket getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Socket client) {
        this.client = client;
    }

    /**
     * @return the ois
     */
    public ObjectInputStream getOis() {
        return ois;
    }

    /**
     * @param ois the ois to set
     */
    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    /**
     * @return the oos
     */
    public ObjectOutputStream getOos() {
        return oos;
    }

    /**
     * @param oos the oos to set
     */
    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }
}
