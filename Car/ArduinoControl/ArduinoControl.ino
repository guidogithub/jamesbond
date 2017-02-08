
//=========================HEADER=============================================================
#include "Arduino.h"
#include <SPI.h>
#include <stdio.h>

// ****************************************************
// Motor Controller Ports
// ****************************************************
#define pwm1 3
#define dir1 2

#define pwm2 5
#define dir2 9

#define pwm3 6
#define dir3 10

// ****************************************************
// Slave Select pins for encoders 1, 2 and 3
// ****************************************************
const int slaveSelectEnc1 = 8;
const int slaveSelectEnc2 = 4;
const int slaveSelectEnc3 = 7;


// signed long extended size variables for number storage, and store 32 bits (4 bytes), from -2,147,483,648 to 2,147,483,647
// These hold the current encoder count.
signed long encoder1count = 0;
signed long encoder2count = 0;
signed long encoder3count = 0;

// ****************************************************
// Control
// ****************************************************

// Testing time stuff Bas
int  qq = 1;
int ppp = 0;
unsigned long timeb_new = 0;
unsigned long timeb_old = 0;
unsigned long timer_abs = 0;
unsigned long time_save = 0;
unsigned long time_save_old =0;

double Test1;  double Test2;  double Test3;

double temp1;  double temp2;  double temp3;

const double  pi        = 3.14159265359;
const int     cts_p_r   = 540;
const double  T         = 0.020395828;  // Sample time MIDLEVEL loop, deze is variabel (MIDlevel bij benadering op 50 HZ)

unsigned long time;
unsigned long time_old;
unsigned long ptime;

double Error1;      double Error2;      double Error3;
double P1;          double P2;          double P3;
double I1;          double I2;          double I3;
double D1;          double D2;          double D3;
double U1;          double U2;          double U3;

double enc1_rad;    double enc2_rad;    double enc3_rad;
double enc1_rad_old;double enc2_rad_old;double enc3_rad_old;  // For integral action in PID control of motors

// Reference
double enc1_ref;        double enc2_ref;        double enc3_ref;
double bound = 0.02;
double delta_x_ref_w;   double delta_y_ref_w;   double delta_psi_ref_w; 

// Control gains
double Kp = 65;        double Kd = 0;          double Ki = 0;
double ScaleFactor = 1;

double err1;        double err2;      double err3;
double delta_th1;   double delta_th2; double delta_th3;
double pos_x;       double pos_y;     double psi;

double th1_ref_old = 0;
double th2_ref_old = 0;
double th3_ref_old = 0;

double delta_x_ref_r;
double delta_y_ref_r;
double delta_psi_ref_r;

// Communicate with raspberry pi
String end_package = "";
String x_val = "";  int x_val_i;
String signx = "";  int signx_i; 

String y_val="";    int y_val_i;
String signy = "";  int signy_i; 

String p_val="";    int p_val_i;
String signp = "";  int signp_i; 

String n_val="";    int n_val_i;  

String Temp1;  String Temp2;  String Temp3;  String Temp4;
String Temp5;  String Temp6;  String Temp7;

// ****************************************************
// Motor Controllers
// ****************************************************
typedef struct {      // Jeroen: define a struct MotorValues which contains the integer
  // pulse and the boolean direction.
  int pulse;
  bool direction;
} MotorValues;

MotorValues motorA;
MotorValues motorB;
MotorValues motorC;

// ****************************************************
// Stops the motors
// ****************************************************
void allStop() {
  analogWrite(pwm1, 0);    // analogWrite( portnumber, value )
  analogWrite(pwm2, 0);
  analogWrite(pwm3, 0);
}

// ****************************************************
// Sets the PWM motor values
// ****************************************************
void commandMotors() {
 analogWrite(pwm1, motorA.pulse);   // the value of motorA.pulse should be [0,255] where 255 represents 100% duty cycle.
  analogWrite(pwm2, motorB.pulse);
  analogWrite(pwm3, motorC.pulse);
}

