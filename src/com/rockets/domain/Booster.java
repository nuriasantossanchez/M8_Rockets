package com.rockets.domain;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Clase de la capa Domain
 *
 * Clase de tipo Booster, identificada por una potencia maxima
 *
 * Contiene una variable de tipo Integer, currentPower, que es la potencia actual, incrementa o
 * decrementa su valor en 1, en funcion de si el cohete acelera o frena
 *
 * Contiene una variable boolean suspended, en funcion de su valor deja en espera el hilo en ejecucion
 *
 * Contiene un enum de tipo StateRace con los valores FORWARD,BACK y FINISH para indicar si el cohete
 * acelera, frena, o ha alcanzado la potencia objetivo
 *
 * Implementa la interfaz Runnable, cada objeto de tipo Booster tendra su propio hilo de ejecucion.
 * Actua como Procuder, incrementando o decrementando en 1 la pontecia actual de cada propulsor,
 * en funcion de si el cohete acelera o frena
 */
public class Booster implements Runnable{

    private Optional<Integer> maxPower=Optional.empty();
    private Integer currentPower= this.getCurrentPower();
    private boolean suspended=true;
    private StateRace state;

    /**
     * Contructor de la clase, identificado por un Integer, maxPower, que representa
     * la potencia maxima que puede alcanzar cada propulsor
     *
     * @param maxPower, potencia maxima que puede alcanzar cada propulsor
     */
    public Booster(Optional<Integer> maxPower) {
        this.maxPower = maxPower;
    }

    /**
     * @return enum de tipo StateRace que contiene los valores FORWARD,BACK y FINISH
     * para indicar si el cohete acelera, frena, o ha alcanzado la potencia objetivo
     */
    public StateRace getState() {
        return state;
    }

    /**
     * Fija el enum de tipo StateRace con el valor pasado como parametro
     * @param state, enum StateRace que puede tomar los valores FORWARD,BACK y FINISH
     *               (acelerar, frenar o potencia objetivo alcanzada)
     */
    public void setState(StateRace state) {
        this.state = state;
    }

    /**
     * @return false pone en espera el hilo en ejecucion, true hace que siga la ejecucion
     */
    public boolean isSuspended() {
        return suspended;
    }

    /**
     * Fija la variable boolean suspended con el valor pasado como parametro
     *
     * @param suspended, false para poner en espera el hilo en ejecucion,
     *                   true para que siga la ejecucion
     */
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    /**
     * @return Integer con la potencia actual del propulsor
     * El valor inicial de la potencia actual no permite valores nulos,
     * en este caso el valor inicial es 0
     */
    public Integer getCurrentPower() {
        currentPower = Optional.ofNullable(currentPower).orElse(0);
        return currentPower;
    }

    /**
     * Fija la potencia maxima de capa propulsor en funcion del valor pasado como parametro
     * @param maxPower, potencia maxima que puede alcanzar un propulsor
     */
    public void setMaxPower(Optional<Integer> maxPower){
        this.maxPower=maxPower;
    }

    /**
     * Incrementa en 1 la potencia actual del propulsor mientras no se supere su potencia maxima
     *
     * @return la potencia actual del propulsor con el incremento aplicado
     */
    public synchronized int powerUp(){
        if(this.currentPower < this.maxPower.get().intValue()){
            this.currentPower=this.currentPower+1;
        }
        return this.currentPower;
    }

    /**
     * Decrementa en 1 la potencia actual del propulsor mientras no se supere su potencia maxima
     * y la potencia actual no sea inferior a cero
     *
     * @return la potencia actual del propulsor con el decremento aplicado
     */
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

    /**
     * Sobreescribe el metodo run por implementar la interfaz Runnable
     *
     * Cada Booster actua como Producer, produciendo una potencia actual que se
     * ira incrementando o decrementando sin sobrepasar la potencia maxima permitida
     *
     * En funcion de los valores que tome el enum StateRace (FORWARD,BACK,FINISH)
     * el propulsor incrementa o decrementa su potencia actual en 1,
     * o pasa al estado finish indicando que se ha llegado a la potencia objetivo
     */
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
                            if(!(Rocket.getCountRockets() < 0)){
                                this.powerUp();
                                notifyAll();
                                System.out.println("[acelerando...] " + this.toString());
                            }
                            wait();
                            break;
                        case BACK:
                            TimeUnit.MILLISECONDS.sleep(1000);
                            if(!(Rocket.getCountRockets() < 0)){
                                this.powerDown();
                                notifyAll();
                                System.out.println("[frenando...] " + this.toString());
                            }
                            wait();
                            break;
                        case FINISH:
                            notifyAll();
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
