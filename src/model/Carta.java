package model;

/**
 * Rappresenta una carta da gioco con valore, seme, forza e immagine associata.
 * Include metodi per ottenere i punti secondo la variante Tressette.
 */
public class Carta {
    private int valore;
    private String seme;
    private int forza;
    private String immagine;

    /**
     * Costruisce una carta con i parametri specificati.
     * L'immagine viene impostata automaticamente in base al valore e al seme.
     *
     * @param valore  Il valore numerico della carta (es. 1, 2, ..., 10).
     * @param seme    Il seme della carta (es. "denari", "coppe", "spade", "bastoni").
     * @param forza   La forza della carta usata per confronti di presa.
     * @param immagine (Non usato direttamente, l'immagine viene costruita internamente).
     */
    public Carta(int valore, String seme, int forza, String immagine) {
        this.valore = valore;
        this.seme = seme;
        this.forza = forza;
        // Path originale dell'immagine costruito con valore e seme (prima lettera maiuscola)
        char semeChar = Character.toUpperCase(seme.charAt(0));
        this.immagine = "/assets/carte/" + valore + semeChar + ".jpeg";
    }

    /**
     * Restituisce il valore numerico della carta.
     *
     * @return Il valore della carta.
     */
    public int getValore() {
        return valore;
    }

    /**
     * Restituisce il seme della carta.
     *
     * @return Il seme della carta.
     */
    public String getSeme() {
        return seme;
    }

    /**
     * Restituisce la forza della carta, usata per determinare la presa.
     *
     * @return La forza della carta.
     */
    public int getForza() {
        return forza;
    }

    /**
     * Restituisce il percorso dell'immagine associata alla carta.
     *
     * @return Il path dell'immagine della carta.
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * Calcola i punti della carta secondo le regole del gioco Tressette.
     * Valori 1 e 3 valgono 1 punto, 8, 9 e 10 valgono 1/3 di punto, gli altri 0.
     *
     * @return I punti della carta nel gioco Tressette.
     */
    public double getPuntiTressette() {
        switch (valore) {
            case 1:
            case 3:
                return 1.0;
            case 8:
            case 9:
            case 10:
                return 1.0 / 3.0;
            default:
                return 0.0;
        }
    }

    /**
     * Confronta questa carta con un altro oggetto per verificarne l'uguaglianza.
     * Due carte sono uguali se hanno lo stesso valore e lo stesso seme.
     *
     * @param obj L'oggetto da confrontare.
     * @return true se le carte sono uguali, false altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Carta altra = (Carta) obj;
        return valore == altra.valore && seme.equals(altra.seme);
    }

    /**
     * Calcola l'hash code della carta basato su valore e seme.
     *
     * @return L'hash code della carta.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + valore;
        result = prime * result + ((seme == null) ? 0 : seme.hashCode());
        return result;
    }
}
