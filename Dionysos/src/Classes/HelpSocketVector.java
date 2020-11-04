/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Protocol.base_request;
import Protocol.help_request;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas
 */
public class HelpSocketVector 
{
    private Vector <HelpSocket> ElVector;
    
    public HelpSocketVector ()
    {
        ElVector = new Vector <HelpSocket> ();
    }
    
    public synchronized boolean AddSocket (HelpSocket socket)
    {
        ElVector.add(socket);
        return true;
    }
    
    public synchronized boolean RemoveSocket (int id)
    {
        int cpt = 0;
        while (ElVector.size()>cpt)
        {
            if (ElVector.elementAt(cpt).getID()==id)
            {
                ElVector.remove(cpt);
                return true;
            }
        }
        
        return false;
    }
    
    public synchronized boolean LookForHelp (int id, int Position)
    {
        boolean someoneisnear = false;
        int cpt = 0;
        while (ElVector.size()>cpt)
        {
            int distance = ElVector.elementAt(cpt).getPosition() - Position;
            if (Math.abs(distance)<5)   //For now we assume that more than 4 in distance is too much
            {
                help_request request = new help_request ();
                request.setStatus(true);
                request.setType_req(base_request.COULD_YOU_HELP);
                request.setId_person_to_help(id);
                
                
                try {
                    ElVector.elementAt(cpt).getOos().writeObject(request);
                    ElVector.elementAt(cpt).getOos().flush();
                } catch (IOException ex) {
                    Logger.getLogger(HelpSocketVector.class.getName()).log(Level.SEVERE, null, ex);
                }
                someoneisnear = true;
            }
        }
        
        if (someoneisnear==true)
        {
            return true;
        }
        else
        {
            //Bad luck, time to run :)
            return false;
        }
    }
}
