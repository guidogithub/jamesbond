package com.bond.controloverlay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }

    @Override
    public void onClick(View v) {
        int targetId = v.getId(); // find out which id (button) has triggerd the event
        if (targetId == R.id.moveForward) {
            Toast.makeText(getApplicationContext(), "The Forward button is pressed", Toast.LENGTH_SHORT).show();

        } else if (targetId == R.id.moveBackward) {
            Toast.makeText(getApplicationContext(), "The Backward button is pressed", Toast.LENGTH_SHORT).show();
        } else if (targetId == R.id.moveLeft) {
            Toast.makeText(getApplicationContext(), "The Left button is pressed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "The Right button is pressed", Toast.LENGTH_SHORT).show();
        }

    }
}

