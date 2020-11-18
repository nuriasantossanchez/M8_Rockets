package com.rockets.domain;

import com.rockets.application.factory.FactoryMethod;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Clase de la capa Domain
 *
 * Clase de tipo Rocket, identificada por un id y un numero de propulsores
 *
 * Contiene un listado de objetos de tipo Booster, con un tamaño igual al numero de propulsores
 *
 * Contiene una potencia objetivo, variable de tipo Integer que representa la potencia que
 * tiene que alcanzar el cohete, al celerar y/o frenar
 *
 * Contiene una variable de tipo int que es la potencia acumulada,
 * la suma de las potencias de cada propulsor
 *
 * Contiene una variable static de tipo int que es el numero objetos de tipo Rocket creados
 * y que participaran en la carrera de cohetes, es decir el total de cohetes que han de llegar
 * a una potencia objetivo
 *
 * Implementa la interfaz Runnable, cada objeto de tipo Rocket tendra su propio hilo de ejecucion.
 * Actua como Consumer, sumando las potencias que produce cada uno de sus propulsores
 *
 */
public class Rocket implements Runnable{
    private String id;
    private int numberOfBoosters;
    private List<Booster> boosters;
    private Integer goalPower=this.getGoalPower();
    private int acumPower;
    private static int countRockets;

    /**
     * Contructor de la clase, identificado por un String, el id del Rocket,
     * y una variable int, el numero de propulsores
     *
     * Inicializa el listado de boosters, para ello llama al metodo createBooster
     * de la interfaz FactoryMethod y crea tantos objetos de tipo Booster como indique
     * la variable numero de propulsores, pasada como parametro en el constructor
     *
     * @param id, String identificador del cohete
     * @param numberOfBoosters, numero de propulsores
     */
    public Rocket(String id, int numberOfBoosters) {
        this.id=id;
        this.numberOfBoosters=numberOfBoosters;
        this.boosters = Stream.generate(FactoryMethod::createBooster)
                    .limit(this.numberOfBoosters)
                    .collect(toList());
    }

    /**
     *
     * @return listado de objetos de tipo Booster
     */
    public List<Booster> getBoosters() {
        return this.boosters;
    }

    /**
     * Añade la potencia maxima a cada objeto del listado de tipo Booster
     *
     * @param maxPower, listado de longitud variable que representa la potencia maxima
     *                  que tendra cada propulsor. La longitud del listado varia en
     *                  funcion del numero de propulsores del cohete
     * @return listdo de objetos de tipo Booster con la  potencia maxima asociada a cada Booster
     */
    public List<Booster> addBoosterMaxPower(Optional<Integer>... maxPower) {
        int i=0;
        for(Optional<Integer> optPower : maxPower){
            this.getBoosters().get(i).setMaxPower(optPower);
            this.getBoosters().set(i,this.getBoosters().get(i));
            i++;
        }
        return this.getBoosters();
    }

    /**
     * @return Integer con la potencia objetivo que tiene que alcanzar un cohete tanto al acelerar
     * como al frenar. El valor inicial de la potencia objetivo no permite valores nulos,
     * en este caso el valor inicial es 0
     */
    public Integer getGoalPower() {
        goalPower=Optional.ofNullable(goalPower).orElse(0);
        return goalPower;
    }

    /**
     * Fija la potencia objetivo del cohete con el valor pasado como parametro
     *
     * @param goalPower, Integer con la potenca objetivo del cohete
     */
    public void setGoalPower(Integer goalPower) {
        this.goalPower = goalPower;
    }

    /**
     * Acelera el cohete hasta que la potencia acumulada, la suma de las potencias de cada
     * propulsor, alcance el valor de la potencia objetivo
     *
     * @param goalPower, Integer con la potencia objetivo que tiene que alcanzar el cohete al acelerar
     * @return la suma de las potencias de cada propulsor, potencia acumulada
     */
    public synchronized int forward(Optional<Integer> goalPower){
        if(this.acumPower<=goalPower.get().intValue()){
            this.acumPower = this.getAcumPower();
        }

        return this.acumPower;
    }

    /**
     * Frena el cohete hasta que la potencia acumulada, la suma de las potencias de cada
     * propulsor, disminuya hasta alcanzar la potencia objetivo
     *
     * @param goalPower, Integer con la potencia objetivo que tiene que alcanzar el cohete al frenar
     * @return la suma de las potencias de cada propulsor, potencia acumulada
     */
    public synchronized int back(Optional<Integer> goalPower){
        if(this.acumPower>=goalPower.get().intValue() && this.acumPower>0){
            this.acumPower = this.getAcumPower();
        }
        return acumPower;
    }

