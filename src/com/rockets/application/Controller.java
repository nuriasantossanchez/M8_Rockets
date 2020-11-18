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
     * @param id, String identificador del cohete
     * @param numberOfBoosters, numero de propulsores
     * @return objeto de tipo Rocket creado
     */
    public Rocket createRocket(String id, int numberOfBoosters){
        this.rocket = factory.create(id, numberOfBoosters);
        this.rockets.add(rocket);
        return this.rocket;

    }

    /**
     * Delega en el objeto Rocket la accion de añadir la potencia maxima
     * a cada uno de sus propulsores
     *
     * @param maxPower, listado de longitud variable que representa la potencia maxima
     *                  que tendra cada propulsor. La longitud del listado varia en
     *                  funcion del numero de propulsores del cohete
     * @return
     */
    public List<Booster> addBoosterMaxPower(Optional<Integer>... maxPower) {
        this.rocket.addBoosterMaxPower(maxPower);
        return this.rocket.getBoosters();
    }

    /**
     * Setea la variable staic countRockets (clase Rocket) con el numero total de
     * Rockets creados, almacenados en un listado de objetos de tipo Rocket
     *
     * Recorre el listado de objetos de tipo Rocket creados y delega en cada objeto la accion
     * de acelerar hasta alcanzar una potencia objetivo
     *
     * @param goalPower, potencia objetivo que han de alcanzar cada uno de los cohetes
     *                   participantes en la carrera
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
     * Setea la variable staic countRockets (clase Rocket) con el numero total de
     * Rockets creados, almacenados en un listado de objetos de tipo Rocket
     *
     * Recorre el listado de objetos de tipo Rocket creados y delega en cada objeto la accion
     * de frenar hasta alcanzar una potencia objetivo
     *
     * @param goalPower, potencia objetivo que han de alcanzar cada uno de los cohetes
     *                   participantes en la carrera
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
     * @return objeto de tipo ExecutorService que proporciona métodos para administrar
     * el progreso de una o más tareas asincrónicas.
     */
    public ExecutorService getExec() {
        return exec;
    }

    /**
     * Ejecuta de forma asincrona una accion, identificada por implementar la interfaz Runnable,
     * que se completa mediante una tarea que se ejecuta en un ExecutorService dado después
     * la ejecuccion de la acción
     *
     * En este caso la acciones Runnables que se ejecutan de forma asincrona estan encapsuladas
     * tanto en los objeto de tipo Rocket que se crean (2 en total, pero podrian ser mas),
     * como en su respectivos Booster (9 en total, pero podrian ser mas)
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
