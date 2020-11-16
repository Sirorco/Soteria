package com.example.soteria_v0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import Protocol.help_request;

import static Protocol.base_request.HELP_OFFER;
import static Protocol.base_request.I_CAN_HELP;
import static Protocol.base_request.I_NEED_HELP;
import static android.app.PendingIntent.getActivity;
import static com.example.soteria_v0.MainActivity.ID;
import static com.example.soteria_v0.MainActivity.IP_SERV;
import static com.example.soteria_v0.MainActivity.PORT_HELP;
import static com.example.soteria_v0.Menu.IDHelpless;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int person_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        person_position = getIntent().getIntExtra("person_position",999);

        Log.d (null,"Map activity",null);
        IWILLHELP tasc = new IWILLHELP();
        tasc.execute();

        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private class IWILLHELP extends AsyncTask<Void, Integer, Void>
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

            Log.d (null,"IWILLHELP thread",null);

            try
            {
                help_request request = new help_request();
                request.setStatus(true);
                request.setType_req(I_CAN_HELP);
                request.setId_person_to_help(IDHelpless);
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng liege = new LatLng(50.63, 5.57);
        mMap.addMarker(new MarkerOptions().position(liege).title("FakePerson postion "+person_position));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(liege,mMap.getMaxZoomLevel()));
    }
}