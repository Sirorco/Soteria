/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protocol;

import java.io.Serializable;

/**
 *
 * @author Toluay
 * T&T industries 2015-2018
 */

public class  base_request implements Serializable, Cloneable
{

    
    
    //sorte de define en java , pas trouvé mieux 
    public static final int LOGIN = 1;                  //Used for the client login
    public static final int HELP_OFFER = 2;             //Used by the client in order to accept or refused giving help
    public static final int I_NEED_HELP = 3;            //A client need help
    public static final int COULD_YOU_HELP = 4;         //Ask if someone could help
    public static final int I_CAN_HELP = 5;             //When help requesti is accepted
    public static final int LOGOUT = 10;                //Used for the client logout




    //LES VARAIBLE DE LA REQUETE 
    private int Type_req;
    private boolean Status;
    private String Error;



    public Object clone() 
    {
       Object o = null;
       try {
               // On récupère l'instance à renvoyer par l'appel de la 
               // méthode super.clone()
               o = super.clone();
       } catch(CloneNotSupportedException cnse) {
               // Ne devrait jamais arriver car nous implémentons 
               // l'interface Cloneable
               cnse.printStackTrace(System.err);
           }
           // on renvoie le clone
           return o;
       }

    /**
     * @return the Type_req
     */
    public int getType_req() {
        return Type_req;
    }

    /**
     * @param Type_req the Type_req to set
     */
    public void setType_req(int Type_req) {
        this.Type_req = Type_req;
    }

    /**
     * @return the Status
     */
    public boolean getStatus() {
        return Status;
    }

    /**
     * @param Status the Status to set
     */
    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    /**
     * @return the Error
     */
    public String getError() {
        return Error;
    }

    /**
     * @param Error the Error to set
     */
    public void setError(String Error) {
        this.Error = Error;
    }
    
}
