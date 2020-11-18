package com.rockets.view;

import com.rockets.application.Controller;

/**
 * Clase de la capa View
 *
 * Delega en el controller la accion de frenar el cohete hasta una potencia determinada
 *
 * Muestra por pantalla la accion a realizar, en este caso frenar y la potencia objetivo indicada
 */
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
