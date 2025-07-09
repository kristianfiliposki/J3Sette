package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che rappresenta un oggetto osservabile nel pattern Observer.
 * Mantiene una lista di osservatori ({@link Observer}) e fornisce metodi per
 * aggiungere, rimuovere e notificare gli osservatori quando lo stato cambia.
 */
public abstract class Observable {
    private List<Observer> observers = new ArrayList<>();

    /**
     * Aggiunge un osservatore alla lista degli osservatori se non è già presente.
     *
     * @param observer L'osservatore da aggiungere.
     */
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Rimuove un osservatore dalla lista degli osservatori.
     *
     * @param observer L'osservatore da rimuovere.
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifica tutti gli osservatori senza argomenti aggiuntivi.
     * Viene chiamato il metodo {@link Observer#update(Observable, Object)} con argomento null.
     */
    protected void notifyObservers() {
        notifyObservers(null);
    }

    /**
     * Notifica tutti gli osservatori passando un argomento opzionale.
     * Viene chiamato il metodo {@link Observer#update(Observable, Object)} per ogni osservatore.
     *
     * @param arg Argomento opzionale da passare agli osservatori.
     */
    protected void notifyObservers(Object arg) {
        for (Observer observer : observers) {
            observer.update(this, arg);
        }
    }
}
