package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StatoMano {
    private List<CartaBanco> carteSulBanco;
    private double punteggioPreseCorrenti; // Useremo un double per i punti della presa attuale

    public StatoMano() {
        punteggioPreseCorrenti = 0.0;
        carteSulBanco = new ArrayList<CartaBanco>();
    }

    public List<CartaBanco> getCarteSulBanco() {
        return carteSulBanco;
    }

    public void aggiungiCartaSulBanco(CartaBanco c) {
        if (c != null) {
            carteSulBanco.add(c);
        } else {
            System.err.println("Tentativo di aggiungere una carta nulla al banco.");
        }
    }

    public void reset() {
        this.punteggioPreseCorrenti = 0.0;
        this.carteSulBanco.clear(); // Usa clear() per riutilizzare la stessa lista
    }

    public void ControllaPresa(List<Giocatore> giocatori) {
        if (this.carteSulBanco.isEmpty()) {
            System.out.println("Il banco è vuoto, nessuna presa da controllare.");
            return;
        }

        CartaBanco primaCarta = this.carteSulBanco.get(0);
        String semePrimaCarta = primaCarta.getSeme();

        List<CartaBanco> carteStessoSeme = this.carteSulBanco.stream()
                .filter(carta -> carta.getSeme().equals(semePrimaCarta))
                .toList();

        Optional<CartaBanco> cartaVincenteOpt = carteStessoSeme.stream()
                .max(java.util.Comparator.comparingInt(Carta::getForza));

        if (cartaVincenteOpt.isPresent()) {
            CartaBanco cartaVincente = cartaVincenteOpt.get();

            // Calcolo punti Tressette
            double puntiPerQuestaPresa = this.carteSulBanco.stream()
                    .mapToDouble(Carta::getPuntiTressette)
                    .sum();

            String nomeVincitore = cartaVincente.getNomeTiratore();
            giocatori.stream()
                    .filter(g -> g.getNome().equalsIgnoreCase(nomeVincitore))
                    .findFirst()
                    .ifPresent(vincitoreDellaPresa -> {
                        vincitoreDellaPresa.addPunti(puntiPerQuestaPresa);
                        System.out.println("Giocatore " + vincitoreDellaPresa.getNome() +
                                " ha preso la mano con " +
                                String.format("%.2f", puntiPerQuestaPresa) +
                                " punti. Totale parziale: " +
                                String.format("%.2f", vincitoreDellaPresa.getPunteggio()));
                    });
        }
        reset();
    }

}
