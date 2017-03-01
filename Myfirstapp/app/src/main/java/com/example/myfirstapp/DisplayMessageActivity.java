package com.example.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display_message);

            Intent intent = getIntent();
            String messageStr = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

            int server_port = 12345;
            DatagramSocket s = new DatagramSocket();
            InetAddress local = InetAddress.getByName("192.168.1.102");
            int msg_length = messageStr.length();
            byte[] message = messageStr.getBytes();
            DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
            s.send(p);

            TextView textView = new TextView(this);
            textView.setTextSize(40);
            textView.setText("Message sent");

            ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
            layout.addView(textView);
        } catch(Exception e) {
        }
    }
}