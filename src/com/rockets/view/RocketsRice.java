package com.rockets.view;

import com.rockets.application.Controller;
import com.rockets.domain.Booster;
import com.rockets.domain.Rocket;

import java.util.List;
import java.util.Optional;

/**
 * Clase de la capa View
 *
 * Crea los cohetes que van a participar en la carrera, en este caso 2, y sus respectivos propulsores,
 * en este caso, 3 propulsores para el cohete1, con sus respectivas potencias maximas 10, 30, 80 y
 * 6 propulsores para el cohete2 con las potencias maximas 30, 40, 50, 50, 30, 10
 *
 * Muestra por pantalla la informacion de los cohetes y sus respectivos propulsores
 */
public class RocketsRice {

    private static Controller controller=Controller.getInstance();
    String idRocket1="LDSFJA32";
    String idRocket2="32WESSDS";
    Rocket rocket1;
    List<Booster> boostersRocket1;
    Rocket rocket2;
    List<Booster> boostersRocket2;

    public RocketsRice() {
        rocket1 = controller.createRocket(idRocket1,3);
        boostersRocket1 = controller.addBoosterMaxPower(Optional.of(10),Optional.of(30),Optional.of(80));
        rocket2 = controller.createRocket(idRocket2,6);
        boostersRocket2 = controller.addBoosterMaxPower(Optional.of(30),Optional.of(40),Optional.of(50),
                Optional.of(50),Optional.of(30),Optional.of(10));
    }

    public void show(){
        System.out.println("--------------------------------------------------");
        System.out.println("CARRERA DE COHETES ....... ");
        System.out.println("--------------------------------------------------");
        System.out.println("COHETE 1");
        System.out.println(rocket1.toString());
        System.out.println();
        boostersRocket1.stream().forEach(System.out::println);
        System.out.println();
        System.out.println("COHETE 2");
        System.out.println(rocket2.toString());
        System.out.println();
        boostersRocket2.stream().forEach(System.out::println);
        System.out.println("--------------------------------------------------");
    }
}
