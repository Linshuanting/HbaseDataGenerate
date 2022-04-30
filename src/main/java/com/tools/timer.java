package com.tools;

public class timer {

    private long startTime = 0;
    private long stopTime = 0;

    public void startTimer(){
        startTime = System.currentTimeMillis();
    }

    public void stopTimer(){
        stopTime = System.currentTimeMillis();
    }

    public void printRunTime(){

        if (startTime >= stopTime) {
            System.out.println("ERROR: timer doesn't be used correctly");
        }

        System.out.println("The code run: " + (stopTime-startTime) +"ms");

    }

    public long getRunTime(){

        if (startTime >= stopTime) {
            System.out.println("ERROR: timer doesn't be used correctly");
        }

        return stopTime-startTime;
    }

}
