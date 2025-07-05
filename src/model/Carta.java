package model;

public class Carta {

    private int valore;     // valore numerico (es. 1-10)
    private String seme;    // Denari, Coppe, Bastoni, Spade
    private int forza;      // forza relativa per confronto
    private String immagine; // path o nome immagine della carta

    // Costruttore
    public Carta(int valore, String seme, int forza, String immagine) {
        this.valore = valore;
        this.seme = seme;
        this.forza = forza;
        char semeChar = Character.toUpperCase(seme.charAt(0));
        // ATTENZIONE: il path immagine dovrebbe essere relative al classpath, non un path fisso
        this.immagine = "../resources/assets/carte/" + valore + semeChar + ".jpeg";
    }



    // Getters
    public int getValore() {
        return valore;
    }

    public String getSeme() {
        return seme;
    }

    public int getForza() {
        return forza;
    }

    public String getImmagine() {
        return immagine;
    }

        // ... codice esistente ...

        // NUOVO METODO PER IL PUNTEGGIO DEL TRESSETTE
        public double getPuntiTressette() {
            switch (valore) {
                case 1: // Asso
                case 3: // Tre
                    return 1.0;
                case 8: // Fante
                case 9: // Cavallo
                case 10: // Re
                    return 1.0 / 3.0;
                default:
                    return 0.0; // Tutte le altre carte non valgono punti
            }
        }

        // Aggiungi anche equals e hashCode se non li hai già
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Carta altra = (Carta) obj;
            return valore == altra.valore &&
                    seme.equals(altra.seme);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + valore;
            result = prime * result + ((seme == null) ? 0 : seme.hashCode());
            return result;
        }
    }


