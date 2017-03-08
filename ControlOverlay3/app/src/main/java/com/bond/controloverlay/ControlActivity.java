package com.bond.controloverlay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ControlActivity extends AppCompatActivity implements View.OnTouchListener {
    Boolean[] Control_matrix;

//    TextView booleanTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);


        // create new reference to buttons
        Button Forward = (Button) findViewById(R.id.moveForward);
        Button Backward = (Button) findViewById(R.id.moveBackward);
        Button Left = (Button) findViewById(R.id.moveLeft);
        Button Right = (Button) findViewById(R.id.moveRight);

//        // place on click listeners on the buttons
//        Forward.setOnTouchListener(this);
//        Backward.setOnTouchListener(this);
//        Left.setOnTouchListener(this);
//        Right.setOnTouchListener(this);

        Control_matrix = new Boolean[4];
        Control_matrix[0] = false;
        Control_matrix[1] = false;
        Control_matrix[2] = false;
        Control_matrix[3] = false;

        Forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // TODO Auto-generated method stub
                if (event.getAction()== MotionEvent.ACTION_DOWN) {
                    Control_matrix[0] = true;
//                    Toast.makeText(getApplicationContext(), "The Forward button is pressed", Toast.LENGTH_SHORT).show();
                    DispMatrix();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Control_matrix[0] = false;
//                    Toast.makeText(getApplicationContext(), "The Forward button is released", Toast.LENGTH_SHORT).show();
                  DispMatrix();
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
                    Control_matrix[1] = true;
//                    Toast.makeText(getApplicationContext(), "The Backward button is pressed", Toast.LENGTH_SHORT).show();
                    DispMatrix();
//                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    Control_matrix[1] = false;
//                    Toast.makeText(getApplicationContext(), "The Backward button is released", Toast.LENGTH_SHORT).show();
                    DispMatrix();
//                    return true;
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
                    Control_matrix[2] = true;
//                    Toast.makeText(getApplicationContext(), "The left button is pressed", Toast.LENGTH_SHORT).show();
                    DispMatrix();
                } else if (action == MotionEvent.ACTION_UP) {
                    Control_matrix[2] = false;
//                    Toast.makeText(getApplicationContext(), "The left button is released", Toast.LENGTH_SHORT).show();
                    DispMatrix();
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
                    Control_matrix[3] = true;
//                    Toast.makeText(getApplicationContext(), "The right button is pressed", Toast.LENGTH_SHORT).show();
                    DispMatrix();
                } else if (action == MotionEvent.ACTION_UP) {
                    Control_matrix[3] = false;
//                    Toast.makeText(getApplicationContext(), "The right button is released", Toast.LENGTH_SHORT).show();
                    DispMatrix();

                }
                return false;

            }
        });
    }

    private void DispMatrix(){
//        Printing integer array list values on screen. Note, array size is fixed (4)
        StringBuilder builder = new StringBuilder();
        for(int i=0; i < 4; i++)
        {
            builder.append("" + Control_matrix[i] + " ");
        }
        Toast.makeText(this, builder, Toast.LENGTH_LONG).show();
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_control);
//
//
//        // create new reference to buttons
//        Button Forward = (Button) findViewById(R.id.moveForward);
//        Button Backward = (Button) findViewById(R.id.moveBackward);
//        Button Left = (Button) findViewById(R.id.moveLeft);
//        Button Right = (Button) findViewById(R.id.moveRight);
//
//        // place on click listeners on the buttons
//        Forward.setOnClickListener(this);
//        Backward.setOnClickListener(this);
//        Left.setOnClickListener(this);
//        Right.setOnClickListener(this);
//    }
//
//
//
//    @Override
//    public void onClick(View v) {
//
//        TextView booleanTextView;
//        booleanTextView = (TextView)findViewById(R.id.textView1);
//        int targetId = v.getId(); // find out which id (button) has triggerd the event
//            if (targetId == R.id.moveForward) {
//                //Toast.makeText(getApplicationContext(), "The Forward button is pressed", Toast.LENGTH_SHORT).show();
//                Control_matrix[0]=true; // set boolean to true
//            } else if (targetId == R.id.moveBackward) {
//                //Toast.makeText(getApplicationContext(), "The Backward button is pressed", Toast.LENGTH_SHORT).show();
//                Control_matrix[1]=true; // set boolean to true
//            } else if (targetId == R.id.moveLeft) {
//                //Toast.makeText(getApplicationContext(), "The Left button is pressed", Toast.LENGTH_SHORT).show();
//                Control_matrix[2]=true; // set boolean to true
//            } else {
//               // Toast.makeText(getApplicationContext(), "The Right button is pressed", Toast.LENGTH_SHORT).show();
//                Control_matrix[3]=true; // set boolean to true
//            }
//
//
////        Printing integer array list values on screen.
////         note, array size is fixed (4)
////        StringBuilder builder = new StringBuilder();
////        for(int i=0; i < 4; i++)
////        {
////            builder.append("" + Control_matrix[i] + " ");
////        }
////        Toast.makeText(this, builder, Toast.LENGTH_LONG).show();
//        //Printing integer array list values on screen.
//         // note, array size is fixed (4)
//       for(int i=0; i < 4; i++) {
//
//           booleanTextView.setText(booleanTextView.getText() + " " + Control_matrix[i] + " , ");
//       }
//
//    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}

