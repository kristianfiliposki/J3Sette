package model;

/**
 * Interfaccia che definisce un osservatore nel pattern Observer.
 * Le classi che implementano questa interfaccia possono essere registrate
 * come osservatori di un {@link Observable} e ricevere notifiche di aggiornamento.
 */
public interface Observer {
    /**
     * Metodo chiamato dall'oggetto {@link Observable} quando lo stato cambia.
     *
     * @param observable L'oggetto osservabile che ha generato l'evento.
     * @param arg        Argomento opzionale passato dall'osservabile (può essere null).
     */
    void update(Observable observable, Object arg);
}
