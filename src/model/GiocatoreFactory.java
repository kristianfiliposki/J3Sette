package model;

public class GiocatoreFactory {
    public static Giocatore creaGiocatore(String ruolo, String nome) {
        return new Giocatore(ruolo, nome, 0, 0, null);
    }

    public static Giocatore creaGiocatoreUmano(String nome) {
        return new Giocatore("user", nome, 0, 0, null);
    }

    public static Giocatore creaGiocatoreBot(String nome) {
        return new Giocatore("bot", nome, 0, 0, null);
    }
}
