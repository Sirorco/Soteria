package com.example.soteria_v0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import Protocol.help_request;
import Protocol.login_request;

import static Protocol.base_request.HELP_OFFER;
import static com.example.soteria_v0.MainActivity.ID;
import static com.example.soteria_v0.MainActivity.IP_SERV;
import static com.example.soteria_v0.MainActivity.PORT_BASE;
import static com.example.soteria_v0.MainActivity.PORT_HELP;

public class Menu extends AppCompatActivity {

    private Socket HelpRequest;
    ObjectInputStream Helois;
    ObjectOutputStream Heloos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Switch MySwitch = (Switch)findViewById(R.id.ICanHelpSwitch);
        MySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ConnectToTheServer thread = new ConnectToTheServer();
                    thread.execute();
                }else{
                    //code
                }
            }
        });
    }


    private class ConnectToTheServer extends AsyncTask<Void, Integer, Void>
    {
        /*@Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Début du traitement asynchrone",
                    Toast.LENGTH_LONG).show();
        }*/

        /*@Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            barre.setProgress(values[0]);
        }*/

        @Override
        protected Void doInBackground(Void... arg0)
        {

            Log.d (null,"Start of the web connection",null);

            try
            {
                //cliSock = new Socket("localhost", PORT_VOYAGEURS);
                HelpRequest = new Socket(IP_SERV, PORT_HELP);
                Log.d (null,"Connected with success !",null);

                Heloos = new ObjectOutputStream(HelpRequest.getOutputStream());

                Helois = new ObjectInputStream(HelpRequest.getInputStream());

                Log.d (null,"Connection succeed !",null);

                help_request request = new help_request();
                request.setStatus(true);
                request.setType_req(HELP_OFFER);
                request.setId_person_to_help(ID);
                request.setPosition(3);

                Heloos.writeObject(request);
                Heloos.flush();

                request = (help_request)Helois.readObject(); //Read the confirmation
                Log.d (null,"Request OK !",null);
                request = (help_request)Helois.readObject(); //Wait for someone askink for help
                //TODO react to the request
            }
            catch (IOException ex) {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        /*@Override
        protected void onPostExecute(Void result)
        {
            Toast.makeText(getApplicationContext(), "Traitement terminé !",
                    Toast.LENGTH_LONG).show();
        }*/
    }
}