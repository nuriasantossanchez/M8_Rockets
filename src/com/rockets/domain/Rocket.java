package com.rockets.domain;

import com.rockets.application.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Rocket implements Runnable{
    private String id;
    private int numberOfBoosters;
    private List<Booster> boosters;
    private Integer goalPower=this.getGoalPower();
    private int acumPower;
    private static int countRockets;

    public Rocket(String id, int numberOfBoosters) {

        this.id=id;
        this.numberOfBoosters=numberOfBoosters;
        this.boosters = Stream.generate(FactoryMethod::create)
                    .limit(this.numberOfBoosters)
                    .collect(toList());
    }

    public List<Booster> getBoosters() {
        return this.boosters;
    }

    public List<Booster> addBoosterMaxPower(Optional<Integer>... maxPower) {
        int i=0;
        for(Optional<Integer> optPower : maxPower){
            this.getBoosters().get(i).setMaxPower(optPower);
            this.getBoosters().set(i,this.getBoosters().get(i));
            i++;
        }
        return this.getBoosters();
    }

    public Integer getGoalPower() {
        goalPower=Optional.ofNullable(goalPower).orElse(0);
        return goalPower;
    }

    public void setGoalPower(Integer goalPower) {
        this.goalPower = goalPower;
    }


    public synchronized int forward(Optional<Integer> goalPower){
        if(this.acumPower<=goalPower.get().intValue()){
            this.acumPower = this.getTotalPower();
        }

        return this.acumPower;
    }

    public synchronized int back(Optional<Integer> goalPower){
        if(this.acumPower>=goalPower.get().intValue() && this.acumPower>0){
            this.acumPower = this.getTotalPower();
        }
        return acumPower;
    }

    public synchronized int getTotalPower(){
        return this.boosters.stream().mapToInt((c) -> c.getCurrentPower()).sum();
    }


    public void printTotalPower(StateRace state){
        if(this.acumPower!=0) {
            System.out.println(state + "\n[Total Power " + this.acumPower + "]\n"
                    + " " + this.toString()
                    + "\n" + this.getBoosters().toString() + "\n");
        }
    }

    public boolean printWinner(int acumPower, StateRace state){
        boolean printed=false;
        if (state.equals(StateRace.FORWARD) && acumPower>=this.getGoalPower()){
            printWinner();
            printed=true;
        }
        else if(state.equals(StateRace.BACK) && acumPower<=this.getGoalPower()){
            printWinner();
            printed=true;
        }
        return printed;

    }

    public void printWinner(){

        System.out.println("--------------------------------------------------");
        System.out.println("FIN DE LA CARRERA!!! Potencia alcanzada: " + this.getTotalPower());
        System.out.println("ROCKET .... " + this.toString()
                + "  \n" + this.getBoosters().toString());
        System.out.println("--------------------------------------------------");

    }

    public void checkWinner(){
        this.getBoosters().stream().forEach(c -> c.setState(StateRace.FINISH));
        Rocket.countRockets--;
    }

    public static int getCountRockets() {
        return countRockets;
    }

    public static void setCountRockets(int countRockets) {
        Rocket.countRockets = countRockets;
    }


    @Override
    public String toString() {
        return "Rocket{" +
                "id='" + id + '\'' +
                ", numberOfBoosters=" + numberOfBoosters +
                '}';
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()) {

                synchronized (this) {
                    for (Booster booster : this.getBoosters()) {
                        synchronized (booster) {
                            booster.setSuspended(true);
                            switch (booster.getState()){
                                case FORWARD:
                                    TimeUnit.MILLISECONDS.sleep(1000);

                                    this.acumPower = this.forward(Optional.of(this.getGoalPower()));
                                    printTotalPower(StateRace.FORWARD);

                                    if(printWinner(this.acumPower, booster.getState())) {
                                        checkWinner();
                                    }

                                    booster.notifyAll();
                                    Thread.currentThread().checkAccess();
                                    TimeUnit.MILLISECONDS.sleep(1000);
                                    break;
                                case BACK:
                                    TimeUnit.MILLISECONDS.sleep(1000);

                                    this.acumPower = this.back(Optional.of(this.getGoalPower()));
                                    printTotalPower(StateRace.BACK);

                                    if(printWinner(this.acumPower, booster.getState())) {
                                        checkWinner();
                                    }

                                    booster.notifyAll();
                                    Thread.currentThread().checkAccess();
                                    TimeUnit.MILLISECONDS.sleep(1000);
                                    break;
                                case FINISH:
                                    Thread.currentThread().checkAccess();
                                    break;
                            }
                        }
                    }
                }
                TimeUnit.MILLISECONDS.sleep(1000);
            }

        } catch(InterruptedException e) {
            System.out.println("Rocket interrupted: " + Thread.currentThread().getName()
                    + "\n" + this.toString() +this.getBoosters().toString());
        }
    }

}
