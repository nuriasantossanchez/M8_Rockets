package com.rockets.view;

import com.rockets.application.Controller;

import java.util.concurrent.TimeUnit;

public class MainApp {

    private static Controller controller=Controller.getInstance();

    public static void main(String[] args) {

        new RocketsRice();

        try {
            new RiceForward(15);
            controller.execute();
            TimeUnit.MILLISECONDS.sleep(28000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            new RiceBack(7);
            TimeUnit.MILLISECONDS.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controller.getExec().shutdownNow();
    }


}


