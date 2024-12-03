/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package naplatnarampa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NaplatnaRampa {
    private final String naziv;
    final List<NaplatnaStanica> stanice = new ArrayList<>();
    private final double tsr;
    private boolean otvorena = false;
    private Random random = new Random();
    private int naplaceniIznos;

    public NaplatnaRampa(String naziv, int brojStanica, double tsr, NaplatnaStanica osnovnaStanica) {
        this.naziv = naziv;
        this.tsr = tsr;
        for (int i = 0; i < brojStanica; i++) {
            stanice.add(osnovnaStanica.napraviKopiju());
        }
        this.random = new Random();
        this.naplaceniIznos = 0;
    }

    public void otvori(Cenovnik noviCenovnik) {
        if (otvorena) throw new IllegalStateException("Rampa je već otvorena.");
        for (NaplatnaStanica stanica : stanice) {
            stanica.postaviCenovnik();
        }
        otvorena = true;
    }

    public void zatvori() {
        if (!otvorena) throw new IllegalStateException("Rampa nije otvorena.");
        otvorena = false;
    }
    
    public void unisti() {
        if (otvorena) {
            zatvori();
        }
        stanice.clear();
    }

    public int getUkupanNaplaceniIznos() {
        naplaceniIznos = stanice.stream().mapToInt(NaplatnaStanica::getUkupanNaplaceniIznos).sum();
        return naplaceniIznos;
    }
    
    public void dolazakVozila() {
        if (!otvorena) {
            throw new IllegalStateException("Rampa nije otvorena.");
        }
        if (stanice.isEmpty()) {
            throw new IllegalStateException("Rampa nema stanica za simulaciju.");
        }

        int kategorija = random.nextInt(stanice.get(0).postaviCenovnik().getBrojKategorija());
        Kategorizovano vozilo = new KategorizovanoVozilo(kategorija);

        int indeksStanice = random.nextInt(stanice.size());
        NaplatnaStanica stanica = stanice.get(indeksStanice);

        // Iz cenovnika stanice se uzima putarina za kategoriju vozila
        int iznos = stanica.postaviCenovnik().getPutarina(vozilo.getKategorija());
        naplaceniIznos += iznos;
        stanica.naplatiPutarinu(vozilo);

        try {
            // slučajni vremenski interval u rasponu (1±0.3)*tsr
            double interval = tsr * (1 + (random.nextDouble() - 0.5) * 0.6);
            // Oduzimamo 0.5 da bismo dobili vrednosti u opsegu [-0.5, 0.5]
            // Množenjem sa 0.6 dobijamo interval u opsegu [-0.3, 0.3]
            Thread.sleep((long) (interval * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void radRampe(int trajanjeSekundi) {
        if (!otvorena) {
            throw new IllegalStateException("Rampa mora biti otvorena pre simulacije.");
        }
        if (trajanjeSekundi <= 0) {
            throw new IllegalArgumentException("Trajanje simulacije mora biti pozitivno.");
        }

        long pocetnoVreme = System.currentTimeMillis();
        long trajanjeMillis = trajanjeSekundi * 1000;

        while (System.currentTimeMillis() - pocetnoVreme < trajanjeMillis) {
            dolazakVozila(); // Simulacija jednog dolaska vozila
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(naziv + "(" + getUkupanNaplaceniIznos() + "):");
        for (NaplatnaStanica stanica : stanice) {
            sb.append(stanica).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
    
}

