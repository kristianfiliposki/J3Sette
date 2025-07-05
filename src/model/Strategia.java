package model;

import java.util.List;

public interface Strategia {
    void scarta(List<Carta> manoGiocatore, CartaBanco carta);
}
