package com.cooldev.tomapan;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Rooms extends Activity {

    ListView lista;
    Intent intent;
    String site = "http://eragon.herokuapp.com/getrooms";
    public static String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        intent = getIntent();
        name = intent.getStringExtra("name");
        lista = (ListView) findViewById(R.id.Lista);
        new ATask().execute();
    }

    public class ATask extends AsyncTask<String, Void, String> {
        String rez = "";
        @Override
        protected String doInBackground(String... urls) {
            //try {
            try {
                Log.e("rasp", site);
                URL obj = new URL(site);
                try {
                    Log.e("rasp", obj.toString());
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    //con.setRequestProperty("User-Agent", USER_AGENT);
                    // For POST only - START
                    con.setDoOutput(true);
                    // For POST only - END
                    int responseCode = con.getResponseCode();
                    Log.e("rasp", "response code-ul e " + Integer.toString(responseCode));
                    if (responseCode == HttpURLConnection.HTTP_OK) { //success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        // print result
                        rez = response.toString();
                    }
                    else {
                        Log.e("rasp", "POST request not worked");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                Log.e("naspa", "E corupt!");
            }
            //} catch (Exception e) {
            // Log.e("rasp", "aia e");
            //}
            return rez;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            String tot[] = rez.split("\\s+");
            //for(int i = 0; i < tot.length; i++)
                //Log.e("ceva", tot[i]);
            //Log.e("ceva", " ba ");
            int nr_camere = Integer.parseInt(tot[0]);
            int contor = 0;
            List<String> toate = new ArrayList<String>();

            for(int i = 0; i < nr_camere; i++)
            {
                String camera = "";
                for(int j = 0; j < 6; j++)
                    camera += tot[++contor] + " ";
                toate.add(camera);
            }
            String [] toti = toate.toArray(new String[toate.size()]);
            for(int i = 0; i < toti.length; i++)
                Log.e("ceva", toti[i]);
            Log.e("ceva", " b ");
            ListAdapter myAdapter = new CustomAdapter(Rooms.this, toti);
            ListView lista = (ListView)findViewById(R.id.Lista);
            lista.setAdapter(myAdapter);
        }
    }

    public void GetRooms(View view)
    {
        boolean se_poate = true;
        for(int i = 0; i < lista.getCount() && se_poate; i++)
        {
            View v = lista.getChildAt(i);
            Button bt = (Button)v.findViewById(R.id.Buton);
            Log.e("butono", "fac altceva!");
            Log.e("butono", bt.getText().toString());
            if(bt.getText().toString().equals("Iesi"))
            {
                se_poate = false;
                Toast.makeText(getApplicationContext(), "Trebuie mai intai sa iesi din camera!", Toast.LENGTH_LONG).show();
            }
        }
        if(se_poate)
        {
            new ATask().execute(site);
            Log.e("clark", "merge, ba!");
        }
    }
}