// ****************************************************
// Motion control
// ****************************************************
void PID_Control(double enc1_rad, double enc2_rad, double enc3_rad, double enc1_ref, double enc2_ref, double enc3_ref) { 

Error1 = enc1_ref;  // was enc1_ref - enc1_rad;
Error2 = enc2_ref;
Error3 = enc3_ref;

// MOTOR 1
  if (abs(Error1) > bound)    // 0.1 rad corresponds with 6 degrees
  { 
  // Windup - check
    //if (abs(Error1) < IntThresh){ // prevent integral 'windup'
    //Integral = Integral + Error; // accumulate the error integral
    //}
    //else {
    //Integral=0; // zero it if out of bounds
    //}
    P1 = Error1*Kp;                 // Proportional action
    //I = Integral*Ki;            // Integral action 
    D1 = (enc1_rad_old-enc1_rad)*Kd;       // Derivative action
    U1 = P1 + D1;            // Control action
  
    // Minus sign because positive for encoder is negative for motor
    U1 = -U1*ScaleFactor;       // Scale control action to be in the range 0-255
    
    // Determine direction
    if (U1 < 0){               
      digitalWrite(dir1, LOW);     // positive direction
      U1 = U1-25;
    }
    else {                  
      digitalWrite (dir1,HIGH);
      U1 = U1+29;
    }
  
    motorA.pulse = abs(U1);
    
    // Saturation 
    if (abs(U1)>255) 
    {   
    motorA.pulse = 255;
    enc1_rad_old = enc1_rad; // save current value for next time
    }
  }
  else
  {
  U1 = 0;
  motorA.pulse = 0;
  enc1_rad_old = enc1_rad; // save current value for next time
  } 

// MOTOR 2
  if (abs(Error2) > bound)    // 0.1 rad corresponds with 6 degrees
  { 
    P2 = Error2*Kp;                         // Proportional action
    D2 = (enc2_rad_old-enc2_rad)*Kd;        // Derivative action
    U2 = P2 + D2;                           // Control action
  
    // Minus sign because positive for encoder is negative for motor
    U2 = -U2*ScaleFactor;       // Scale control action to be in the range 0-255
    
    // Determine direction
    if (U2 < 0){               
      digitalWrite(dir2, LOW);     // positive direction
      U2 = U2-29;
    }
    else {                  
      digitalWrite (dir2,HIGH);
      U2 = U2+24;
    }
  
    motorB.pulse = abs(U2);
    
    // Saturation 
    if (abs(U2)>255) 
    {   
      motorB.pulse = 255;
      enc2_rad_old = enc2_rad; // save current value for next time
    }
  }
  else
  {
  U2 = 0;
  motorB.pulse = 0;
  enc2_rad_old = enc2_rad; // save current value for next time
  } 
  
  // MOTOR 3
  if (abs(Error3) > bound)    // 0.1 rad corresponds with 6 degrees
  { 
    P3 = Error3*Kp;                         // Proportional action
    D3 = (enc3_rad_old-enc3_rad)*Kd;        // Derivative action
    U3 = P3 + D3;                           // Control action
  
    // Minus sign because positive for encoder is negative for motor
    U3 = -U3*ScaleFactor;       // Scale control action to be in the range 0-255
    
    // Determine direction
    if (U3 < 0){               
      digitalWrite(dir3, LOW);     // positive direction
      U3 = U3-29;
    }
    else {                  
      digitalWrite (dir3,HIGH);
      U3 = U3+31;
    }
  
    motorC.pulse = abs(U3);
    
    // Saturation 
    if (abs(U3)>255) 
    {   
      motorC.pulse = 255;
      enc3_rad_old = enc3_rad; // save current value for next time
    }
  }
  else
  {
  U3 = 0;
  motorC.pulse = 0;
  enc3_rad_old = enc3_rad; // save current value for next time
  } 

  //Command to send all new motorpulses to motors
  commandMotors();

  //time = micros();
     
  //Serial.print(Error1); Serial.print(","); Serial.print(motorA.pulse); Serial.print(","); Serial.print(Error2); Serial.print(","); Serial.print(motorB.pulse); Serial.print(","); Serial.print(Error3); Serial.print(","); Serial.println(motorC.pulse);
} // end function

// ****************************************************
// Initial setup function, called once
// ****************************************************
void setup() {

  // Debug Serial
  //Serial.begin(115200);  // previous one
  Serial.begin(9600);      // as with the raspberry pi

  // Set motor controller communication pins as outputs
  pinMode(dir1, OUTPUT);
  //  pinMode(brkA, OUTPUT); // is this necessary?
  pinMode(dir2, OUTPUT);
  // pinMode(brkB, OUTPUT);
  pinMode(dir3, OUTPUT);
  // pinMode(brkC, OUTPUT);
  initEncoders();       Serial.println("Encoders Initialized...");
  clearEncoderCount();  Serial.println("Encoders Cleared...");

  // Command all motors to stop
  allStop();
}

