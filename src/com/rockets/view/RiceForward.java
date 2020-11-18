package com.rockets.view;

import com.rockets.application.Controller;

/**
 * Clase de la capa View
 *
 * Delega en el controller la accion de acelerar el cohete hasta una potencia determinada
 *
 * Muestra por pantalla la accion a realizar, en este caso acelerar y la potencia objetivo indicada
 */

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
