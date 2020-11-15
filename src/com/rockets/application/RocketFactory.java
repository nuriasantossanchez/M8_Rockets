package com.rockets.application;

import com.rockets.domain.Rocket;

public class RocketFactory implements FactoryMethod{
    @Override
    public Rocket create(String id, int numberOfBoosters) {
        return new Rocket(id, numberOfBoosters);
    }

}