void initEncoders() {

  // Set slave selects as outputs
  pinMode(slaveSelectEnc1, OUTPUT);
  pinMode(slaveSelectEnc2, OUTPUT);
  pinMode(slaveSelectEnc3, OUTPUT);

  // Raise select pins
  // Communication begins when you drop the individual select signsl
  digitalWrite(slaveSelectEnc1, HIGH);
  digitalWrite(slaveSelectEnc2, HIGH);
  digitalWrite(slaveSelectEnc3, HIGH);

  SPI.begin();

  // Initialize encoder 1
  //    Clock division factor: 0
  //    Negative index input
  //    free-running count mode
  //    x4 quatrature count mode (four counts per quadrature cycle)
  // NOTE: For more information on commands, see datasheet
  digitalWrite(slaveSelectEnc1, LOW);       // Begin SPI conversation
  SPI.transfer(0x88);                       // Write to MDR0
  SPI.transfer(0x03);                       // Configure to 4 byte mode
  digitalWrite(slaveSelectEnc1, HIGH);      // Terminate SPI conversation

  // Initialize encoder 2
  //    Clock division factor: 0
  //    Negative index input
  //    free-running count mode
  //    x4 quatrature count mode (four counts per quadrature cycle)
  // NOTE: For more information on commands, see datasheet
  digitalWrite(slaveSelectEnc2, LOW);       // Begin SPI conversation
  SPI.transfer(0x88);                       // Write to MDR0
  SPI.transfer(0x03);                       // Configure to 4 byte mode
  digitalWrite(slaveSelectEnc2, HIGH);      // Terminate SPI conversation

  // Initialize encoder 3
  //    Clock division factor: 0
  //    Negative index input
  //    free-running count mode
  //    x4 quatrature count mode (four counts per quadrature cycle)
  // NOTE: For more information on commands, see datasheet
  digitalWrite(slaveSelectEnc3, LOW);       // Begin SPI conversation
  SPI.transfer(0x88);                       // Write to MDR0
  SPI.transfer(0x03);                       // Configure to 4 byte mode
  digitalWrite(slaveSelectEnc3, HIGH);      // Terminate SPI conversation
}

long readEncoder(int encoder) {

  // Initialize temporary variables for SPI read
  unsigned int count_1, count_2, count_3, count_4;
  long count_value;

  // Read encoder 1
  if (encoder == 1) {
    digitalWrite(slaveSelectEnc1, LOW);     // Begin SPI conversation
    SPI.transfer(0x60);                     // Request count
    count_1 = SPI.transfer(0x00);           // Read highest order byte
    count_2 = SPI.transfer(0x00);
    count_3 = SPI.transfer(0x00);
    count_4 = SPI.transfer(0x00);           // Read lowest order byte
    digitalWrite(slaveSelectEnc1, HIGH);    // Terminate SPI conversation
  }

  // Read encoder 2
  else if (encoder == 2) {
    digitalWrite(slaveSelectEnc2, LOW);     // Begin SPI conversation
    SPI.transfer(0x60);                      // Request count
    count_1 = SPI.transfer(0x00);           // Read highest order byte
    count_2 = SPI.transfer(0x00);
    count_3 = SPI.transfer(0x00);
    count_4 = SPI.transfer(0x00);           // Read lowest order byte
    digitalWrite(slaveSelectEnc2, HIGH);    // Terminate SPI conversation
  }

  // Read encoder 3
  else if (encoder == 3) {
    digitalWrite(slaveSelectEnc3, LOW);     // Begin SPI conversation
    SPI.transfer(0x60);                      // Request count
    count_1 = SPI.transfer(0x00);           // Read highest order byte
    count_2 = SPI.transfer(0x00);
    count_3 = SPI.transfer(0x00);
    count_4 = SPI.transfer(0x00);           // Read lowest order byte
    digitalWrite(slaveSelectEnc3, HIGH);    // Terminate SPI conversation
  }

  // Calculate encoder count
  count_value = (count_1 << 8) + count_2;
  count_value = (count_value << 8) + count_3;
  count_value = (count_value << 8) + count_4;

  return count_value;
}

// Function found by Bas to separte the incoming input string -> works fine
String getValue(String data, char separator, int index)
{
 int found = 0;
  int strIndex[] = {
0, -1  };
  int maxIndex = data.length()-1;
  for(int i=0; i<=maxIndex && found<=index; i++){
  if(data.charAt(i)==separator || i==maxIndex){
  found++;
  strIndex[0] = strIndex[1]+1;
  strIndex[1] = (i == maxIndex) ? i+1 : i;
  }
 }
  return found>index ? data.substring(strIndex[0], strIndex[1]) : "";
}


