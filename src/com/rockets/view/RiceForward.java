package com.rockets.view;

import com.rockets.application.Controller;

import java.util.concurrent.ExecutionException;

public class RiceForward {

    private static Controller controller=Controller.getInstance();
    int powerForward;

    public RiceForward(int powerForward) {
        this.powerForward = powerForward;
        controller.forward(powerForward);
    }
}
