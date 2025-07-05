package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazzo {
    private List<Carta> carteDelMazzo;

    public Mazzo() {
        this.carteDelMazzo = new ArrayList<>();

        String[] semi = {"Denari", "Coppe", "Bastoni", "Spade"};
        String[] valoriNomi = {"Asso", "Due", "Tre", "Quattro", "Cinque", "Sei", "Sette", "Fante", "Cavallo", "Re"};
        int[] valoriNumerici = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] forze = {3, 2, 1, 0, 0, 0, 0, -1, -2, -3};
        String baseImagePath = "immagini_carte/";

        for (String seme : semi) {
            for (int i = 0; i < valoriNomi.length; i++) {
                String nomeValore = valoriNomi[i];
                int valoreNum = valoriNumerici[i];
                int forzaCarta = forze[i];
                String nomeImmagine = baseImagePath + nomeValore.toLowerCase() + "_" + seme.toLowerCase() + ".png";

                Carta carta = new Carta(valoreNum, seme, forzaCarta, nomeImmagine);
                carteDelMazzo.add(carta);
            }
        }
        mischia();
    }

    public void mischia() {
        Collections.shuffle(carteDelMazzo);
        System.out.println("Mazzo mischiato con successo!");
    }

    public Carta distribuisciCarta() {
        if (carteDelMazzo.isEmpty()) {
            return null;
        }
        return carteDelMazzo.remove(0);
    }

    public int carteRimanenti() {
        return carteDelMazzo.size();
    }

    public void reset() {
        carteDelMazzo.clear();
        String[] semi = {"Denari", "Coppe", "Bastoni", "Spade"};
        String[] valoriNomi = {"Asso", "Due", "Tre", "Quattro", "Cinque", "Sei", "Sette", "Fante", "Cavallo", "Re"};
        int[] valoriNumerici = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] forze = {3, 2, 1, 0, 0, 0, 0, -1, -2, -3};
        String baseImagePath = "immagini_carte/";

        for (String seme : semi) {
            for (int i = 0; i < valoriNomi.length; i++) {
                String nomeValore = valoriNomi[i];
                int valoreNum = valoriNumerici[i];
                int forzaCarta = forze[i];
                String nomeImmagine = baseImagePath + nomeValore.toLowerCase() + "_" + seme.toLowerCase() + ".png";

                Carta carta = new Carta(valoreNum, seme, forzaCarta, nomeImmagine);
                carteDelMazzo.add(carta);
            }
        }
        mischia();
    }
}
