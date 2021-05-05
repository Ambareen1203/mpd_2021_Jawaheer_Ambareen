package org.me.gcu.equakestartercode;

//Student name: Ambareen Shabnam Jawaheer
//Student ID: S1903330

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends AppCompatActivity {
    //declaring variables
    ArrayList<EarthQuake> earthQuakeList = new ArrayList();
    private TextView rawDataDisplay;
    private Button startButton;
    private String result;
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check for network availability
        if (networkAvailable()){
            Log.e("MyTag","Device is online");
            startProgress();
        } else {
            Log.e("MyTag","Device is offline");
            // Enabling internet
            ((ProgressBar)findViewById(R.id.loadingIcon)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.errorText)).setVisibility(View.VISIBLE);
        }


    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    private boolean networkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }







    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {
            ArrayList<String[]> earthQuakeStrings = new ArrayList();
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {

                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                in.readLine();
                in.readLine();
                //
                // Throw away the first 2 header lines before parsing
                //
                //
                //
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", ae.toString());
            }

            XmlPullParserFactory factory = null;
            try {
                factory = XmlPullParserFactory.newInstance();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            factory.setNamespaceAware(false);
            XmlPullParser xpp = null;
            try {
                xpp = factory.newPullParser();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            try {
                System.out.println(result);
                xpp.setInput( new StringReader( result ) );
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            int eventType = 0;
            try {
                eventType = xpp.getEventType();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            Boolean isDescription = false;
            Boolean isItem = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("item")){
                        isItem = true;
                    }
                    if (xpp.getName().equals("description")){
                        isDescription = true;
                    }
                } else if(eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equals("item")){
                        isItem = false;
                    }
                    if (xpp.getName().equals("description")){
                        isDescription = false;
                    }
                } else if(eventType == XmlPullParser.TEXT) {
                    if (isDescription && isItem){
                        earthQuakeStrings.add(xpp.getText().split(";"));
                    }
                }

                try {
                    eventType = xpp.next();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("End document");

            for (int i=0;i<earthQuakeStrings.size();i++){
                earthQuakeList.add(new EarthQuake(earthQuakeStrings.get(i)));
            }

            //
            // Now that you have the xml data you can parse it
            //

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Intent ide = new Intent(MainActivity.this,Dashboard.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("earthquakeList",earthQuakeList);
                    ide.putExtras(bundle);
                    startActivity(ide);
                    finish();
                }
            });
        }

    }

}