/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protocol;

/**
 *
 * @author Thomas
 */

//Used in 2 cases :
// - When a user accept to answer to I_NEED_HELP
// - When a user launch a I_NEED_HELP
// - When a user respond to COULD_YOU_HELP
public class help_request extends base_request
{
    //The ID of the person who need help_request
    private int id_person_to_help;
    //The position of the person who need
    private int position;

    /**
     * @return the id_person_to_help
     */
    public int getId_person_to_help() {
        return id_person_to_help;
    }

    /**
     * @param id_person_to_help the id_person_to_help to set
     */
    public void setId_person_to_help(int id_person_to_help) {
        this.id_person_to_help = id_person_to_help;
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
