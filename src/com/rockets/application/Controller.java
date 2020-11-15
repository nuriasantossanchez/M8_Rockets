package com.rockets.application;

import com.rockets.domain.Booster;
import com.rockets.domain.Rocket;
import com.rockets.domain.StateRace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;


public class Controller {
    private static Controller instance=null;
    private Rocket rocket;
    private List<Rocket> rockets = new ArrayList<Rocket>();
    private FactoryMethod factory;
    private ExecutorService exec;

    private Controller(FactoryMethod factory) {
        this.factory = factory;
        exec = Executors.newCachedThreadPool();
    }

    public static Controller getInstance(){
        if (instance==null){
            instance=new Controller(new RocketFactory());
        }
        return instance;

    }

    public Rocket createRocket(String id, int numberOfBoosters){
        this.rocket = factory.create(id, numberOfBoosters);
        this.rockets.add(rocket);
        return this.rocket;

    }

    public List<Booster> addBoosterMaxPower(Optional<Integer>... maxPower) {
        this.rocket.addBoosterMaxPower(maxPower);
        return this.rocket.getBoosters();
    }

    public void forward(Integer goalPower){
        for (Rocket rocket : this.rockets) {
            rocket.forward(Optional.of(goalPower));
            rocket.setGoalPower(goalPower);
            rocket.getBoosters().stream().forEach(c->c.setState(StateRace.FORWARD));
        }
     }

    public void back(Integer goalPower){
        for (Rocket rocket : this.rockets) {
            rocket.back(Optional.of(goalPower));
            rocket.setGoalPower(goalPower);
            rocket.getBoosters().stream().forEach(c->c.setState(StateRace.BACK));

        }
    }

    public ExecutorService getExec() {
        return exec;
    }

    public void execute(){
        for (Rocket rocket : this.rockets) {
            CompletableFuture.runAsync(rocket, exec);
            for (Booster booster : rocket.getBoosters()) {
                CompletableFuture.runAsync(booster, exec);
            }
        }
    }
}
