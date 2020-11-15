package com.rockets.domain;


import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Booster implements Runnable{

    private Optional<Integer> maxPower=Optional.empty();
    private Integer currentPower= this.getCurrentPower();
    private boolean suspended=false;
    StateRace state;

    public Booster(Optional<Integer> maxPower) {
        this.maxPower = maxPower;
    }

    public StateRace getState() {
        return state;
    }

    public void setState(StateRace state) {
        this.state = state;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public Integer getCurrentPower() {
        currentPower = Optional.ofNullable(currentPower).orElse(0);
        return currentPower;
    }

    public void setMaxPower(Optional<Integer> maxPower){
        this.maxPower=maxPower;
    }

    public synchronized int powerUp(){
        if(this.currentPower < this.maxPower.get().intValue()){
            this.currentPower=this.currentPower+1;
        }
        return this.currentPower;
    }

    public synchronized int powerDown(){
        if(this.currentPower.intValue() > 0
                && this.currentPower.intValue() < this.maxPower.get().intValue()){
            this.currentPower=this.currentPower-1;
        }
        return this.currentPower;
    }

    @Override
    public String toString() {
        return "Booster{" +
                "maxPower=" + maxPower +
                ", currentPower=" + currentPower +
                '}';
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized (this) {
                    while (!this.isSuspended()) {
                        System.out.println("[wait...] " + this.toString());
                        wait();
                    }
                }

                synchronized (this) {
                    this.setSuspended(false);
                    TimeUnit.MILLISECONDS.sleep(1000);
                    switch (state){
                        case FORWARD:
                            TimeUnit.MILLISECONDS.sleep(1000);
                            this.powerUp();
                            notifyAll();
                            System.out.println("[acelerando...] " + this.toString());
                            wait();
                            break;
                        case BACK:
                            TimeUnit.MILLISECONDS.sleep(1000);
                            this.powerDown();
                            System.out.println("[frenando...] " + this.toString());
                            notifyAll();
                            wait();
                            break;
                        case FINISH:
                            TimeUnit.MILLISECONDS.sleep(1000);
                            notifyAll();
                            wait();
                            TimeUnit.MILLISECONDS.sleep(1000);
                            break;
                    }

                }
               TimeUnit.MILLISECONDS.sleep(1000);
            }
        } catch(InterruptedException e) {
            System.out.println("Booster interrupted: "
                    + Thread.currentThread().getName());
        }
    }

}
