/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package naplatnarampa;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Postavljanje osnovnog cenovnika
        int[] osnovnePutarine = {50, 100, 150};
        Cenovnik cenovnik = new Cenovnik(osnovnePutarine);

        // Kreiranje osnovne stanice
        NaplatnaStanica osnovnaStanica = new NaplatnaStanica(cenovnik);

        // Kreiranje rampe
        NaplatnaRampa rampa = new NaplatnaRampa("Rampa", 3, 2.0, osnovnaStanica);
        
        // Otvaranje rampe
        System.out.println("Otvaranje rampe...");
        rampa.otvori(cenovnik);

        System.out.println("Rampa otvorena:");
        System.out.println(rampa);
        
        new Thread(() -> rampa.radRampe(10)).start();
        
        // Pauza za simulaciju
        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Ispis trenutnog stanja rampe
        System.out.println("Stanje rampe nakon simulacije:");
        System.out.println(rampa);
        
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        

        // Zatvaranje rampe
        System.out.println("Zatvaranje rampe...");
        rampa.zatvori();
        System.out.println("Rampa zatvorena:");
        System.out.println(rampa);

        // Uništavanje rampe
        rampa.unisti();
        System.out.println("Rampa unistena:");
        System.out.println(rampa);
    }
}

