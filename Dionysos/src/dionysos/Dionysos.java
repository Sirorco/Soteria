/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dionysos;

import Classes.HelpSocket;
import Classes.HelpSocketVector;
import Protocol.base_request;
import Protocol.help_request;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas
 */
public class Dionysos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Properties propfile=new Properties();
        HelpSocketVector Helps = new HelpSocketVector(); //Contains all the sockets of the client who accept to repond to I_NEED_HELP request.
     
        try 
        {
            FileInputStream fip= new FileInputStream("config.properties");
            propfile.load(fip);
            
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(Dionysos.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        } catch (IOException ex) {
            Logger.getLogger(Dionysos.class.getName()).log(Level.SEVERE, null, ex);
        }

        String PortBase = propfile.getProperty("port_base", "50001");
        String PortHelp = propfile.getProperty("port_help", "50002");



        AcceptPoolThread ServerSP = new AcceptPoolThread(Integer.parseInt(PortBase), Helps);
        ServerSP.start();
        
        try {
            ServerSocket SocketHelp = new ServerSocket(Integer.parseInt(PortHelp));
            Socket CSocket;
            ObjectInputStream ois=null;
            ObjectOutputStream oos =null;
            help_request request;
            
            while (true)
            {
                //Waiting for a client to offer his help
                CSocket = SocketHelp.accept();
                System.out.println("=>Succes Socket Retrie");
                try
                {

                    oos = new ObjectOutputStream(CSocket.getOutputStream());
                    ois = new ObjectInputStream(CSocket.getInputStream());
                    request = (help_request)ois.readObject();
                    System.out.println("=> The user " + request.getId_person_to_help() + " is Ok to respond to I_NEED_HELP.");
                    
                    request.setStatus(true);
                    oos.writeObject(request);
                    oos.flush();
                    
                    HelpSocket Help = new HelpSocket (CSocket, ois, oos, request.getId_person_to_help(), request.getPosition());
                    Helps.AddSocket(Help);
                    
                }
                catch (IOException e)
                { System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); } 
                catch (ClassNotFoundException ex) {
                    Logger.getLogger(Dionysos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Dionysos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
