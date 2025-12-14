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
import javafx.scene.Node; 
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

/**
 * @brief Classe Boundary che gestisce l'interfaccia utente (View).
 */
public class GUIView {
    
    private final GUIController sistema;
    private final MessaggiInterfaccia messaggi; // Riferimento per coerenza
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
            mostraListaAuditTrail(sistema.ottieniAuditTrail()); 
            
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
        
        Node btnOk = pane.lookupButton(ButtonType.OK);
        if (btnOk instanceof Button) {
            ((Button) btnOk).setText("Conferma");
            ((Button) btnOk).setStyle("-fx-base: #2ecc71; -fx-text-fill: black; -fx-font-weight: bold;"); 
            ((Button) btnOk).setGraphic(new Label("✅")); 
        }
        
        Node btnCancel = pane.lookupButton(ButtonType.CANCEL);
        if (btnCancel instanceof Button) {
            ((Button) btnCancel).setText("Annulla");
            ((Button) btnCancel).setStyle("-fx-base: #e74c3c; -fx-text-fill: black;"); 
            ((Button) btnCancel).setGraphic(new Label("❌"));
        }
    }
    
    // =========================================================================
    // GESTIONE DIALOGHI (PULITI E DELEGATI)
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
        stilaPulsantiDialogo(dialog); 

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
            if (sistema.aggiungiLibro(l)) {
                mostraListaLibri(sistema.ottieniCatalogoOrdinato());
                mostraMessaggio(String.format(MessaggiInterfaccia.SUCCESSO_AGGIUNTA_LIBRO, l.getTitolo()));
            } else {
                // Messaggio generico di fallimento (copre ISBN errato, duplicati, ecc.)
                mostraMessaggio(MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO);
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
        stilaPulsantiDialogo(dialog); 

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) return new Utente(matricola.getText(), nome.getText(), cognome.getText(), email.getText());
            return null;
        });

        Optional<Utente> res = dialog.showAndWait();
        res.ifPresent(u -> {
            // DELEGA TOTALE: Il ValidatoreDati nel controller gestisce Regex Email e Matricola.
            if (sistema.aggiungiUtente(u)) {
                mostraListaUtenti(sistema.ottieniAnagraficaOrdinata());
                mostraMessaggio(MessaggiInterfaccia.SUCCESSO_AGGIUNTA_UTENTE); 
            } else {
                // Se fallisce, può essere un dato non valido o una matricola duplicata.
                // MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO è appropriato qui se vogliamo delegare tutto.
                // Oppure possiamo assumere che sia la matricola se i formati sono corretti (ma qui non controlliamo i formati).
                mostraMessaggio(MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO);
            }
        });
    }
    
    public void gestisciModificaLibro(){
        Libro libroSelezionato = dashboardController.getLibroSelezionato();
        
        if (libroSelezionato == null) {
            mostraMessaggio(MessaggiInterfaccia.ERRORE_SELEZIONE_LIBRO);
            return;
        }
        
        String isbnOriginale = libroSelezionato.getISBN();

        Dialog<Libro> dialog = new Dialog<>();
        dialog.setTitle("Modifica Libro");
        dialog.setHeaderText("✎ Modifica i dati del libro: " + libroSelezionato.getTitolo());
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
        stilaPulsantiDialogo(dialog); 

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
            // DELEGA TOTALE
            if (sistema.modificaLibro(isbnOriginale, lNuovo)) {
                mostraListaLibri(sistema.ottieniCatalogoOrdinato());
                mostraMessaggio(MessaggiInterfaccia.SUCCESSO_MODIFICA_LIBRO);
            } else {
                mostraMessaggio(MessaggiInterfaccia.ERRORE_MODIFICA_LIBRO);
            }
        });
    }
    
    public void gestisciModificaUtente(){
        Utente utenteSelezionato = dashboardController.getUtenteSelezionato();
        
        if (utenteSelezionato == null) {
            mostraMessaggio(MessaggiInterfaccia.ERRORE_SELEZIONE_UTENTE);
            return;
        }
        
        String matricolaOriginale = utenteSelezionato.getMatricola();

        Dialog<Utente> dialog = new Dialog<>();
        dialog.setTitle("Modifica Utente");
        dialog.setHeaderText("✎ Modifica dati utente: " + utenteSelezionato.getCognome());
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
        stilaPulsantiDialogo(dialog); 

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) return new Utente(matricola.getText(), nome.getText(), cognome.getText(), email.getText());
            return null;
        });

        Optional<Utente> res = dialog.showAndWait();
        res.ifPresent(uNuovo -> {
            // DELEGA TOTALE
            if(sistema.modificaUtente(matricolaOriginale, uNuovo)){
                mostraListaUtenti(sistema.ottieniAnagraficaOrdinata());
                mostraMessaggio(MessaggiInterfaccia.SUCCESSO_MODIFICA_UTENTE);
            } else {
                mostraMessaggio(MessaggiInterfaccia.ERRORE_MODIFICA_UTENTE);
            }
        });
    }
    
    public void gestisciEliminazioneUtente(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Elimina Utente");
        dialog.setHeaderText("☒ Inserisci Matricola per confermare eliminazione");
        
        Utente sel = dashboardController.getUtenteSelezionato();
        if(sel != null) dialog.getEditor().setText(sel.getMatricola());

        stilaPulsantiDialogo(dialog); 

        Optional<String> res = dialog.showAndWait();
        res.ifPresent(matr -> {
            if(sistema.rimuoviUtente(matr)){
                mostraListaUtenti(sistema.ottieniAnagraficaOrdinata());
                mostraMessaggio(MessaggiInterfaccia.SUCCESSO_RIMOZIONE_UTENTE);
            } else {
                mostraMessaggio(MessaggiInterfaccia.ERRORE_RIMOZIONE_UTENTE);
            }
        });
    }
    
    public void gestisciEliminazioneLibro(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Elimina Libro");
        dialog.setHeaderText("☒ Inserisci ISBN per confermare eliminazione");
        
        Libro sel = dashboardController.getLibroSelezionato();
        if(sel != null) dialog.getEditor().setText(sel.getISBN());

        stilaPulsantiDialogo(dialog); 

        Optional<String> res = dialog.showAndWait();
        res.ifPresent(isbn -> {
            if(sistema.rimuoviLibro(isbn)){
                mostraListaLibri(sistema.ottieniCatalogoOrdinato());
                mostraMessaggio(MessaggiInterfaccia.SUCCESSO_RIMOZIONE_LIBRO);
            } else {
                mostraMessaggio(MessaggiInterfaccia.ERRORE_RIMOZIONE_LIBRO);
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
        stilaPulsantiDialogo(dialog); 
        
        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                // Passiamo i dati grezzi. Il controller restituirà false se ISBN/Matricola sono invalidi.
                return sistema.gestisciPrestito(isbn.getText(), matr.getText(), data.getValue());
            }
            return null; // Annulla
        });
        
        Optional<Boolean> esito = dialog.showAndWait();
        
        if (esito.isPresent()) {
            if (esito.get()) {
                // Successo
                mostraListaPrestiti(sistema.ottieniReportPrestiti());
                mostraListaLibri(sistema.ottieniCatalogoOrdinato());
                String msg = String.format(MessaggiInterfaccia.SUCCESSO_PRESTITO, 0);
                mostraMessaggio(new MessaggiInterfaccia() { @Override public String toString() { return msg; }});
            } else {
                // Errore: il controller ha restituito false (dati non validi o vincoli violati)
                mostraMessaggio(MessaggiInterfaccia.ERRORE_REGISTRAZIONE_PRESTITO);
            }
        }
    }
    
    public void gestisciRestituzionePrestito(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Restituzione");
        dialog.setHeaderText("↩️ Inserisci ID Prestito");
        
        stilaPulsantiDialogo(dialog);
        
        Prestito pSel = dashboardController.getPrestitoSelezionato();
        if(pSel != null) dialog.getEditor().setText(String.valueOf(pSel.getIdPrestito()));
        
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                if(sistema.gestisciRestituzione(id, LocalDate.now())){
                    mostraListaPrestiti(sistema.ottieniReportPrestiti());
                    mostraListaLibri(sistema.ottieniCatalogoOrdinato());
                    mostraMessaggio(MessaggiInterfaccia.SUCCESSO_RESTITUZIONE);
                } else {
                    mostraMessaggio(MessaggiInterfaccia.ERRORE_RESTITUZIONE_NON_TROVATO);
                }
            } catch(NumberFormatException e) {
                mostraMessaggio(MessaggiInterfaccia.ERRORE_ID_NON_NUMERICO);
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
        stilaPulsantiDialogo(dialog); 

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
            if (risultati.isEmpty()) mostraMessaggio(MessaggiInterfaccia.NESSUN_LIBRO_TROVATO);
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
        comboCriterio.getItems().addAll("Cognome", "Nome", "Matricola");
        comboCriterio.setValue("Cognome"); 

        TextField txtQuery = new TextField();
        txtQuery.setPromptText("Testo da cercare...");

        grid.add(new Label("Cerca per:"), 0, 0);
        grid.add(comboCriterio, 1, 0);
        grid.add(new Label("Testo:"), 0, 1);
        grid.add(txtQuery, 1, 1);

        dialog.getDialogPane().setContent(grid);
        stilaPulsantiDialogo(dialog); 

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
            if (risultati.isEmpty()) mostraMessaggio(MessaggiInterfaccia.NESSUN_UTENTE_TROVATO);
        });
    }
    
    public void mostraListaLibri(List<Libro> lista){
        if (dashboardController != null && dashboardController.getTableLibri() != null)
            dashboardController.getTableLibri().getItems().setAll(lista);
    }
    
    public void mostraListaUtenti(List<Utente> lista){
        if (dashboardController != null && dashboardController.getTableUtenti() != null)
            dashboardController.getTableUtenti().getItems().setAll(lista);
    }
    
    public void mostraListaPrestiti(List<Prestito> lista){
        if (dashboardController != null && dashboardController.getTablePrestiti() != null)
            dashboardController.getTablePrestiti().getItems().setAll(lista);
    }
    
    public void mostraListaAuditTrail(List<String> lista) {
        if (lista == null) {
            lista = new ArrayList<>(); 
        }
        if (dashboardController != null && dashboardController.getTableAuditTrail() != null) {
            dashboardController.getTableAuditTrail().setItems(sistema.ottieniAuditTrail());
        }
    }
    
    public void mostraReportUtente(Utente u){
        mostraMessaggio("Report: " + u.getMatricola());
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