void clearEncoderCount() {

  // Set encoder1's data register to 0
  digitalWrite(slaveSelectEnc1, LOW);     // Begin SPI conversation
  // Write to DTR
  SPI.transfer(0x98);
  // Load data
  SPI.transfer(0x00);  // Highest order byte
  SPI.transfer(0x00);
  SPI.transfer(0x00);
  SPI.transfer(0x00);  // lowest order byte
  digitalWrite(slaveSelectEnc1, HIGH);    // Terminate SPI conversation

  delayMicroseconds(100);  // provides some breathing room between SPI conversations

  // Set encoder1's current data register to center
  digitalWrite(slaveSelectEnc1, LOW);     // Begin SPI conversation
  SPI.transfer(0xE0);
  digitalWrite(slaveSelectEnc1, HIGH);    // Terminate SPI conversation

  // Set encoder2's data register to 0
  digitalWrite(slaveSelectEnc2, LOW);     // Begin SPI conversation
  // Write to DTR
  SPI.transfer(0x98);
  // Load data
  SPI.transfer(0x00);  // Highest order byte
  SPI.transfer(0x00);
  SPI.transfer(0x00);
  SPI.transfer(0x00);  // lowest order byte
  digitalWrite(slaveSelectEnc2, HIGH);    // Terminate SPI conversation

  // Set encoder2's current data register to center  - THIS PART WAS MISSING IN SUPERDROIDS CODE
  digitalWrite(slaveSelectEnc2, LOW);     // Begin SPI conversation
  SPI.transfer(0xE0);
  digitalWrite(slaveSelectEnc2, HIGH);    // Terminate SPI conversation

  delayMicroseconds(100);  // provides some breathing room between SPI conversations

  // Set encoder3's current data register to center
  digitalWrite(slaveSelectEnc3, LOW);     // Begin SPI conversation
  SPI.transfer(0xE0);
  digitalWrite(slaveSelectEnc3, HIGH);    // Terminate SPI conversation

  // Set encoder3's data register to 0
  digitalWrite(slaveSelectEnc3, LOW);     // Begin SPI conversation
  // Write to DTR
  SPI.transfer(0x98);
  // Load data
  SPI.transfer(0x00);  // Highest order byte
  SPI.transfer(0x00);
  SPI.transfer(0x00);
  SPI.transfer(0x00);  // lowest order byte
  digitalWrite(slaveSelectEnc3, HIGH);    // Terminate SPI conversation

  delayMicroseconds(100);  // provides some breathing room between SPI conversations

  // Set encoder3's current data register to center
  digitalWrite(slaveSelectEnc3, LOW);     // Begin SPI conversation
  SPI.transfer(0xE0);
  digitalWrite(slaveSelectEnc3, HIGH);    // Terminate SPI conversation

}

// ****************************************************
// Main program loop
// ****************************************************

