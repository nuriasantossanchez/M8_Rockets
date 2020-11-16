package com.rockets.application;

import com.rockets.application.factory.FactoryMethod;
import com.rockets.application.factory.RocketFactory;
import com.rockets.domain.Booster;
import com.rockets.domain.Rocket;
import com.rockets.domain.StateRace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Clase de la capa Application
 *
 * Implementa el patron Singleton
 *
 * Hace uso del patron AbstractFactory para la creacion de objetos de la capa de Dominio
 *
 * Hace uso de la interfaz ExecutorService para la creacion de un thread pool que sera
 * utilizado para hacer correr las clases del Dominio que implementen la interfaz Runnable
 *
 */
public class Controller {
    private static Controller instance=null;
    private Rocket rocket;
    private List<Rocket> rockets = new ArrayList<Rocket>();
    private FactoryMethod factory;
    private ExecutorService exec;

    /**
     * Constructor privado de la clase Controller. El constructor privado asegura que la clase
     * solo tendra una instancia (patron Singleton), que sera accedida por un punto global
     *
     * Inicializa la interfaz FactoryMethod que implementa el patron AbstractFactory
     *
     * Inicializa la interfaz ExecutorService con un pool de threads proporcionado por la clase Executors
     *
     * @param factory interfaz de tipo FactoryMethod
     */
    private Controller(FactoryMethod factory) {
        this.factory = factory;
        exec = Executors.newCachedThreadPool();
    }

    /**
     * Punto global de acceso a la instancia unica de la clase Controller
     *
     * @return instacia de la clase Controller
     */
    public static Controller getInstance(){
        if (instance==null){
            instance=new Controller(new RocketFactory());
        }
        return instance;
    }

    /**
     * Delega en la clase Factory la creacion de un objeto de tipo Rocket
     * Añade el objeto creado a un listado de objetos del mismo tipo
     *
     * @param id, identificador del cohete
     * @param numberOfBoosters, numero de propulsores
     * @return objeto de tipo Rocket creado
     */
    public Rocket createRocket(String id, int numberOfBoosters){
        this.rocket = factory.create(id, numberOfBoosters);
        this.rockets.add(rocket);
        return this.rocket;

    }

    /**
     *
     * @param maxPower
     * @return
     */
    public List<Booster> addBoosterMaxPower(Optional<Integer>... maxPower) {
        this.rocket.addBoosterMaxPower(maxPower);
        return this.rocket.getBoosters();
    }

    /**
     *
     * @param goalPower
     */
    public void forward(Integer goalPower){
        Rocket.setCountRockets(this.rockets.size());
        for (Rocket rocket : this.rockets) {
            rocket.forward(Optional.of(goalPower));
            rocket.setGoalPower(goalPower);
            rocket.getBoosters().stream().forEach(c->c.setState(StateRace.FORWARD));
        }
    }

    /**
     *
     * @param goalPower
     */
    public void back(Integer goalPower){
        Rocket.setCountRockets(this.rockets.size());
        for (Rocket rocket : this.rockets) {
            rocket.back(Optional.of(goalPower));
            rocket.setGoalPower(goalPower);
            rocket.getBoosters().stream().forEach(c->c.setState(StateRace.BACK));
        }
    }

    /**
     *
     * @return
     */
    public ExecutorService getExec() {
        return exec;
    }

    /**
     *
     */
    public void execute(){
        for (Rocket rocket : this.rockets) {
            CompletableFuture.runAsync(rocket, exec);
            for (Booster booster : rocket.getBoosters()) {
                CompletableFuture.runAsync(booster, exec);
            }
        }
    }
}
