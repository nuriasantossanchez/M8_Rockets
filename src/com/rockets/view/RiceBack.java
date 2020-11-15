package com.rockets.view;

import com.rockets.application.Controller;

import java.util.concurrent.ExecutionException;

public class RiceBack {

    private static Controller controller=Controller.getInstance();
    int powerBack;

    public RiceBack(int powerBack) {
        this.powerBack = powerBack;
        controller.back(powerBack);
    }
}
