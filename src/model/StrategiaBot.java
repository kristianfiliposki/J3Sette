package model;

import java.util.List;

public class StrategiaBot implements Strategia {
    @Override
    public void scarta(List<Carta> manoGiocatore, CartaBanco carta) {
        manoGiocatore.remove(manoGiocatore.get(0));
    }
}
