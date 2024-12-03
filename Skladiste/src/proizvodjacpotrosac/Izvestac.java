/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proizvodjacpotrosac;

public class Izvestac extends Thread {
    private static int statId = 0;
    private int id = ++statId;
    Skladiste skladiste;

    public Izvestac(Skladiste skladiste) {
        this.skladiste = skladiste;
    }

    @Override
    public void run() {
        System.out.println("\n"+ "Izvestac " + id + ": Stanje -> " + skladiste.getStanje() +"\n");
    }

    @Override
    public String toString() {
        return "Izvestac{" + "id=" + id + '}';
    }
}


