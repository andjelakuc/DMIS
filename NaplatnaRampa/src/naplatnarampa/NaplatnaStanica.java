/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package naplatnarampa;

public class NaplatnaStanica {
    private static int sledeciId = 0;
    private final int id;
    private Cenovnik cenovnik;
    private int naplaceniIznos;

    public NaplatnaStanica(Cenovnik cenovnik) {
        this.id = sledeciId++;
        this.cenovnik = cenovnik;
        this.naplaceniIznos = 0;
    }

    public Cenovnik postaviCenovnik() {
        return cenovnik;
    }

    public NaplatnaStanica napraviKopiju() {
        return new NaplatnaStanica(this.cenovnik);
    }

    public void naplatiPutarinu(Kategorizovano vozilo) {
        if (cenovnik == null) {
            throw new IllegalStateException("Cenovnik nije definisan.");
        }
        int kategorija = vozilo.getKategorija();
        int putarina = cenovnik.getPutarina(kategorija);
        naplaceniIznos += putarina;
    }

    public int getUkupanNaplaceniIznos() {
        return naplaceniIznos;
    }

    @Override
    public String toString() {
        return id + "(" + naplaceniIznos + ")";
    }
}

