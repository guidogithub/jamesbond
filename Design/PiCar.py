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

    if isSetSpeedCommand(data):
        processSetSpeedCommand(data)

    elif isSetSteeringWheelCommand(data):
        processSetSteeringWheelCommand(data)

    elif isStartVideoStreamCommand(data):
        processStartVideoStreamCommand(data)

    elif isStopVideoStreamCommand(data):
        processStopVideoStreamCommand(data)


def isSetSpeedCommand(data):
    # return True / False
    pass

def processSetSpeedCommand(data):
    # process control, stuur Arduino aan
    pass

def isSetSteeringWheelCommand(data):
    # return True / False
    pass

def processSetSteeringWheelCommand(data):
    # process control, stuur Arduino aan
    pass

def isStartVideoStreamCommand(data):
    # return True / False
    pass

def processStartVideoStreamCommand(data):
    # process control, stuur Arduino aan
    pass

def isStopVideoStreamCommand(data):
    # return True / False
    pass

def processStopVideoStreamCommand(data):
    # process control, stuur Arduino aan
    pass
