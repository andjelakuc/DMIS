/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package naplatnarampa;

public class KategorizovanoVozilo implements Kategorizovano {
    private int kategorija;

    public KategorizovanoVozilo(int kategorija) {
        this.kategorija = kategorija;
    }

    @Override
    public int getKategorija() {
        return kategorija;
    }
}

