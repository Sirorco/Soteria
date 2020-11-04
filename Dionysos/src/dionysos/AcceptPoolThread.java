/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dionysos;

import Classes.HelpSocketVector;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas
 */
public class AcceptPoolThread extends Thread
{
    private int Port;
    private Thread Job;
    private ServerSocket SSocket;
    private HelpSocketVector Helps;
    
    public AcceptPoolThread (int pPort, HelpSocketVector pHelps)
    {
        //Job = jJob;
        try {
            SSocket = new ServerSocket(pPort);
        } catch (IOException ex) {
            Logger.getLogger(AcceptPoolThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Helps = pHelps;
    }
    
    public void run()
    {
        Socket CSocket;
        
        
        while (!isInterrupted())
        {
            try 
            {
                System.out.println("************ Serveur en attente");
                CSocket = SSocket.accept();
                System.out.println("Nouveau client");
                
                Thread_Mercure  thread = new Thread_Mercure(CSocket, Helps);
                thread.start();
                
            } catch (IOException ex) {
                Logger.getLogger(AcceptPoolThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
}
