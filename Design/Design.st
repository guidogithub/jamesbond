"Situatie:

App schrijven

- Requirements
MUSTS
Android app kan auto accelereren
Android app kan auto decelereren
Android app kan auto naar links sturen
Android app kan auto naar rechts sturen
Android app kan zicht vanuit perspectief van de auto zien

SHOULD
Android app kan auto naar achteren laten rijden
Smooth accelereren en decelereren"

class AppUI [
  
  new: car []

"
Zet input van UI elementen om in commando's voor de AppCar module
"

].


class AppCar [

"
Onthoud IP adres van PiCar.
"
  new: ipAddress [].

"
Stuur controls naar PiCar over UDP.
"
  setSpeed: speed [].
  setSteeringWheel: angle [].

"
Stuur verzoek om video te starten over UDP.
Open browser window naar HTTP video stream.
"
  startVideoStream: fps [].

"
Stuur verzoek om video te stoppen over UDP.
"
  stopVideoStream []

].


class PiCar [

"
Start UDP control server (listen on UDP port).
"
  new [].

"
Listen these commands via UDP.
Translate naar low-level controls via serial.
"
  setSpeed: speed [].
  setSteeringWheel: angle [].

"
Listen these commands via UDP.

Met Raspberry Pi camera:
https://raspberrypi.stackexchange.com/questions/23182/how-to-stream-video-from-raspberry-pi-camera-and-watch-it-live/32521#32521
HTTP SERVER
"
  startVideoStream: fps [].
  stopVideoStream []

].
