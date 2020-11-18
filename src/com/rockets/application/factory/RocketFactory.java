package com.rockets.application.factory;

import com.rockets.domain.Rocket;

/**
 * Clase de la capa Application
 *
 * Se utiliza para implementar el patron AbstractFactory
 *
 * Implementa la interfaz FactoryMethod en cuyo metodo queda
 * desacoplada la creacion de objetos de tipo Rocket
 *
 */
public class RocketFactory implements FactoryMethod {

    /**
     * Crea un objeto de tipo Rocket
     *
     * @param id, identificador del cohete
     * @param numberOfBoosters, numero de propulsores
     * @return objeto de tipo Rocket
     */
    @Override
    public Rocket create(String id, int numberOfBoosters) {
        return new Rocket(id, numberOfBoosters);
    }

}
