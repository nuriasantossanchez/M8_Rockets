package com.rockets.application.factory;

import com.rockets.domain.Booster;
import com.rockets.domain.Rocket;

import java.util.Optional;

/**
 * Interfaz de la capa Application
 *
 * Se utiliza para implementar el patron AbstractFactory con el fin de
 * desacoplar la creacion de objetos
 */
public interface FactoryMethod {

    Rocket create(String id, int numberOfBoosters);

    /**
     * Metodo static implementado en la interfaz, accesible directamente desde la interfaz.
     * Al ser static, el método es inicializado y esta disponible cuando se carga
     * por primera vez la interfaz, esto es cuando es accedida por primera vez
     *
     * @return objeto de tipo Booster que puede ser inicializado opcionalmente
     * con un parametro vacio
     */
    static Booster createBooster() {
        return new Booster(Optional.empty());
    }

}
