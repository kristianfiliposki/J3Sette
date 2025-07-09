package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un giocatore nel gioco di carte.
 * Ogni giocatore ha un ruolo (es. "user" o "bot"), un nome, un punteggio accumulato,
 * il numero di partite vinte, una mano di carte e una strategia di gioco associata.
 */
public class Giocatore {
    private String ruolo;
    private String nome;
    private double punteggio;
    private int partiteVinte;
    private List<Carta> carte;
    private Strategia strategiaGiocata;

    /**
     * Costruisce un giocatore con ruolo, nome, punteggio iniziale, partite vinte e strategia di gioco.
     * La strategia viene assegnata automaticamente in base al ruolo:
     * "user" -> {@link StrategiaUmano}, "bot" -> {@link StrategiaBot}.
     *
     * @param ruolo          Il ruolo del giocatore ("user" o "bot").
     * @param nome           Il nome identificativo del giocatore.
     * @param punteggio      Il punteggio iniziale del giocatore.
     * @param partiteVinte   Il numero di partite vinte dal giocatore.
     * @param strategiaGiocata La strategia di gioco (non usata direttamente, sovrascritta in base al ruolo).
     */
    public Giocatore(String ruolo, String nome, double punteggio, int partiteVinte, Strategia strategiaGiocata) {
        this.ruolo = ruolo;
        this.nome = nome;
        this.punteggio = punteggio;
        this.partiteVinte = partiteVinte;
        this.carte = new ArrayList<>();

        // Assegna la strategia in base al ruolo
        if ("user".equals(ruolo)) {
            this.strategiaGiocata = new StrategiaUmano();
        } else if ("bot".equals(ruolo)) {
            this.strategiaGiocata = new StrategiaBot();
        }
    }

    /**
     * Restituisce il ruolo del giocatore.
     *
     * @return Il ruolo del giocatore ("user" o "bot").
     */
    public String getRuolo() {
        return ruolo;
    }

    /**
     * Restituisce il nome del giocatore.
     *
     * @return Il nome del giocatore.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il punteggio attuale del giocatore.
     *
     * @return Il punteggio accumulato.
     */
    public double getPunteggio() {
        return punteggio;
    }

    /**
     * Restituisce il numero di partite vinte dal giocatore.
     *
     * @return Il numero di partite vinte.
     */
    public int getPartiteVinte() {
        return partiteVinte;
    }

    /**
     * Restituisce la lista delle carte attualmente in mano al giocatore.
     *
     * @return La lista delle carte.
     */
    public List<Carta> getCarte() {
        return carte;
    }

    /**
     * Restituisce la strategia di gioco associata al giocatore.
     *
     * @return La strategia utilizzata dal giocatore.
     */
    public Strategia getStrategiaGiocata() {
        return strategiaGiocata;
    }

    /**
     * Imposta il punteggio del giocatore.
     *
     * @param punteggio Il nuovo punteggio da assegnare.
     */
    public void setPunteggio(double punteggio) {
        this.punteggio = punteggio;
    }

    /**
     * Incrementa di 1 il numero di partite vinte dal giocatore.
     */
    public void addPartiteVinte() {
        this.partiteVinte++;
    }

    /**
     * Aggiunge un certo numero di punti al punteggio attuale del giocatore.
     *
     * @param punti I punti da aggiungere.
     */
    public void addPunti(double punti) {
        this.punteggio += punti;
    }

    /**
     * Aggiunge una carta alla mano del giocatore.
     *
     * @param carta La carta da aggiungere.
     */
    public void addCarta(Carta carta) {
        this.carte.add(carta);
    }

    /**
     * Resetta la mano del giocatore svuotando la lista delle carte.
     */
    public void resetMano() {
        this.carte.clear();
    }

    /**
     * Verifica se questo giocatore è uguale ad un altro oggetto.
     * Due giocatori sono uguali se hanno lo stesso nome e ruolo.
     *
     * @param obj L'oggetto da confrontare.
     * @return true se i giocatori sono uguali, false altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Giocatore other = (Giocatore) obj;
        return nome.equals(other.nome) && ruolo.equals(other.ruolo);
    }

    /**
     * Calcola l'hash code del giocatore basandosi su nome e ruolo.
     *
     * @return L'hash code calcolato.
     */
    @Override
    public int hashCode() {
        return nome.hashCode() + ruolo.hashCode();
    }
}
