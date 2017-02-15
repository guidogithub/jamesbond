package com.bond.controloverlay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ResourceBundle;


public class ControlActivity extends AppCompatActivity implements View.OnClickListener {
    Boolean[] Control_matrix = new Boolean[4];
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
}

