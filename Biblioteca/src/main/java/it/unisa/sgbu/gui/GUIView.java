/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import it.unisa.sgbu.domain.*;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node; // NECESSARIO PER TROVARE I PULSANTI
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

/**
 * @brief Classe Boundary che gestisce l'interfaccia utente (View).
 */
public class GUIView {
    
    private final GUIController sistema;
    private final MessaggiInterfaccia messaggi;
    private Stage primaryStage;
    private ObservableList<String> datiAuditTrail;
    
    private DashboardController dashboardController;

    public GUIView(GUIController sistema, Stage primaryStage){
        this.sistema = sistema;
        this.messaggi = new MessaggiInterfaccia();
        this.primaryStage = primaryStage;
    }
    
    public void avviaInterfaccia(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardView.fxml")); 
            Parent root = loader.load();
            
            this.dashboardController = loader.getController();
            this.dashboardController.setSistema(this.sistema, this);
            
            mostraListaLibri(sistema.ottieniCatalogoOrdinato());
            mostraListaUtenti(sistema.ottieniAnagraficaOrdinata());
            mostraListaPrestiti(sistema.ottieniReportPrestiti());
            mostraListaAuditTrail(sistema.ottieniAuditTrail()); // Passiamo la lista viva
            
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("SGBU - Biblioteca Dashboard");
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostraMessaggio(MessaggiInterfaccia.AVVISO_CARICAMENTO_FALLITO);
        }
    }

    // =========================================================================
    // METODO HELPER PER AGGIUNGERE ICONE AI PULSANTI DEI POPUP
    // =========================================================================
    private void stilaPulsantiDialogo(Dialog<?> dialog) {
        DialogPane pane = dialog.getDialogPane();
        
        // Cerca il pulsante OK e aggiunge l'icona
        Node btnOk = pane.lookupButton(ButtonType.OK);
        if (btnOk instanceof Button) {
            ((Button) btnOk).setText("Conferma");
            ((Button) btnOk).setStyle("-fx-base: #2ecc71; -fx-text-fill: black; -fx-font-weight: bold;"); // Verde
            ((Button) btnOk).setGraphic(new Label("✅")); 
        }
        
        // Cerca il pulsante CANCEL e aggiunge l'icona
        Node btnCancel = pane.lookupButton(ButtonType.CANCEL);
        if (btnCancel instanceof Button) {
            ((Button) btnCancel).setText("Annulla");
            ((Button) btnCancel).setStyle("-fx-base: #e74c3c; -fx-text-fill: black;"); // Rosso
            ((Button) btnCancel).setGraphic(new Label("❌"));
        }
    }
    
    // =========================================================================
    // GESTIONE DIALOGHI
    // =========================================================================

    public void gestisciAggiuntaLibro(){
        Dialog<Libro> dialog = new Dialog<>();
        dialog.setTitle("Aggiungi Libro");
        dialog.setHeaderText("📖 Nuovo Libro");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField isbn = new TextField(); isbn.setPromptText("ISBN");
        TextField titolo = new TextField(); titolo.setPromptText("Titolo");
        TextField autore = new TextField(); autore.setPromptText("Autori (separati da virgola)");
        TextField anno = new TextField(); anno.setPromptText("Anno");
        TextField copie = new TextField(); copie.setPromptText("Copie");

        grid.add(new Label("ISBN:"), 0, 0); grid.add(isbn, 1, 0);
        grid.add(new Label("Titolo:"), 0, 1); grid.add(titolo, 1, 1);
        grid.add(new Label("Autore:"), 0, 2); grid.add(autore, 1, 2);
        grid.add(new Label("Anno:"), 0, 3); grid.add(anno, 1, 3);
        grid.add(new Label("Copie:"), 0, 4); grid.add(copie, 1, 4);

        dialog.getDialogPane().setContent(grid);
        stilaPulsantiDialogo(dialog); // APPLICA STILE

        dialog.setResultConverter(Button -> {
            if (Button == ButtonType.OK) {
                try {
                    List<String> autori = Arrays.asList(autore.getText().split(","));
                    return new Libro(isbn.getText(), titolo.getText(), autori, Integer.parseInt(anno.getText()), Integer.parseInt(copie.getText()));
                } catch (Exception e) { return null; }
            }
            return null;
        });

        Optional<Libro> result = dialog.showAndWait();
        result.ifPresent(l -> {
            if (!l.getISBN().matches("^[0-9-]{13,17}$")) {
                mostraMessaggio("Errore: Formato ISBN non valido.");
                return;
            }
            if (l.getAnno() < 0 || l.getAnno() > java.time.Year.now().getValue()) {
                mostraMessaggio(MessaggiInterfaccia.INPUT_ANNO_NON_VALIDO);
                return;
            }

            if (sistema.aggiungiLibro(l)) {
                mostraListaLibri(sistema.ottieniCatalogoOrdinato());
                mostraMessaggio(String.format(MessaggiInterfaccia.SUCCESSO_AGGIUNTA_LIBRO, l.getTitolo()));
            } else {
                mostraMessaggio(String.format(MessaggiInterfaccia.ERRORE_ISBN_DUPLICATO, l.getISBN(), "presente nel catalogo"));
            }
        });
    }
    
    public void gestisciAggiuntaUtente(){
        Dialog<Utente> dialog = new Dialog<>();
        dialog.setTitle("Nuovo Utente");
        dialog.setHeaderText("👤 Dati Anagrafici");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        
        TextField matricola = new TextField(); matricola.setPromptText("Matricola");
        TextField nome = new TextField(); nome.setPromptText("Nome");
        TextField cognome = new TextField(); cognome.setPromptText("Cognome");
        TextField email = new TextField(); email.setPromptText("Email");

        grid.add(new Label("Matricola:"), 0, 0); grid.add(matricola, 1, 0);
        grid.add(new Label("Nome:"), 0, 1); grid.add(nome, 1, 1);
        grid.add(new Label("Cognome:"), 0, 2); grid.add(cognome, 1, 2);
        grid.add(new Label("Email:"), 0, 3); grid.add(email, 1, 3);

        dialog.getDialogPane().setContent(grid);
        stilaPulsantiDialogo(dialog); // APPLICA STILE

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) return new Utente(matricola.getText(), nome.getText(), cognome.getText(), email.getText());
            return null;
        });

        Optional<Utente> res = dialog.showAndWait();
        res.ifPresent(u -> {
            // Controllo Matricola
            if (!u.getMatricola().matches("^\\d{10}$")) {
                mostraMessaggio(MessaggiInterfaccia.INPUT_MATRICOLA_NON_VALIDO);
                return;
            }

            // --- CORREZIONE REGEX EMAIL ---
            // Questa regex impone che dopo ogni punto ci sia del testo, vietando ".."
            String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,}$";
            
            if (!u.getEmail().matches(regexEmail)) {
                mostraMessaggio(MessaggiInterfaccia.INPUT_EMAIL_NON_VALIDO + 
                                "\n(Formato non valido o presenza di doppi punti '..')");
                return;
            }
            // ------------------------------

            // Controllo Nome
            if (!u.getNome().matches("^[a-zA-Z\\s']+$")) {
                mostraMessaggio(MessaggiInterfaccia.INPUT_NOME_NON_VALIDO);
                return;
            }
            // Controllo Cognome
            if (!u.getCognome().matches("^[a-zA-Z\\s']+$")) {
                mostraMessaggio(MessaggiInterfaccia.INPUT_COGNOME_NON_VALIDO);
                return;
            }

            if (sistema.aggiungiUtente(u)) {
                mostraListaUtenti(sistema.ottieniAnagraficaOrdinata());
                mostraMessaggio("Utente registrato con successo."); 
            } else {
                mostraMessaggio(String.format(MessaggiInterfaccia.ERRORE_MATRICOLA_DUPLICATA, u.getMatricola()));
            }
        });
    }
    
    public void gestisciModificaLibro(){
        Libro libroSelezionato = dashboardController.getLibroSelezionato();
        
        if (libroSelezionato == null) {
            mostraMessaggio("Seleziona prima un libro dalla tabella!");
            return;
        }
        
        String isbnOriginale = libroSelezionato.getISBN();

        Dialog<Libro> dialog = new Dialog<>();
        dialog.setTitle("Modifica Libro");
        dialog.setHeaderText("✏️ Modifica i dati del libro: " + libroSelezionato.getTitolo());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField isbn = new TextField(libroSelezionato.getISBN());
        TextField titolo = new TextField(libroSelezionato.getTitolo());
        
        String autoriString = String.join(",", libroSelezionato.getAutore());
        TextField autore = new TextField(autoriString);
        
        TextField anno = new TextField(String.valueOf(libroSelezionato.getAnno()));
        TextField copie = new TextField(String.valueOf(libroSelezionato.getCopieTotali()));

        grid.add(new Label("ISBN:"), 0, 0); grid.add(isbn, 1, 0);
        grid.add(new Label("Titolo:"), 0, 1); grid.add(titolo, 1, 1);
        grid.add(new Label("Autore:"), 0, 2); grid.add(autore, 1, 2);
        grid.add(new Label("Anno:"), 0, 3); grid.add(anno, 1, 3);
        grid.add(new Label("Copie Totali:"), 0, 4); grid.add(copie, 1, 4);

        dialog.getDialogPane().setContent(grid);
        stilaPulsantiDialogo(dialog); // APPLICA STILE

        dialog.setResultConverter(Button -> {
            if (Button == ButtonType.OK) {
                try {
                    List<String> autoriList = Arrays.asList(autore.getText().split(","));
                    return new Libro(isbn.getText(), titolo.getText(), autoriList, Integer.parseInt(anno.getText()), Integer.parseInt(copie.getText()));
                } catch (Exception e) { return null; }
            }
            return null;
        });

        Optional<Libro> result = dialog.showAndWait();
        result.ifPresent(lNuovo -> {
            if (!lNuovo.getISBN().matches("^[0-9-]{13,17}$")) {
                mostraMessaggio("Errore: Formato ISBN non valido.");
                return;
            }
            if (lNuovo.getAnno() < 0 || lNuovo.getAnno() > java.time.Year.now().getValue()) {
                mostraMessaggio(MessaggiInterfaccia.INPUT_ANNO_NON_VALIDO);
                return;
            }

            if (sistema.modificaLibro(isbnOriginale, lNuovo)) {
                mostraListaLibri(sistema.ottieniCatalogoOrdinato());
                mostraMessaggio("Libro modificato con successo.");
            } else {
                mostraMessaggio("Modifica fallita.\nCause possibili:\n1. ISBN duplicato.\n2. Il libro ha prestiti attivi (Impossibile cambiare ISBN).");
            }
        });
    }
    
    public void gestisciModificaUtente(){
        Utente utenteSelezionato = dashboardController.getUtenteSelezionato();
        
        if (utenteSelezionato == null) {
            mostraMessaggio("Seleziona prima un utente dalla tabella!");
            return;
        }
        
        String matricolaOriginale = utenteSelezionato.getMatricola();

        Dialog<Utente> dialog = new Dialog<>();
        dialog.setTitle("Modifica Utente");
        dialog.setHeaderText("✏️ Modifica dati utente: " + utenteSelezionato.getCognome());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        
        TextField matricola = new TextField(utenteSelezionato.getMatricola());
        TextField nome = new TextField(utenteSelezionato.getNome());
        TextField cognome = new TextField(utenteSelezionato.getCognome());
        TextField email = new TextField(utenteSelezionato.getEmail());

        grid.add(new Label("Matricola:"), 0, 0); grid.add(matricola, 1, 0);
        grid.add(new Label("Nome:"), 0, 1); grid.add(nome, 1, 1);
        grid.add(new Label("Cognome:"), 0, 2); grid.add(cognome, 1, 2);
        grid.add(new Label("Email:"), 0, 3); grid.add(email, 1, 3);

        dialog.getDialogPane().setContent(grid);
        stilaPulsantiDialogo(dialog); // APPLICA STILE

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) return new Utente(matricola.getText(), nome.getText(), cognome.getText(), email.getText());
            return null;
        });

        Optional<Utente> res = dialog.showAndWait();
        res.ifPresent(uNuovo -> {
            if (!uNuovo.getMatricola().matches("^\\d{10}$")) {
                mostraMessaggio(MessaggiInterfaccia.INPUT_MATRICOLA_NON_VALIDO);
                return;
            }
            if (!uNuovo.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                mostraMessaggio(MessaggiInterfaccia.INPUT_EMAIL_NON_VALIDO);
                return;
            }
            
            if(sistema.modificaUtente(matricolaOriginale, uNuovo)){
                mostraListaUtenti(sistema.ottieniAnagraficaOrdinata());
                mostraMessaggio("Utente modificato con successo.");
            } else {
                mostraMessaggio("Modifica fallita.\nCause possibili:\n1. Matricola duplicata.\n2. L'utente ha prestiti attivi (Impossibile cambiare Matricola).");
            }
        });
    }
    
    public void gestisciEliminazioneUtente(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Elimina Utente");
        dialog.setHeaderText("🗑️ Inserisci Matricola per confermare");
        
        stilaPulsantiDialogo(dialog); // APPLICA STILE

        Utente sel = dashboardController.getUtenteSelezionato();
        if(sel != null) dialog.getEditor().setText(sel.getMatricola());

        Optional<String> res = dialog.showAndWait();
        res.ifPresent(matr -> {
            if(sistema.rimuoviUtente(matr)){
                mostraListaUtenti(sistema.ottieniAnagraficaOrdinata());
                mostraMessaggio("Utente rimosso.");
            } else {
                mostraMessaggio(MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO);
            }
        });
    }
    
    public void gestisciEliminazioneLibro(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Elimina Libro");
        dialog.setHeaderText("🗑️ Inserisci ISBN per confermare");
        
        stilaPulsantiDialogo(dialog); // APPLICA STILE
        
        Libro sel = dashboardController.getLibroSelezionato();
        if(sel != null) dialog.getEditor().setText(sel.getISBN());

        Optional<String> res = dialog.showAndWait();
        res.ifPresent(isbn -> {
            if(sistema.rimuoviLibro(isbn)){
                mostraListaLibri(sistema.ottieniCatalogoOrdinato());
                mostraMessaggio("Libro rimosso.");
            } else {
                mostraMessaggio(MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO);
            }
        });
    }
    
   public void gestisciRegistrazionePrestito(){
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Nuovo Prestito");
        dialog.setHeaderText("📚 Registra Prestito");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        
        TextField isbn = new TextField(); isbn.setPromptText("ISBN Libro");
        TextField matr = new TextField(); matr.setPromptText("Matricola Utente");
        DatePicker data = new DatePicker(LocalDate.now().plusDays(30));
        
        grid.add(new Label("ISBN:"), 0, 0); grid.add(isbn, 1, 0);
        grid.add(new Label("Matricola:"), 0, 1); grid.add(matr, 1, 1);
        grid.add(new Label("Scadenza:"), 0, 2); grid.add(data, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        stilaPulsantiDialogo(dialog); // APPLICA STILE
        
        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                // Validazione formati base
                if (!isbn.getText().matches("^[0-9-]{13,17}$")) {
                    // Nota: Non mostriamo messaggi qui per evitare conflitti col thread grafico, 
                    // ritorniamo null per indicare input non valido se vogliamo bloccare, 
                    // oppure gestiamo l'errore nel controller. 
                    // Per semplicità qui ritorniamo false se il formato è errato.
                    return false; 
                }
                if (!matr.getText().matches("^\\d{10}$")) {
                    return false;
                }
                // Prova a registrare il prestito
                return sistema.gestisciPrestito(isbn.getText(), matr.getText(), data.getValue());
            }
            return null; // Se preme Annulla
        });
        
        Optional<Boolean> esito = dialog.showAndWait();
        
        // --- LOGICA CORRETTA ---
        if (esito.isPresent()) {
            if (esito.get()) {
                // CASO SUCCESSO (True)
                mostraListaPrestiti(sistema.ottieniReportPrestiti());
                mostraListaLibri(sistema.ottieniCatalogoOrdinato());
                String msg = String.format(MessaggiInterfaccia.SUCCESSO_PRESTITO, 0);
                mostraMessaggio(new MessaggiInterfaccia() { @Override public String toString() { return msg; }});
            } else {
                // CASO ERRORE (False) - Questo è quello che mancava!
                // L'utente ha premuto OK, ma il sistema ha detto "NO" (libro non disp, utente pieno, o input errato)
                mostraMessaggio("❌ Errore Registrazione Prestito:\n"
                        + "L'operazione è fallita. Verifica che:\n"
                        + "1. Il libro esista e ci siano copie disponibili.\n"
                        + "2. L'utente esista e non abbia superato il limite prestiti.\n"
                        + "3. I formati (ISBN/Matricola) siano corretti.");
            }
        }
        // Se esito non è presente, l'utente ha premuto Annulla o chiuso la finestra (non facciamo nulla).
    }
   
   
    public void gestisciRestituzionePrestito(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Restituzione");
        dialog.setHeaderText("↩️ Inserisci ID Prestito");
        
        stilaPulsantiDialogo(dialog); // APPLICA STILE
        
        // Pre-compilazione se selezionato
        Prestito pSel = dashboardController.getPrestitoSelezionato();
        if(pSel != null) dialog.getEditor().setText(String.valueOf(pSel.getIdPrestito()));
        
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                if(sistema.gestisciRestituzione(id, LocalDate.now())){
                    mostraListaPrestiti(sistema.ottieniReportPrestiti());
                    mostraListaLibri(sistema.ottieniCatalogoOrdinato());
                    mostraMessaggio("Restituzione effettuata.");
                } else {
                    mostraMessaggio(MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO);
                }
            } catch(NumberFormatException e) {
                mostraMessaggio(MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO);
            }
        });
    }
    
    public void gestisciRicercaLibro(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Ricerca Libri");
        dialog.setHeaderText("🔍 Cerca nel Catalogo");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> comboCriterio = new ComboBox<>();
        comboCriterio.getItems().addAll("Titolo", "Autore", "ISBN");
        comboCriterio.setValue("Titolo"); 

        TextField txtQuery = new TextField();
        txtQuery.setPromptText("Cosa vuoi cercare?");

        grid.add(new Label("Cerca per:"), 0, 0);
        grid.add(comboCriterio, 1, 0);
        grid.add(new Label("Testo:"), 0, 1);
        grid.add(txtQuery, 1, 1);

        dialog.getDialogPane().setContent(grid);
        stilaPulsantiDialogo(dialog); // APPLICA STILE

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(comboCriterio.getValue(), txtQuery.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        
        result.ifPresent(pair -> {
            String criterio = pair.getKey();
            String testo = pair.getValue();
            
            List<Libro> risultati = sistema.cercaLibro(testo, criterio);
            
            mostraListaLibri(risultati);
            
            if (risultati.isEmpty()) {
                mostraMessaggio("Nessun libro trovato con " + criterio + ": " + testo);
            }
        });
    }
    
    public void gestisciRicercaUtente(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Ricerca Utenti");
        dialog.setHeaderText("🔍 Cerca nell'Anagrafica");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> comboCriterio = new ComboBox<>();
        comboCriterio.getItems().addAll("Cognome", "Matricola");
        comboCriterio.setValue("Cognome"); 

        TextField txtQuery = new TextField();
        txtQuery.setPromptText("Cosa vuoi cercare?");

        grid.add(new Label("Cerca per:"), 0, 0);
        grid.add(comboCriterio, 1, 0);
        grid.add(new Label("Testo:"), 0, 1);
        grid.add(txtQuery, 1, 1);

        dialog.getDialogPane().setContent(grid);
        stilaPulsantiDialogo(dialog); // APPLICA STILE

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(comboCriterio.getValue(), txtQuery.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        
        result.ifPresent(pair -> {
            String criterio = pair.getKey();
            String testo = pair.getValue();
            
            List<Utente> risultati = sistema.cercaUtente(testo, criterio);
            
            mostraListaUtenti(risultati);
            
            if (risultati.isEmpty()) {
                mostraMessaggio("Nessun utente trovato con " + criterio + ": " + testo);
            }
        });
    }
    
    public void mostraListaLibri(List<Libro> Lista){
        if (dashboardController != null && dashboardController.getTableLibri() != null)
            dashboardController.getTableLibri().getItems().setAll(Lista);
    }
    
    public void mostraListaUtenti(List<Utente> Lista){
        if (dashboardController != null && dashboardController.getTableUtenti() != null)
            dashboardController.getTableUtenti().getItems().setAll(Lista);
    }
    
    public void mostraListaPrestiti(List<Prestito> Lista){
        if (dashboardController != null && dashboardController.getTablePrestiti() != null)
            dashboardController.getTablePrestiti().getItems().setAll(Lista);
    }
    
    public void mostraListaAuditTrail(List<String> Lista) {
        if (Lista == null) {
            Lista = new ArrayList<>(); 
        }
        // Utilizziamo direttamente la lista osservabile dal controller per sincronizzazione
        if (dashboardController != null && dashboardController.getTableAuditTrail() != null) {
            dashboardController.getTableAuditTrail().setItems(sistema.ottieniAuditTrail());
        }
    }
    
    public void mostraReportUtente(Utente u){
        mostraMessaggio("Report: " + u.toString());
    }
    
    public void mostraMessaggio(Object msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SGBU Info");
        alert.setHeaderText(null);
        alert.setContentText(msg.toString());
        stilaPulsantiDialogo(alert);
        alert.showAndWait();
    }
    
    public void mostraFinestraLogin(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml")); 
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller.setSistema(this.sistema, this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("SGBU - Login");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void mostraLogDiSistema(List<String> log){
        TextArea area = new TextArea();
        for(String s : log) area.appendText(s + "\n");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Log");
        alert.getDialogPane().setContent(area);
        stilaPulsantiDialogo(alert);
        alert.showAndWait();
    }
}