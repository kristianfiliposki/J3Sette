package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Finestra interna (JInternalFrame) che mostra un form di input per
 * inserire il numero di giocatori e il nome del giocatore principale.
 * Fornisce un metodo per aprire il dialog e restituire i dati inseriti.
 */
public class OpenInputForm extends JInternalFrame {

    /**
     * Costruisce la finestra interna con titolo, dimensioni e proprietà di ridimensionamento.
     */
    public OpenInputForm() {
        super("Input Form", true, true, true, true);
        setSize(400, 300);
        setVisible(true);
    }

    /**
     * Mostra un dialog modale per l'inserimento del numero di giocatori e del nome principale.
     * Valida i dati inseriti e restituisce i valori se confermati.
     *
     * @return Una lista di stringhe contenente il numero di giocatori e il nome principale,
     *         oppure una lista vuota se l'utente annulla.
     */
    public List<String> openInputForm() {
        List<String> result = new ArrayList<>();
        JDialog inputDialog = new JDialog((Frame) null, "Dati Giocatori", true);
        inputDialog.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel labelNumGiocatori = new JLabel("Numero Giocatori:");
        JTextField fieldNumGiocatori = new JTextField();

        JLabel labelNomeMain = new JLabel("Nome Giocatore Principale:");
        JTextField fieldNomeMain = new JTextField();

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Annulla");

        inputDialog.add(labelNumGiocatori);
        inputDialog.add(fieldNumGiocatori);
        inputDialog.add(labelNomeMain);
        inputDialog.add(fieldNomeMain);
        inputDialog.add(okButton);
        inputDialog.add(cancelButton);

        inputDialog.setSize(350, 150);
        inputDialog.setLocationRelativeTo(null);

        final boolean[] confirmed = {false};

        okButton.addActionListener(ev -> {
            String nomGiocatoriStr = fieldNumGiocatori.getText().trim();
            String nomeMain = fieldNomeMain.getText().trim();

            if (nomGiocatoriStr.isEmpty() || nomeMain.isEmpty()) {
                JOptionPane.showMessageDialog(inputDialog, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int nomGiocatori = Integer.parseInt(nomGiocatoriStr);
                if (nomGiocatori < 2 || nomGiocatori > 4) {
                    JOptionPane.showMessageDialog(inputDialog, "Numero giocatori deve essere tra 2 e 4.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                result.add(nomGiocatoriStr);
                result.add(nomeMain);
                confirmed[0] = true;
                inputDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(inputDialog, "Inserisci un numero valido per i giocatori.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(ev -> {
            confirmed[0] = false;
            inputDialog.dispose();
        });

        inputDialog.setVisible(true);

        if (confirmed[0]) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }
}
