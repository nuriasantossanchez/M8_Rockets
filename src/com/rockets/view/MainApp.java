package com.rockets.view;

import com.rockets.application.Controller;

import java.util.concurrent.TimeUnit;

public class MainApp {

    private static Controller controller=Controller.getInstance();

    public static void main(String[] args) {

        new RocketsRice().show();

        try {
            new RiceForward(12).show();
            controller.execute();
            TimeUnit.MILLISECONDS.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            new RiceBack(7).show();
            TimeUnit.MILLISECONDS.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controller.getExec().shutdownNow();
    }


}


