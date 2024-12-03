/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proizvodjacpotrosac;

import java.util.concurrent.ThreadLocalRandom;

public class Potrosac extends Thread{
    private static int statId = 0;
    private int id = ++statId;

    private Skladiste skladiste;
    private int minTime ;
    private int maxTime ;

    public Potrosac(Skladiste skladiste,int minTime, int maxTime) {
        this.skladiste = skladiste;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    @Override
    public void run() {
        try{
            while(!interrupted()){
                Thread.sleep(ThreadLocalRandom.current().nextLong(minTime, maxTime));
                int proizvod = skladiste.Uzmi();
                System.out.println("Potrosac " + id + " je potrosio proizvod " + proizvod);
            }
        } catch (InterruptedException ex) {
           System.out.println("Potrosac "+id+ " je prekinut");
        }
    }
}

