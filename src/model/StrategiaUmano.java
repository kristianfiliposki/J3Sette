package model;

import java.util.List;

public class StrategiaUmano implements Strategia {
    @Override
    public void scarta(List<Carta> manoGiocatore, CartaBanco carta) {
        manoGiocatore.removeIf(c ->
                c.getValore() == carta.getValore() &&
                        c.getSeme().equals(carta.getSeme())
        );
    }
}
