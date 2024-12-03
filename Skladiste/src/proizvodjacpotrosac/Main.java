/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proizvodjacpotrosac;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.exit;

public class Main {

    static final int BR_PROIZVODA = 10;
    static final int BR_PROIZVODJACA = 20;
    static final int BR_POTROSACA = 30;
    static final int BR_IZVESTACA = 3;
    static final int IZVESTACI_PERIOD = 10; //na 10 sekundi se poziva sledeci izvestac
    static boolean finished = false;

    public static void main(String[] args) {

        System.out.println("Pritisni ENTER za prekid simulacije!");
        try {
            Thread.sleep(2000); //kako bi korisnik procitao poruku o prekidu
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //KREIRANJE SKLADISTA
        Skladiste skladiste = new Skladiste(BR_PROIZVODA);

        //KREIRANJE PROIZVODJACA
        List<Proizvodjac> proizvodjaci = new ArrayList<>();
        for (int i = 0; i<BR_PROIZVODJACA; i++){
            proizvodjaci.add(new Proizvodjac(skladiste,1000,4000));
        }

        //KREIRANJE POTROSACA
        List<Potrosac> potrosaci = new ArrayList<>();
        for (int i = 0; i<BR_POTROSACA; i++){
            potrosaci.add(new Potrosac(skladiste,1000,7000));
        }

        //KREIRANJE IZVESTACA
        List<Izvestac> izvestaci = new ArrayList<>();
        for (int i=0;i<BR_IZVESTACA;i++){
            izvestaci.add(new Izvestac(skladiste));
        }

        //CITANJE IZ FAJLA
        List<Integer> rasporedIzvestaca = new ArrayList<>();
        try {
            File file = new File("src/proizvodjacpotrosac/raspored.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String izvestacNaziv = parts[1]; //naziv izvestaca koji se oglasava

                // naziv sadrzi id izvestaca na poslednjem karakteru
                char poslednjiKarakterNaziva = izvestacNaziv.charAt(izvestacNaziv.length() - 1);
                if (Character.isDigit(poslednjiKarakterNaziva)) {
                    int idIzvestaca = Character.getNumericValue(poslednjiKarakterNaziva);

                    // u listu koja cuva raspored izvestaca dodajemo id izvestaca
                    rasporedIzvestaca.add(idIzvestaca);
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Greška prilikom čitanja rasporeda: " + e.getMessage());
        }

        //ISPIS RASPOREDA
        System.out.println("Raspored izvestaca:");
        System.out.println(rasporedIzvestaca);

        //Zakazivanje izvrsavanja izvestaca prema ucitanom redosledu (rasporedu)
         ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);

        // Indeks koji prati trenutnu poziciju
        AtomicInteger currentIndex = new AtomicInteger(0);
        final int maxIterations = rasporedIzvestaca.size(); //max ulaza iz fajla

        threadPool.scheduleAtFixedRate(() -> {
            int idIzvestaca = rasporedIzvestaca.get(currentIndex.get());
            Izvestac izvestac = izvestaci.get(idIzvestaca-1);

            if (currentIndex.get() < maxIterations-1) {
                currentIndex.getAndIncrement();
                threadPool.submit(izvestac);
            } else {
                //ako je poslednji izvestac, prekinii ostale niti i zavrsi program
                //prvo prekidamo ostale niti kako se proizvodi ne bi vise dodavali i uzimali nakon izvestavanja
                //poslednjeg izvestaca
                zaustaviSimulaciju(proizvodjaci,potrosaci);
                threadPool.submit(izvestac);
                threadPool.shutdown();
                //finised postavljamo na true kako se ne bi vrsila obrada prekida kada se program sam zaustavi
                //kako se ne bi opet izvrsila funkcija zaustaviSimulaciju(...)
                finished = true;
                System.out.println("Izvršavanje izvestaca zavrseno.");
                exit(0);
            }
        }, IZVESTACI_PERIOD, IZVESTACI_PERIOD, TimeUnit.SECONDS); // pocetni delay i period izvrsavanja svake sledece niti su jednaki IZVESTACI_PERIOD
                                                                 // Napr ako je IZVESTACI_PERIOD = 10, prva nit se poziva posle 10 sekundi i svaka sledeca u intervalu od 10 sek.

        //POKRECEMO NITI
        for (Proizvodjac proizvodjac: proizvodjaci){
            proizvodjac.start();
        }

        for (Potrosac potrosac: potrosaci){
            potrosac.start();
        }

        // Kontrola za prekidanje simulacije preko tastature
        // unos ENTER za prekid
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input != null) {
            exit(0);
        }

        // OBRADA PREKIDA PROGRAMA (STOP MAIN)
        // Ova fj-a se poziva kada se prekine program sa STOP MAIN ili kada se zavrsi program sam od sebe ili sa exit()
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(!finished){
                // pozivamo funkciju za zaustavljanje simulacije na prekid rada programa
                threadPool.shutdown();
                zaustaviSimulaciju(proizvodjaci,potrosaci);
            }
        }));

    }

    public static void zaustaviSimulaciju(List<Proizvodjac> proizvodjaci, List<Potrosac> potrosaci){
        //ZAUSTAVLJANJE NITI
        for (Proizvodjac proizvodjac: proizvodjaci){
            proizvodjac.interrupt();
        }

        for (Potrosac potrosac: potrosaci){
            potrosac.interrupt();
        }

        for (Proizvodjac proizvodjac: proizvodjaci){
            try {
                proizvodjac.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (Potrosac potrosac: potrosaci){
            try {
                potrosac.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("PREKINUTA SIMULACIJA");
    }
}