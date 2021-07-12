package com.example.zshop.services;

public class ThreadDemo extends Thread{
    private Thread t;
    private String threadName;

    ThreadDemo(String name) {
        threadName = name;
    }

    @Override
    public void run() {
        System.out.println("Running " + threadName);
        try {
            for (int i = 4; i > 0; i--) {
                System.out.println("Thread: " + threadName + ", " + i);
                // Let the thread sleep for a while.
                Thread.sleep(50);
            }
        } catch (Exception e) {
            System.out.println("Thread " + threadName + " interrupted.");
        }
        System.out.println("Thread " + threadName + " exiting.");
    }
}
