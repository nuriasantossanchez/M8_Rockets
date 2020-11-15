package com.rockets.view;

import com.rockets.application.Controller;

import java.util.Optional;

public class RocketsRice {

    private static Controller controller=Controller.getInstance();
    String idRocket1="LDSFJA32";
    String idRocket2="32WESSDS";

    public RocketsRice() {
        controller.createRocket(idRocket1,3);
        controller.addBoosterMaxPower(Optional.of(10),Optional.of(30),Optional.of(80));
        controller.createRocket(idRocket2,6);
        controller.addBoosterMaxPower(Optional.of(30),Optional.of(40),Optional.of(50),
                Optional.of(50),Optional.of(30),Optional.of(10));
    }
}
