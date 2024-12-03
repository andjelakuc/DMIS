/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package naplatnarampa;

public class Cenovnik {
    private final int[] putarine;

    public Cenovnik(int[] putarine) {
        this.putarine = putarine.clone();
    }

    public int getBrojKategorija() {
        return putarine.length;
    }

    public int getPutarina(int kategorija) {
        if (kategorija < 0 || kategorija >= putarine.length) {
            throw new IllegalArgumentException("Kategorija " + kategorija + " ne postoji u cenovniku.");
        }
        return putarine[kategorija];
    }
    
}

