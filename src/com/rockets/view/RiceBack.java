package com.rockets.view;

import com.rockets.application.Controller;

public class RiceBack {

    private static Controller controller=Controller.getInstance();
    int powerBack;

    public RiceBack(int powerBack) {
        this.powerBack = powerBack;
        controller.back(powerBack);
    }

    public void show(){
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("FRENAR HASTA POTENCIA " + powerBack + " ....... ") ;
        System.out.println("--------------------------------------------------");
        System.out.println();
    }
}
