package model;

import util.AudioManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta un mazzo di carte da gioco.
 * Il mazzo contiene una lista di {@link Carta} e fornisce metodi per
 * mescolare, distribuire carte, controllare le carte rimanenti e resettare il mazzo.
 */
public class Mazzo {
    private List<Carta> carteDelMazzo;

    /**
     * Costruisce un nuovo mazzo di carte standard italiane,
     * inizializzando tutte le carte con valori, semi, forza e immagini associate.
     * Il mazzo viene mescolato automaticamente alla creazione.
     */
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

    /**
     * Mescola le carte del mazzo in modo casuale.
     * Riproduce anche un suono di distribuzione tramite {@link AudioManager}.
     */
    public void mischia() {
        AudioManager.getInstance().play("audio/distribuisci.wav");
        Collections.shuffle(carteDelMazzo);
        System.out.println("Mazzo mischiato con successo!");
    }

    /**
     * Distribuisce (rimuove e restituisce) la prima carta dal mazzo.
     *
     * @return La carta distribuita, o {@code null} se il mazzo è vuoto.
     */
    public Carta distribuisciCarta() {
        if (carteDelMazzo.isEmpty()) {
            return null;
        }
        return carteDelMazzo.remove(0);
    }

    /**
     * Restituisce il numero di carte rimanenti nel mazzo.
     *
     * @return Il numero di carte ancora disponibili nel mazzo.
     */
    public int carteRimanenti() {
        return carteDelMazzo.size();
    }

    /**
     * Resetta il mazzo svuotandolo e ricreando tutte le carte originali,
     * quindi mescola nuovamente il mazzo.
     */
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
