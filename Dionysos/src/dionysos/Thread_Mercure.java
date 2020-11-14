/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dionysos;

import Classes.HelpSocket;
import Classes.HelpSocketVector;
import Protocol.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.StandardOpenOption;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


/**
 *
 * @author Thomas
 */
public class Thread_Mercure extends Thread
{
    private Socket Client;
    private HelpSocketVector Helps;
    
    public Thread_Mercure (Socket cClient, HelpSocketVector pHelps)
    {
        Client = cClient;
        Helps = pHelps;
    }
    
    public void run()
    {
        ObjectInputStream ois=null;
        ObjectOutputStream oos =null;
        base_request request;
        
        //The ID of the user is given by the server.
        //This ID will be use in order to find the "Help" socket of the client.
        int id= 669;
        

        
        System.out.println("-->Succes Socket Retrie");
        try
        {

            oos = new ObjectOutputStream(Client.getOutputStream());
            ois = new ObjectInputStream(Client.getInputStream());
            System.out.println("Streams open !");

        }
        catch (IOException e)
        { System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); }
        
        
        
                        
        try
        {
            while (!isInterrupted())
            {
                request = null;
                request = (base_request)ois.readObject();
                System.out.println("Le thread : " + Thread.currentThread().getId() + " a recu un msg");

                switch(request.getType_req())
                {
                    case base_request.LOGIN :

                        System.out.println("LOGIN");
                        login_request logreq = (login_request) request;

                        //It's bad but it's good for now !
                        if (logreq.getLogin().equals("Thomas") || logreq.getLogin().equals("Camille"))
                        {
                            if (logreq.getPassword().equals("Spiga") || logreq.getPassword().equals("Xei"))
                            {
                                logreq.setStatus(true);
                                logreq.setError("Welcome !");
                                
                                if (logreq.getLogin().equals("Thomas"))
                                {
                                    logreq.setId(1997);
                                    id = 1997;
                                }
                                
                                if (logreq.getLogin().equals("Camille"))
                                {
                                    logreq.setId(1998);
                                    id = 1998;
                                }
                            }
                            else
                            {
                                logreq.setStatus(false);
                                logreq.setError("Nad password !");
                            }
                        }
                        else
                        {
                            logreq.setStatus(false);
                            logreq.setError("Unknown user !");
                        }
                        oos.writeObject(logreq);
                        oos.flush();
                        break;
                        
                    //The user refused to respond to I_NEED_HELP.
                    case base_request.HELP_OFFER :
                        System.out.println("HELP_OFFER");
                        
                        help_request requesthel = (help_request) request;
                        Helps.RemoveSocket(requesthel.getId_person_to_help());
                        //We remove the socket of the user from le list of socket who want to help.
                        
                        requesthel.setStatus(true);
                        oos.writeObject(requesthel);
                        oos.flush();
                        break;

                    case base_request.I_NEED_HELP:
                        System.out.println("I_NEED_HELP");
                        
                        help_request requesthelp = (help_request) request;
                        boolean status = Helps.LookForHelp(requesthelp.getId_person_to_help(), requesthelp.getPosition());
                        requesthelp.setStatus(status);
                        oos.writeObject(requesthelp);
                        oos.flush();
                        break;
                        
                    case base_request.LOGOUT :
                        System.out.println("LOGOUT");
                        request.setStatus(true);
                        oos.writeObject(request);
                        oos.flush();
                        break;
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Thread_Mercure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Thread_Mercure.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
        System.out.println("The end");
    }
    
}
