package com.rockets.domain;

/**
 * Enum de la capa Domain
 *
 * Puede tomar 3 valores en funcion de si el cohete, y por tanto sus propulsores, han de
 * acelerar (FORWARD) hasta una potencia objetivo, frenar (BACK) hasta una potencia objetivo,
 * o han alcanzado la potencia objetivo dada (FINISH)
 */
public enum StateRace {
    FORWARD,BACK,FINISH;
}