void loop() {

//for (int j = 1; j < 1001; j++)    TO CHECK FREQUENCY IN MIDLEVEL LOOP
//{
  // Communication protocol with Raspberry Pi
  if(Serial.available()>0) // if something in the buffer do the following (while loop ish)
    {
//////////////////////////////////////////////////////////////////////////////////////////////////////      
    if( Serial.read() == 'a' ) 
   {
      
      Temp1 = Serial.readStringUntil('t');      //to clear t in the buffer
      
      String x_val = getValue(Temp1,'d',0);    // Contains xstr 
      x_val_i = x_val.toInt();
      String Temp2 = getValue(Temp1,'d',1);    // Contains rest
      
      String signx = getValue(Temp2,'b',0);    // contains sign x
      signx_i = signx.toInt();
      String Temp3 = getValue(Temp2,'b',1);
      
      String y_val = getValue(Temp3,'e',0);    // contains ystr
      y_val_i = y_val.toInt();
      String Temp4 = getValue(Temp3,'e',1);
     
      String signy = getValue(Temp4,'c',0);    // contains sing y
      signy_i = signy.toInt();
      String Temp5 = getValue(Temp4,'c',1);
      
      String p_val = getValue(Temp5,'f',0);    // contains pstr
      p_val_i = p_val.toInt();
      String Temp6 = getValue(Temp5,'f',1);
      
      String signp = getValue(Temp6,'n',0);    // contains sign p
      signp_i = signp.toInt();
      String Temp7 = getValue(Temp6,'n',1);
      
      String nval  = getValue(Temp7,'t',0);    // contains value n
      n_val_i = n_val.toInt();
      
      
        if(signx_i == 0)  // correct for negative value
        {         
          x_val_i = -x_val_i;
        }  
      
        if(signy_i == 0)  // correct for negative value
        {
          y_val_i = -y_val_i;
        }  
        
        if(signp_i == 0)  // correct for negative value
        {
          p_val_i = -p_val_i;
        }  

 //if(qq ==1)
       //{
          //timeb_old = millis();    
       //}     
     
     //timeb_new = millis();

     //Serial.print(timeb_new-timeb_old);  Serial.print(" , ");  Serial.print(qq);   Serial.print(" , ");  Serial.print(y_val_i);  Serial.print(" , ");  Serial.println(signy_i);

     //timeb_old = timeb_new;
     //qq = qq+1;

  //Serial.print("x:"); Serial.print(x_val_i); Serial.print("  y:"); Serial.print(y_val_i); Serial.print("  psi:"); Serial.print(p_val_i); Serial.print("  n:"); Serial.println(n_val_i);  
  } 
      
          
     // Convert millimeter to meter, millirad to rad
    delta_x_ref_r   = (double)x_val_i/1000;   delta_y_ref_r   = (double)y_val_i/1000;   delta_psi_ref_r = (double)p_val_i/1000;
  
    // Error in delta robot coordinates
    err1 = delta_x_ref_r;   err2 = delta_y_ref_r;  err3 = delta_psi_ref_r;
       
    //Reference in delta wheel radians (based on Adr)
    
    // If reference contains feedback information, new desired reference not depending on old references
    if (n_val_i == 1)
    {
      //Serial.println("Ben 111");
      delta_th1 = -17.0477*err1 + 9.8425*err2  + 3.1634*err3 + enc1_rad;      th1_ref_old= delta_th1;   
      delta_th2 =  0*err1       - 19.6850*err2 + 3.1634*err3 + enc2_rad;      th2_ref_old= delta_th2;  
      delta_th3 =  17.0477*err1 + 9.8425*err2  + 3.1634*err3 + enc3_rad;      th3_ref_old= delta_th3;  
    }
    // If reference does not contain feedback information, new desired reference depending on old references
    if (n_val_i == 0)
    {
      //Serial.println("Ben 000");
      delta_th1 = -17.0477*err1 + 9.8425*err2  + 3.1634*err3 + th1_ref_old;      th1_ref_old= delta_th1;   
      delta_th2 =  0*err1       - 19.6850*err2 + 3.1634*err3 + th2_ref_old;      th2_ref_old= delta_th2;  
      delta_th3 =  17.0477*err1 + 9.8425*err2  + 3.1634*err3 + th3_ref_old;      th3_ref_old= delta_th3;  
    }
    
    //Serial.print("11:"); Serial.print( delta_th1); Serial.print(" 22:"); Serial.print( delta_th2); Serial.print("33:"); Serial.println( delta_th3);
      
  }  // closes if available statement
   
  enc1_ref = delta_th1 - enc1_rad; // absolute reference encoder rad
  enc2_ref = delta_th2 - enc2_rad;
  enc3_ref = delta_th3 - enc3_rad;
  
  //Serial.print("11:"); Serial.print( delta_th1); Serial.print(" 22:"); Serial.print( delta_th2); Serial.print("33:"); Serial.println( delta_th3);
  //Serial.print("w1:"); Serial.print( enc1_ref); Serial.print(" w2:"); Serial.print( enc2_ref); Serial.print(" w3:"); Serial.println( enc3_ref);
  //Serial.print("1:"); Serial.print( delta_th1); Serial.print(" 2:"); Serial.print( delta_th2); Serial.print("3:"); Serial.println( delta_th3);

     // FEEDBACK CONTOL - low level
      for (int i = 1; i < 11; i++) // factor 10 
      {
        //Retrieve current encoder counters
        encoder1count = readEncoder(1);   encoder2count = readEncoder(2);   encoder3count = readEncoder(3);
    
        enc1_rad = (double)encoder1count / cts_p_r * 2 * pi;
        enc2_rad = (double)encoder2count / cts_p_r * 2 * pi;
        enc3_rad = (double)encoder3count / cts_p_r * 2 * pi;
        
        PID_Control(enc1_rad, enc2_rad, enc3_rad, enc1_ref, enc2_ref, enc3_ref) ;     // input uML
        
        delayMicroseconds(1770);   // Frequency inner loop with delay of 770 is equal to 1kHz
                                   // Frequency inner loop with delay of 1770 is equal to 500 Hz (one run inside forloop takes 230 micro sec.)                           
      }
  // Read encoders after completing lowest level control
  encoder1count = readEncoder(1);   encoder2count = readEncoder(2);   encoder3count = readEncoder(3);
    
  enc1_rad = (double)encoder1count / cts_p_r * 2 * pi;
  enc2_rad = (double)encoder2count / cts_p_r * 2 * pi;
  enc3_rad = (double)encoder3count / cts_p_r * 2 * pi;
 
//}
//time      = micros();
//ptime     = time-time_old;
//time_old  = time;

//Serial.println(ptime);

}
