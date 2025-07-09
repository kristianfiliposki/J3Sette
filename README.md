
# J3SETTE – Gioco di Carte Tressette

**Autore:** Kristian Filiposki  
**Matricola:** 2193452  
**Email:** filiposki.2193452@studenti.uniroma1.it  
**Data:** Mercoledì 9 luglio

---

## Introduzione

### Obiettivo del Progetto
Realizzare un’applicazione desktop in Java che simuli il gioco di carte italiano **Tressette**, con interfaccia grafica, gestione di più giocatori (umani e bot) e logica di gioco completa e fedele alle regole ufficiali.

### Descrizione Generale
Il progetto implementa il gioco di carte Tressette utilizzando il paradigma orientato agli oggetti in Java.  
L’architettura segue il pattern **MVC** (Model-View-Controller):  
- **Model:** gestisce lo stato del gioco e le regole.  
- **View:** interfaccia grafica Swing per visualizzare lo stato e ricevere input.  
- **Controller:** coordina la logica di gioco e media tra model e view.

Sono previsti giocatori umani e bot con strategie diverse, gestione dei turni, controllo delle prese e punteggi, e possibilità di riavviare la partita.

---

## Documentazione Funzionale

### Scopo
Descrivere in dettaglio le funzionalità offerte dall’applicazione, i requisiti funzionali e i principali casi d’uso per garantire una chiara comprensione del comportamento atteso dal sistema.

### Funzionalità Principali

1. **Configurazione iniziale partita**  
   L’utente inserisce il numero di giocatori (2-4) e il nome del giocatore principale tramite un form di input. Il sistema crea automaticamente i giocatori bot con nomi predefiniti e assegna i ruoli.

2. **Distribuzione delle carte**  
   Il mazzo viene creato e mescolato. Le carte vengono distribuite equamente ai giocatori in base al numero totale di partecipanti.

3. **Gestione del turno**  
   Il sistema indica quale giocatore è di turno. Il giocatore umano può selezionare una carta giocabile dalla propria mano. I bot giocano automaticamente seguendo una strategia semplice.

4. **Giocata di una carta**  
   La carta selezionata viene rimossa dalla mano del giocatore e aggiunta allo stato della mano (banco). Viene notificata la vista per aggiornare la UI.

5. **Controllo della presa**  
   Dopo che tutti i giocatori hanno giocato una carta, il sistema determina il vincitore della presa basandosi sul seme dominante e sulla forza delle carte. I punti della presa vengono calcolati e assegnati al giocatore vincitore. Il turno successivo inizia con il vincitore della presa.

6. **Fine della partita**  
   Quando tutte le carte sono state giocate, il sistema calcola il punteggio totale di ogni giocatore, determina e mostra il vincitore della partita. Le statistiche di vittorie vengono aggiornate.

7. **Riavvio della partita**  
   L’utente può riavviare la partita in qualsiasi momento tramite un pulsante dedicato. Il sistema resetta lo stato del gioco, azzera punteggi e carte, e ricomincia una nuova partita.

### Requisiti Funzionali Dettagliati

| Codice | Descrizione                                                                                  |
|--------|----------------------------------------------------------------------------------------------|
| RF1    | Supportare da 2 a 4 giocatori.                                                               |
| RF2    | Distinguere tra giocatori umani e bot.                                                      |
| RF3    | Visualizzare informazioni dettagliate per ogni giocatore: nome, punti, vittorie e carte in mano. |
| RF4    | Impedire al giocatore umano di giocare carte fuori turno o non valide.                       |
| RF5    | Mostrare le carte giocate sul tavolo con indicazione del giocatore che le ha giocate.       |
| RF6    | Notificare visivamente il giocatore di turno.                                               |
| RF7    | Consentire il riavvio della partita con conferma da parte dell’utente.                       |
| RF8    | Gestire correttamente situazioni di errore, come input non validi o stato inconsistente.     |

### Casi d’Uso Principali

| Caso d’uso        | Attore   | Descrizione                                                                                  |
|-------------------|----------|----------------------------------------------------------------------------------------------|
| Inizializza partita | Utente   | Inserisce numero giocatori e nome, sistema crea giocatori e distribuisce le carte.           |
| Gioca carta        | Giocatore| Seleziona una carta valida e la gioca, sistema aggiorna stato e passa al turno successivo.  |
| Gioca carta bot    | Bot      | Il bot sceglie automaticamente una carta da giocare secondo la strategia implementata.       |
| Controlla presa    | Sistema  | Determina il vincitore della presa e aggiorna i punteggi.                                   |
| Visualizza info    | Utente   | Visualizza informazioni aggiornate su giocatori e stato del gioco.                           |
| Riavvia partita    | Utente   | Richiede conferma e resetta lo stato per iniziare una nuova partita.                         |

---

## Documentazione Tecnica

### Architettura del Sistema
L’applicazione segue il pattern **MVC**:

- **Model:** rappresenta lo stato del gioco e le regole.
- **View:** interfaccia grafica Swing per visualizzare lo stato e ricevere input.
- **Controller:** coordina la logica di gioco e media tra model e view.

### Componenti Principali

- **GameController:** gestisce i turni, le giocate, il controllo delle prese e la fine partita.  
- **Tavolo:** modello centrale che contiene giocatori, mazzo e stato della mano.  
- **Giocatore:** rappresenta un giocatore con ruolo, mano di carte, punteggio e strategia.  
- **Carta e CartaBanco:** rappresentano le carte di gioco, con CartaBanco che aggiunge il nome del giocatore che ha giocato la carta.  
- **Mazzo:** gestisce la creazione, mescolamento e distribuzione delle carte.  
- **StatoMano:** mantiene le carte giocate sul banco e determina il vincitore della presa.  
- **Strategia:** interfaccia per definire la logica di scarto delle carte, implementata da bot e umano.  
- **View:** pannelli Swing come CardTablePanel, PlayerInfoPanel e DeckPanel per la UI.

### Tecnologie e Librerie

- Linguaggio: Java 8+  
- GUI: Swing  
- Audio: gestione tramite AudioManager personalizzato  
- Pattern: Observer per aggiornamento vista, MVC per separazione logica e UI  

### Ciclo di Vita del Gioco

- Inizializzazione con creazione giocatori e mazzo.  
- Distribuzione carte e inizio turno.  
- Alternanza turni con giocate e aggiornamenti.  
- Controllo prese e aggiornamento punteggi.  
- Fine partita e possibilità di riavvio.

### Note di Implementazione

- Uso di Timer Swing per gestire ritardi e animazioni.  
- Strategie di gioco semplici per bot (gioca prima carta disponibile).  
- Javadoc completo per tutte le classi principali.  
- Gestione errori e validazioni per input e stato di gioco.

---

**Kristian Filiposki**  
