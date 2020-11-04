package com.example.soteria_v0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import Protocol.base_request;
import Protocol.login_request;

public class MainActivity extends AppCompatActivity {

    static String IP_SERV = "192.168.1.8";
    static int PORT_BASE = 50001;
    static int PORT_HELP = 50002;
    static Socket cliSock = null;
    static ObjectInputStream ois;
    static ObjectOutputStream oos;
    static String User;
    static int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (cliSock==null) {
            ConnectToTheServer calcul = new ConnectToTheServer();
            calcul.execute();
        }
    }

    public void methode_button_login (View v)
    {
        String msg;
        DoLogin calcul=new DoLogin();
        calcul.execute();
    }

    public void display_result (String msg, Boolean status)
    {
        if (status==true)
        {
            Intent myIntent = new Intent(this, Menu.class);
            startActivity(myIntent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
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
                cliSock = new Socket(IP_SERV, PORT_BASE);
                Log.d (null,"Connected with success !",null);

                oos = new ObjectOutputStream(cliSock.getOutputStream());

                ois = new ObjectInputStream(cliSock.getInputStream());

                Log.d (null,"Connection succeed !",null);
            }
            catch (IOException ex) {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
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

    private class DoLogin extends AsyncTask<Void, Integer, Void>
    {
        String retrunmsg="NotInit";
        Boolean status = false;
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
            EditText login = (EditText)findViewById(R.id.TextBox_Login);
            EditText pwd = (EditText)findViewById(R.id.TextBox_Password);

            User = login.getText().toString();

            login_request request = new login_request();
            request.setType_req(base_request.LOGIN);
            request.setStatus(true);
            request.setLogin(login.getText().toString());
            request.setPassword(pwd.getText().toString());


            try
            {
                oos.writeObject(request);
                oos.flush();

                request = (login_request)ois.readObject();

                if(false == request.getStatus() )
                {
                    retrunmsg = request.getError();
                    Log.d (null,"11111111111111",null);

                }
                else
                {
                    status = true;
                    Log.d (null,"22222222222",null);
                    ID = request.getId();
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            display_result (retrunmsg, status);
        }
    }
}