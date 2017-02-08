# -*- coding: cp1252 -*-
import socket
import sys
import time
import timeit   # To be able to time stuff
import math
import serial   # From the PI to Arduino code

# To be able to control the frequency
from timeit import default_timer as timer

# Define serial port which corresponds to Arduino board
ser = serial.Serial('/dev/ttyACM0', 9600)

# Sending sock (to Bas laptop)
UDP_IP_Bas = "192.168.0.102"

# Receiving sock
UDP_IP = "192.168.0.197" 
UDP_PORT1 = 8010
sock2 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock2.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

sock2.bind((UDP_IP, UDP_PORT1))

################################################################################
# LOOP
################################################################################

while True:

        # Wait for start to receive
        data, addr = sock2.recvfrom(2048)

        # Correct
        Temp1 = data.split('x');        Temp2 = Temp1[1]
        Temp3 = Temp2.split('y');       Temp4 = Temp3[1]
        Temp5 = Temp4.split('p');       Temp6 = Temp5[1]
        Temp7 = Temp6.split('t');
                                         
        Uex_t   = Temp3[0];   Uex_t   = Uex_t.split('a');   Uex   = Uex_t[0];   Usx   = Uex_t[1]
        Uey_t   = Temp5[0];   Uey_t   = Uey_t.split('b');   Uey   = Uey_t[0];   Usy   = Uey_t[1]
        Uepsi_t = Temp7[0];   Uepsi_t = Uepsi_t.split('c'); Uepsi = Uepsi_t[0]; Uspsi = Uepsi_t[1]

        # Convert to integers
        Usx = int(Usx); Usy = int(Usy); Uspsi = int(Uspsi);

        # Correct for sign
        if Usx == 0:    Uex   = -Uex;
        if Usy == 0:    Uey   = -Uey;
        if Uspsi == 0:  Uepsi = -Uepsi;

        # Compute the actual control input
        Ux   = float(Uex)
        Uy   = float(Uey)
        Upsi = float(Uepsi)
        nval = "1"

        xstr = str(int(abs(round(Ux))));          xlength   = str(len(xstr)); 
        ystr = str(int(abs(round(Uy))));          ylength   = str(len(ystr));
        psistr = str(int(abs(round(Upsi))));      psilength = str(len(psistr));
                                      
        # Obtain sign of reference
        if math.copysign(1,Ux) == -1:   signxs = "0";
        else:   signxs = "1"

        if math.copysign(1,Uy) == -1:   signys = "0";
        else:   signys = "1"
                                
        if math.copysign(1,Upsi) == -1: signpsis = "0";
        else:   signpsis = "1"

        # Create string to send
        inp = "a" + xstr + "d" + signxs + "b" + ystr + "e" + signys + "c" + psistr + "f" + signpsis + "n" + nval + "t"
        ser.write(inp)        #send input
