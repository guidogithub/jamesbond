package com.bond.controloverlay;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import static java.net.InetAddress.getByName;

public class ControlActivity extends AppCompatActivity implements View.OnTouchListener {
    int[] Control_matrix;
    private static final boolean DEBUG = false;
    private static final String TAG = "MJPEG";

    private MjpegView mv = null;
    String URL;
    String str;

    // for settings (network and resolution)
    private static final int REQUEST_SETTINGS = 0;

    private int width = 512;
    private int height = 288;

    private int ip_ad1 = 192;
    private int ip_ad2 = 168;
    private int ip_ad3 = 1;
    private int ip_ad4 = 3;
    private int ip_port = 80;
    private String ip_command = "html/cam_pic_new.php?pDelay=40000";

    private Timer messageTimer;

    static {
        System.loadLibrary("ImageProc");
    }

    public String getIP() {
        SharedPreferences preferences = getSharedPreferences("SAVED_VALUES", MODE_PRIVATE);
        ip_ad1 = preferences.getInt("ip_ad1", ip_ad1);
        ip_ad2 = preferences.getInt("ip_ad2", ip_ad2);
        ip_ad3 = preferences.getInt("ip_ad3", ip_ad3);
        ip_ad4 = preferences.getInt("ip_ad4", ip_ad4);

        StringBuilder sb = new StringBuilder();
        String s_dot = ".";
        sb.append(ip_ad1)
                .append(s_dot)
                .append(ip_ad2)
                .append(s_dot)
                .append(ip_ad3)
                .append(s_dot)
                .append(ip_ad4);
        return new String(sb);
    }

    private void startSendingMessages(int interval) {
        messageTimer = new Timer();
        messageTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new SendMessageTask(getIP()).execute(str);
            }
        },0,interval);
    }

    private void stopSendingMessages() {
        if(messageTimer != null)
            messageTimer.cancel();
    }

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

        Control_matrix = new int[4];
        Control_matrix[0] = 0;
        Control_matrix[1] = 0;
        Control_matrix[2] = 0;
        Control_matrix[3] = 0;
        str = "f00a0r00b0t";
        final char[] strChars = str.toCharArray();

        // place on click listeners on the buttons
        Forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // TODO Auto-generated method stub
                if (event.getAction()== MotionEvent.ACTION_DOWN) {
                    Control_matrix[0] = 1;
                    strChars[1] = '5';
                    strChars[4] = '1';
                    str = String.valueOf(strChars);
                    startSendingMessages(75);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Control_matrix[0] = 0;
                    strChars[1] = '0';
                    strChars[4] = '1';
                    str = String.valueOf(strChars);
                    stopSendingMessages();
                    return true;
                }

                return false;

            }
        });

        Backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // TODO Auto-generated method stub
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Control_matrix[1] = 1;
                    strChars[1] = '5';
                    strChars[4] = '0';
                    str = String.valueOf(strChars);
                    startSendingMessages(75);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    Control_matrix[1] = 0;
                    strChars[1] = '0';
                    strChars[4] = '0';
                    str = String.valueOf(strChars);
                    stopSendingMessages();
                    return true;
                }
                return false;

            }
        });

        Left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // TODO Auto-generated method stub
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Control_matrix[2] = 1;
                    strChars[6] = '5';
                    strChars[9] = '1';
                    str = String.valueOf(strChars);
                    startSendingMessages(50);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    Control_matrix[2] = 0;
                    strChars[6] = '0';
                    strChars[9] = '1';
                    str = String.valueOf(strChars);
                    stopSendingMessages();
                    return true;
                }
                return false;

            }
        });

        Right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // TODO Auto-generated method stub
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Control_matrix[3] = 1;
                    strChars[6] = '5';
                    strChars[9] = '0';
                    str = String.valueOf(strChars);
                    startSendingMessages(50);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    Control_matrix[3] = 0;
                    strChars[6] = '0';
                    strChars[9] = '0';
                    str = String.valueOf(strChars);
                    stopSendingMessages();
                    return true;
                }
                return false;

            }
        });

        SharedPreferences preferences = getSharedPreferences("SAVED_VALUES", MODE_PRIVATE);
        width = preferences.getInt("width", width);
        height = preferences.getInt("height", height);
        ip_port = preferences.getInt("ip_port", ip_port);
        ip_command = preferences.getString("ip_command", ip_command);

        StringBuilder sb = new StringBuilder();
        String s_http = "http://";
        String s_colon = ":";
        String s_slash = "/";
        sb.append(s_http);
        sb.append(this.getIP());
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
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
    public class SendMessageTask extends AsyncTask<String, Void, Void> {

        private String ipString;

        public SendMessageTask(String ipAddress) {
            super();
            this.ipString = ipAddress;
        }

        @Override
//
        public Void doInBackground(String... str) {

            DatagramSocket client_socket = null;

            int server_port = 8010;

            try {
                client_socket = new DatagramSocket(server_port);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            InetAddress IPAddress = null;
            try {
                IPAddress = getByName(this.ipString);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            //while (true)
            // {

            byte[] send_data = str[0].getBytes();
            //System.out.println("Type Something (q or Q to quit): ");

            DatagramPacket send_packet = new DatagramPacket(send_data, str[0].length(), IPAddress, server_port);

            try {
                assert client_socket != null;
                Log.i(str[0],"start of send");
                client_socket.send(send_packet);
                Log.i(send_data.toString(),"end of send");
            } catch (IOException e) {
                e.printStackTrace();
            }


            client_socket.close();

            // }
            return null;
        }
    }
}

