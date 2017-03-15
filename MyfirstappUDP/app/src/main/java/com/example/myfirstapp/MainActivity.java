package com.example.myfirstapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static android.provider.Telephony.Carriers.PORT;
import static java.net.InetAddress.getByName;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String host = null;
    private int port;
    String str = null;
    byte[] send_data = new byte[1024];

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);

        new SendMessageTask().execute(message);


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class SendMessageTask extends AsyncTask<String, Void, Void> {

        @Override
//        protected Void doInBackground(String... arg0) {
//
//            byte [] ip_bytes = new byte[] {(byte)131,(byte)155,(byte)227,(byte)239};
//
//            try {
//                inet_addr = InetAddress.getByAddress(ip_bytes );
//            } catch (UnknownHostException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            byte[] buffer = arg0[0].getBytes();
//
//            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,inet_addr, PORT);
//            try {
//                socket= new DatagramSocket();
//                socket.send(packet);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            return null;
//        }
        public Void doInBackground(String... str) {

            DatagramSocket client_socket = null;

            int server_port = 1234;

            try {
                client_socket = new DatagramSocket(server_port);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            InetAddress IPAddress = null;
            try {
                IPAddress = getByName("131.155.227.239");
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
//
//    public void client() throws IOException {
//
//
//        DatagramSocket client_socket = new DatagramSocket(2362);
//        InetAddress IPAddress =  InetAddress.getByName("131.155.224.56");
//
//        //while (true)
//        // {
//        send_data = str.getBytes();
//        //System.out.println("Type Something (q or Q to quit): ");
//
//        DatagramPacket send_packet = new DatagramPacket(send_data,str.length(), IPAddress, 2362);
//        client_socket.send(send_packet);
//
//        client_socket.close();
//
//        // }
//
//    }
}