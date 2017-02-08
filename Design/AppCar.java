package com.example.myfirstapp;

/**
 * Created by gloupias on 8-2-2017.
 *
 * Gebruik deze klasse in de UI om de Pi aan te sturen
 */

public class AppCar {

    protected String piIp;

    public AppCar(String piIp) {
        this.piIp = piIp;
    }

    public void setSpeed(float velocity) {
        // Stuur controls naar Pi over UDP
    }

    public void setSteeringWheel(float angle) {
        // Stuur controls naar Pi over UDP
    }

    public void startVideoStream(int fps) {
        // Stuur request om videostream te starten naar Pi over UDP
    }

    public void stopVideoStream() {
        // Stuur request om videostream te stoppen naar Pi Over UDP
    }

}
