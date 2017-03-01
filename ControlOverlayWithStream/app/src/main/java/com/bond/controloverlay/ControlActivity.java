package com.bond.controloverlay;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.URI;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener {
    Boolean[] Control_matrix = new Boolean[4];
    private static final boolean DEBUG = false;
    private static final String TAG = "MJPEG";

    private MjpegView mv = null;
    String URL;

    // for settings (network and resolution)
    private static final int REQUEST_SETTINGS = 0;

    private int width = 640;
    private int height = 480;

    private int ip_ad1 = 192;
    private int ip_ad2 = 168;
    private int ip_ad3 = 2;
    private int ip_ad4 = 1;
    private int ip_port = 80;
    private String ip_command = "?action=stream";

    private boolean suspending = false;

    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        // For printing the array
       //int arraySize = myArray.size();
        //for(int i = 0; i < arraySize; i++) {
        //    myTextView.append(myArray[i]);
        //}
        //  end for printing the array

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);


        // create new reference to buttons
        Button Forward = (Button) findViewById(R.id.moveForward);
        Button Backward = (Button) findViewById(R.id.moveBackward);
        Button Left = (Button) findViewById(R.id.moveLeft);
        Button Right = (Button) findViewById(R.id.moveRight);

        // place on click listeners on the buttons
        Forward.setOnClickListener(this);
        Backward.setOnClickListener(this);
        Left.setOnClickListener(this);
        Right.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences("SAVED_VALUES", MODE_PRIVATE);
        width = preferences.getInt("width", width);
        height = preferences.getInt("height", height);
        ip_ad1 = preferences.getInt("ip_ad1", ip_ad1);
        ip_ad2 = preferences.getInt("ip_ad2", ip_ad2);
        ip_ad3 = preferences.getInt("ip_ad3", ip_ad3);
        ip_ad4 = preferences.getInt("ip_ad4", ip_ad4);
        ip_port = preferences.getInt("ip_port", ip_port);
        ip_command = preferences.getString("ip_command", ip_command);

        StringBuilder sb = new StringBuilder();
        String s_http = "http://";
        String s_dot = ".";
        String s_colon = ":";
        String s_slash = "/";
        sb.append(s_http);
        sb.append(ip_ad1);
        sb.append(s_dot);
        sb.append(ip_ad2);
        sb.append(s_dot);
        sb.append(ip_ad3);
        sb.append(s_dot);
        sb.append(ip_ad4);
        sb.append(s_colon);
        sb.append(ip_port);
        sb.append(s_slash);
        sb.append(ip_command);
        URL = new String(sb);

        mv = (MjpegView) findViewById(R.id.mv);
        if (mv != null) {
            mv.setResolution(width, height);
        }

        setTitle(R.string.title_connecting);
        new DoRead().execute(URL);
    }

    @Override
    public void onClick(View v) {

        TextView booleanTextView;
        booleanTextView = (TextView)findViewById(R.id.textView1);
        int targetId = v.getId(); // find out which id (button) has triggerd the event
            if (targetId == R.id.moveForward) {
                //Toast.makeText(getApplicationContext(), "The Forward button is pressed", Toast.LENGTH_SHORT).show();
                Control_matrix[0]=true; // set boolean to true
            } else if (targetId == R.id.moveBackward) {
                //Toast.makeText(getApplicationContext(), "The Backward button is pressed", Toast.LENGTH_SHORT).show();
                Control_matrix[1]=true; // set boolean to true
            } else if (targetId == R.id.moveLeft) {
                //Toast.makeText(getApplicationContext(), "The Left button is pressed", Toast.LENGTH_SHORT).show();
                Control_matrix[2]=true; // set boolean to true
            } else {
               // Toast.makeText(getApplicationContext(), "The Right button is pressed", Toast.LENGTH_SHORT).show();
                Control_matrix[3]=true; // set boolean to true
            }

        // the code below toasts the matrix after each click
//        Printing integer array list values on screen.
//         note, array size is fixed (4)
        StringBuilder builder = new StringBuilder();
        for(int i=0; i < 4; i++)
        {
            builder.append("" + Control_matrix[i] + " ");
        }
        Toast.makeText(this, builder, Toast.LENGTH_LONG).show();


        //Printing integer array list values on screen.
         // note, array size is fixed (4)
//       for(int i=0; i < 4; i++) {
//
//           booleanTextView.setText(booleanTextView.getText() + " " + Control_matrix[i] + " , ");
//       }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settings_intent = new Intent(ControlActivity.this, SettingsActivity.class);
                settings_intent.putExtra("width", width);
                settings_intent.putExtra("height", height);
                settings_intent.putExtra("ip_ad1", ip_ad1);
                settings_intent.putExtra("ip_ad2", ip_ad2);
                settings_intent.putExtra("ip_ad3", ip_ad3);
                settings_intent.putExtra("ip_ad4", ip_ad4);
                settings_intent.putExtra("ip_port", ip_port);
                settings_intent.putExtra("ip_command", ip_command);
                startActivityForResult(settings_intent, REQUEST_SETTINGS);
                return true;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SETTINGS:
                if (resultCode == Activity.RESULT_OK) {
                    width = data.getIntExtra("width", width);
                    height = data.getIntExtra("height", height);
                    ip_ad1 = data.getIntExtra("ip_ad1", ip_ad1);
                    ip_ad2 = data.getIntExtra("ip_ad2", ip_ad2);
                    ip_ad3 = data.getIntExtra("ip_ad3", ip_ad3);
                    ip_ad4 = data.getIntExtra("ip_ad4", ip_ad4);
                    ip_port = data.getIntExtra("ip_port", ip_port);
                    ip_command = data.getStringExtra("ip_command");

                    if (mv != null) {
                        mv.setResolution(width, height);
                    }
                    SharedPreferences preferences = getSharedPreferences("SAVED_VALUES", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("width", width);
                    editor.putInt("height", height);
                    editor.putInt("ip_ad1", ip_ad1);
                    editor.putInt("ip_ad2", ip_ad2);
                    editor.putInt("ip_ad3", ip_ad3);
                    editor.putInt("ip_ad4", ip_ad4);
                    editor.putInt("ip_port", ip_port);
                    editor.putString("ip_command", ip_command);

                    editor.commit();

                    new RestartApp().execute();
                }
                break;
        }
    }

    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
//            TODO: if camera has authentication deal with it and don't just not work
            HttpResponse res = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpParams httpParams = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);
            if (DEBUG) Log.d(TAG, "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url[0])));
                if (DEBUG)
                    Log.d(TAG, "2. Request finished, status = " + res.getStatusLine().getStatusCode());
                if (res.getStatusLine().getStatusCode() == 401) {
                    //You must turn off camera User Access Control before this will work
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());
            } catch (ClientProtocolException e) {
                if (DEBUG) {
                    e.printStackTrace();
                    Log.d(TAG, "Request failed-ClientProtocolException", e);
                }
                //Error connecting to camera
            } catch (IOException e) {
                if (DEBUG) {
                    e.printStackTrace();
                    Log.d(TAG, "Request failed-IOException", e);
                }
                //Error connecting to camera
            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            if (result != null) {
                result.setSkip(1);
                setTitle(R.string.app_name);
            } else {
                setTitle(R.string.title_disconnected);
            }
            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv.showFps(false);
        }
    }

    public class RestartApp extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... v) {
            ControlActivity.this.finish();
            return null;
        }

        protected void onPostExecute(Void v) {
            startActivity((new Intent(ControlActivity.this, ControlActivity.class)));
        }
    }
}

