package com.example.soteria_v0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import Protocol.help_request;

import static Protocol.base_request.HELP_OFFER;
import static Protocol.base_request.I_NEED_HELP;
import static com.example.soteria_v0.MainActivity.ID;
import static com.example.soteria_v0.MainActivity.IP_SERV;
import static com.example.soteria_v0.MainActivity.PORT_HELP;

public class Menu extends AppCompatActivity {

    private Socket HelpRequest;
    ObjectInputStream Helois;
    ObjectOutputStream Heloos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //create channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "needHelp";
            String description = "used when someone needs help";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(name.toString(),name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //Switch ICanHelp
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

        //Bouton INeedHelp
        Button help = findViewById(R.id.help_button);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendHelp helpNotification = new SendHelp();
                helpNotification.execute();
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

                while(true) {
                    request = (help_request) Helois.readObject(); //Wait for someone askink for help
                    //TODO react to the request
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Menu.this, "needHelp");
                    builder.setSmallIcon(R.drawable.help_icon);
                    builder.setContentTitle(request.getId_person_to_help()+" needs help");
                    builder.setContentText(request.getId_person_to_help()+" needs you at "+request.getPosition());
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setTimeoutAfter(90000); //la notification se retire après 1m30

                    //PendingIntent pour mettre des boutons ?

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Menu.this);
                    notificationManager.notify(1, builder.build());

                }
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

    private class SendHelp extends AsyncTask<Void, Integer, Void>
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

            Log.d (null,"Send help alert",null);

            try
            {
                help_request request = new help_request();
                request.setStatus(true);
                request.setType_req(I_NEED_HELP);
                request.setId_person_to_help(ID);
                request.setPosition(3);

                MainActivity.oos.writeObject(request);

                request = (help_request)MainActivity.ois.readObject(); //Read the confirmation
                if(request.getStatus())
                    Log.d (null,"Request OK !",null);
                else
                    Log.d (null,"You'd better run",null);

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