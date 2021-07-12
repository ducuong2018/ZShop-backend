package com.example.zshop.services;

public class TestThread  extends  Thread{
    public static void main(String args[]){

    ThreadDemo t1 = new ThreadDemo("Thread-1-HR-Database");
    t1.start();
        try {
            t1.join(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ThreadDemo t2 = new ThreadDemo("Cuong Dep trai");
    t2.start();
    }
}
