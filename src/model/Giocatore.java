package model;

import java.util.ArrayList;
import java.util.List;

public class Giocatore {
    private String ruolo;
    private String nome;
    private double punteggio; // punteggio decimale
    private int partiteVinte;
    private List<Carta> Mano;
    private Strategia strategiaGiocata;

    public Giocatore(String ruolo, String nome, int punteggio, int partiteVinte, List<Carta> Mano) {
        this.ruolo = ruolo;
        this.nome = nome;
        this.punteggio = 0.0;
        this.partiteVinte = 0;
        this.Mano = new ArrayList<Carta>();
        if (ruolo.toLowerCase().equals("bot")) {
            this.strategiaGiocata = new StrategiaBot();
        } else if (ruolo.toLowerCase().equals("user")) {
            this.strategiaGiocata = new StrategiaUmano();
        } else {
            System.err.println("Attenzione: Ruolo giocatore sconosciuto: " + ruolo + ". Nessuna strategia assegnata.");
        }
    }

    public String getNome() {
        return nome;
    }

    public int getPartiteVinte() {return partiteVinte;}
    public double getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(double punteggio) {
        this.punteggio = punteggio;
    }

    public void addPunti(double punti) {
        this.punteggio += punti;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void resetMano() {
        this.Mano.clear();
    }

    public List<Carta> getCarte() {
        return Mano;
    }

    public Strategia getStrategiaGiocata() {
        return strategiaGiocata;
    }

    public List<Carta> addCarta(Carta carta) {
        Mano.add(carta);
        return Mano;
    }

    public void addPartiteVinte() {
        partiteVinte++;
    }
}
