package com.rockets.application;

import com.rockets.domain.Booster;
import com.rockets.domain.Rocket;

import java.util.Optional;


public interface FactoryMethod {

    Rocket create(String id, int numberOfBoosters);

    static Booster create() {
        return new Booster(Optional.empty());
    }

}
