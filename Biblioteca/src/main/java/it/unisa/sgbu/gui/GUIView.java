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
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @brief Classe Boundary che gestisce l'interfaccia utente (View).
 */
public class GUIView {
    
    private final GUIController sistema;
    private final MessaggiInterfaccia messaggi;
    private Stage primaryStage;
    
    // Riferimento al controller grafico per aggiornare le tabelle
    private DashboardController dashboardController;

    /**
     * @brief Costruttore della classe GUIView.
     */
    public GUIView(GUIController sistema, Stage primaryStage){
        this.sistema = sistema;
        this.messaggi = new MessaggiInterfaccia();
        this.primaryStage = primaryStage;
    }
    
    /**
     * @brief Avvia l'interfaccia grafica principale.
     */
    public void avviaInterfaccia(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardView.fxml")); 
            Parent root = loader.load();
            
            this.dashboardController = loader.getController();
            this.dashboardController.setSistema(this.sistema, this);
            
            // Aggiornamento manuale delle tabelle all'avvio
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
    
    /**
     * @brief Gestisce il form di inserimento di un nuovo libro.
     */
    public void gestisciAggiuntaLibro(){
        Dialog<Libro> dialog = new Dialog<>();
        dialog.setTitle("Aggiungi Libro");
        dialog.setHeaderText("Nuovo Libro");
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
                String msg = String.format(MessaggiInterfaccia.SUCCESSO_AGGIUNTA_LIBRO, l.getTitolo());
                mostraMessaggio(new MessaggiInterfaccia() { @Override public String toString() { return msg; }});
            } else {

                String msg = String.format(MessaggiInterfaccia.ERRORE_ISBN_DUPLICATO, l.getISBN(), "presente nel catalogo");
                mostraMessaggio(new MessaggiInterfaccia() { @Override public String toString() { return msg; }});
            }
        });
    }
    
    /**
     * @brief Gestisce il form di registrazione di un nuovo utente.
     */
    public void gestisciAggiuntaUtente(){
        Dialog<Utente> dialog = new Dialog<>();
        dialog.setTitle("Nuovo Utente");
        dialog.setHeaderText("Dati Anagrafici");
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

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) return new Utente(matricola.getText(), nome.getText(), cognome.getText(), email.getText());
            return null;
        });

        Optional<Utente> res = dialog.showAndWait();
        res.ifPresent(u -> {
            if (sistema.aggiungiUtente(u)) {
                mostraListaUtenti(sistema.ottieniAnagraficaOrdinata());
                mostraMessaggio("Utente registrato con successo."); 
            } else {

                String msg = String.format(MessaggiInterfaccia.ERRORE_MATRICOLA_DUPLICATA, u.getMatricola());
                mostraMessaggio(new MessaggiInterfaccia() { @Override public String toString() { return msg; }});
            }
        });
    }
    
    public void gestisciModificaLibro(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Modifica Libro");
        dialog.setHeaderText("ISBN Libro");
        Optional<String> res = dialog.showAndWait();
        

        res.ifPresent(isbn -> mostraMessaggio(MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO + " (Funzionalità UI incompleta)"));
    }
    
    public void gestisciModificaUtente(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Modifica Utente");
        dialog.setHeaderText("Matricola Utente");
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(matr -> mostraMessaggio(MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO + " (Funzionalità UI incompleta)"));
    }
    
    public void gestisciEliminazioneUtente(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Elimina Utente");
        dialog.setHeaderText("Inserisci Matricola");
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
        dialog.setHeaderText("Inserisci ISBN");
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
    
    /**
     * @brief Gestisce l'interazione per la registrazione di un prestito.
     */
    public void gestisciRegistrazionePrestito(){
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Nuovo Prestito");
        dialog.setHeaderText("Registra Prestito");
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
        
        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                return sistema.gestisciPrestito(isbn.getText(), matr.getText(), data.getValue());
            }
            return false;
        });
        
        Optional<Boolean> esito = dialog.showAndWait();
        if (esito.isPresent() && esito.get()) {
            mostraListaPrestiti(sistema.ottieniReportPrestiti());
            mostraListaLibri(sistema.ottieniCatalogoOrdinato());
            String msg = String.format(MessaggiInterfaccia.SUCCESSO_PRESTITO, 0);
            mostraMessaggio(new MessaggiInterfaccia() { @Override public String toString() { return msg; }});
        } else {
            mostraMessaggio(MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO);
        }
    }
    
    public void gestisciRestituzionePrestito(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Restituzione");
        dialog.setHeaderText("ID Prestito");
        
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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cerca Libro");
        dialog.setHeaderText("Titolo");
        Optional<String> res = dialog.showAndWait();
        
        res.ifPresent(query -> {
            List<Libro> risultati = sistema.cercaLibro(query, "Titolo");
            mostraListaLibri(risultati);
        });
    }
    
    public void gestisciRicercaUtente(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cerca Utente");
        dialog.setHeaderText("Cognome");
        Optional<String> res = dialog.showAndWait();
        
        res.ifPresent(query -> {
            List<Utente> risultati = sistema.cercaUtente(query, "Cognome");
            mostraListaUtenti(risultati);
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
    
    // 1. Dichiariamo e instanziamo l'ObservableList localmente.
    // Usiamo 'lista' o 'Lista' come nome della variabile in ingresso, a seconda della convenzione che usi.
    if (Lista == null) {
        Lista = new ArrayList<>(); // Evita NullPointerException se la lista è null
    }
    
    ObservableList<String> datiObservable = FXCollections.observableArrayList(Lista);
    
    // 2. Usiamo le parentesi graffe per racchiudere entrambe le operazioni
    if (dashboardController != null && dashboardController.getTableAuditTrail() != null) {
        // La creazione dell'istanza ora avviene FUORI dall'if, dopo aver controllato il null.
        
        dashboardController.getTableAuditTrail().setItems(datiObservable);
    }
}
    
    public void mostraReportUtente(Utente u){
        mostraMessaggio("Report: " + u.toString());
    }
    
    /**
     * @brief Mostra un messaggio di feedback all'utente.
     * * Modificato per accettare Object/String per adattarsi all'uso delle costanti.
     */
    public void mostraMessaggio(Object msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SGBU Info");
        alert.setHeaderText(null);
        alert.setContentText(msg.toString());
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
        alert.showAndWait();
    }
}
