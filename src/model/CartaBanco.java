package model;

public class CartaBanco extends Carta {
    private String NomeTiratore;

    public CartaBanco(String NomeTiratore, int valore, String seme, int forza, String immagine) {
        super(valore, seme, forza, immagine);
        this.NomeTiratore = NomeTiratore;
    }

    public String getNomeTiratore() {
        return NomeTiratore;
    }
}