    /**
     * Suma las potencias de cada propulsor del cohete, cada vez que estas incrementan (acelerar)
     * o decrementan (frenar) su valor en 1
     *
     * @return la suma de las potencias de cada propulsor, potencia acumulada
     */
    public synchronized int getAcumPower(){
        return this.boosters.stream().mapToInt((c) -> c.getCurrentPower()).sum();
    }

    /**
     * Imprime la potencia total del cohete, potencia acumulada hasta el momento,
     * cada vez que varia la potencia de alguno de sus propulsores, indica si
     * la variacion de potencia ha sido al acelerar o al frenar
     *
     * @param state, enum de tipo StateRace que contiene los valores FORWARD, BACK o FINISH
     *              en funcion de si el cohete acelera, frena o ha llegado a la potencia objetivo
     */
    public void printAcumPower(StateRace state){
        if(this.acumPower!=0) {
            System.out.println(state + "\n[Total Power " + this.acumPower + "]\n"
                    + " " + this.toString()
                    + "\n" + this.getBoosters().toString() + "\n");
        }
    }

    /**
     * Metodo sobrecargado, llama al metodo printWinner() cuando el cohete alcanza la potencia objetivo
     *
     * @param acumPower, potencia acumulada del cohete, suma de las potencias de cada propulsor,
     *                   pasada como parametro para comprobar si el el cohete ha llegado a la
     *                   potencia objetivo
     * @return true si el cohete ha llegado a la potencia objetivo, y por tanto se ha mostrado por
     * pantalla el resultado, false en caso contrario
     */
    public boolean printWinner(int acumPower){
        boolean printed=false;
        if (acumPower==this.getGoalPower()){
            printWinner();
            printed=true;
        }
        return printed;
    }

    /**
     * Muestra por panatalla la potencia acumulada del cohete, ha de ser igual a la potencia objetivo,
     * junto con los datos del cohete y sus propulsores
     */
    public void printWinner(){
        System.out.println("--------------------------------------------------");
        System.out.println("FIN DE LA CARRERA!!! Potencia alcanzada: " + this.getAcumPower());
        System.out.println("ROCKET .... " + this.toString()
                + "  \n" + this.getBoosters().toString());
        System.out.println("--------------------------------------------------");
    }

    /**
     * Cambia el estado de los propulsores del cohete al valor FINISH cuando un cohete alcanza
     * la potencia objetivo.
     * Disminuye en 1 el numero de cohetes que han de alcanzar el objetivo
     */
    public void checkWinner(){
        this.getBoosters().stream().forEach(c -> c.setState(StateRace.FINISH));
        Rocket.countRockets--;
    }

    /**
     * @return el numero objetos de tipo Rocket creados y que participaran en la carrera de cohetes,
     * es decir el total de cohetes que han de llegar a una potencia objetivo
     */
    public static int getCountRockets() {
        return countRockets;
    }

    /**
     * Fija el total de cohetes que participan en la carrera, a partir del valor pasado como parametro
     *
     * @param countRockets, total de cohetes que participan en la carrera
     */
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

    /**
     * Sobreescribe el metodo run por implementar la interfaz Runnable
     *
     * Cada Rocket actua como Consumer, sumando las potencias que produce cada uno de sus propulsores
     *
     * En funcion de los valores que tome el enum StateRace (FORWARD,BACK,FINISH)
     * el cohete acelera o frena, hasta alcanzar una potencia objetivo o pasa a estado finish
     * (indicando que ha llegado a la potencia objetivo)
     */
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
                                    printAcumPower(StateRace.FORWARD);

                                    if(printWinner(this.acumPower)) {
                                        checkWinner();
                                    }

                                    booster.notifyAll();
                                    Thread.currentThread().checkAccess();
                                    TimeUnit.MILLISECONDS.sleep(1000);
                                    break;
                                case BACK:
                                    TimeUnit.MILLISECONDS.sleep(1000);

                                    this.acumPower = this.back(Optional.of(this.getGoalPower()));
                                    printAcumPower(StateRace.BACK);

                                    if(printWinner(this.acumPower)) {
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
