package com.rockets.view;

import com.rockets.application.Controller;

public class RiceForward {

    private static Controller controller=Controller.getInstance();
    int powerForward;

    public RiceForward(int powerForward) {
        this.powerForward = powerForward;
        controller.forward(powerForward);
    }

    public void show(){
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("ACELERAR HASTA POTENCIA " + powerForward + " ....... ") ;
        System.out.println("--------------------------------------------------");
        System.out.println();
    }
}
