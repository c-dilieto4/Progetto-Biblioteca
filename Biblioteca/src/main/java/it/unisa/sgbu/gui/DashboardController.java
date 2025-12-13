/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import javafx.event.ActionEvent; 
import javafx.fxml.FXML;
import javafx.scene.control.Alert; 
import javafx.scene.control.Button; 
import javafx.scene.control.Tab; 
import javafx.scene.control.TabPane; 
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn; 
import javafx.scene.control.TableCell; 
import javafx.beans.property.SimpleStringProperty; 
import java.time.LocalDate; 
import javafx.collections.FXCollections;

import java.util.List; 
import java.util.stream.Collectors; 
import it.unisa.sgbu.domain.*;

public class DashboardController {

    // --- RIFERIMENTI AI TAB ---
    @FXML private TabPane tabPane; 
    @FXML private Tab tabPrestiti; 
    
    // --- TABELLE ---
    @FXML private TableView<Libro> tableLibri;
    @FXML private TableView<Utente> tableUtenti;
    @FXML private TableView<Prestito> tablePrestiti;
    @FXML private TableView<String> tableAuditTrail; 
    
    // --- COLONNE SPECIALI ---
    @FXML private TableColumn<Prestito, String> colRitardo; 
    // AGGIUNTO: Riferimento alla colonna del Log
    @FXML private TableColumn<String, String> auditTrailColumn; 

    private GUIController sistema;
    private GUIView mainView;
    
    public void setSistema(GUIController sistema, GUIView view) {
        this.sistema = sistema;
        this.mainView = view;
        
        // AGGIUNTO: Collegamento dati alle tabelle
        // Convertiamo le List normali in ObservableList per JavaFX
        if (tableLibri != null) {
            tableLibri.setItems(FXCollections.observableArrayList(sistema.ottieniCatalogoOrdinato()));
        }
        
        if (tableUtenti != null) {
            tableUtenti.setItems(FXCollections.observableArrayList(sistema.ottieniAnagraficaOrdinata()));
        }
        
        if (tablePrestiti != null) {
            tablePrestiti.setItems(FXCollections.observableArrayList(sistema.ottieniReportPrestiti()));
        }
        
        // Per l'AuditTrail NON serve FXCollections perché nel GUIController 
        // restituisce già una ObservableList (come abbiamo fatto prima)
        if (tableAuditTrail != null) {
            tableAuditTrail.setItems(sistema.ottieniAuditTrail());
        }
    }
    
    @FXML
    public void initialize() {
        // Imposta il ridimensionamento automatico delle colonne
        if(tableLibri != null) tableLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        if(tableUtenti != null) tableUtenti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        if(tablePrestiti != null) tablePrestiti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        if(tableAuditTrail != null) tableAuditTrail.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // ==========================================
        // LOGICA COLONNA RITARDO (PRESTITI)
        // ==========================================
        if (colRitardo != null) {
            colRitardo.setCellValueFactory(cellData -> {
                Prestito p = cellData.getValue();
                if (p.getDataEffettivaRestituzione() != null) return new SimpleStringProperty("Restituito");
                if (LocalDate.now().isAfter(p.getDataPrevistaRestituzione())) return new SimpleStringProperty("⚠️ IN RITARDO");
                return new SimpleStringProperty("Regolare");
            });

            colRitardo.setCellFactory(column -> new TableCell<Prestito, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null); setStyle("");
                    } else {
                        setText(item);
                        if (item.contains("RITARDO")) setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        else if (item.equals("Restituito")) setStyle("-fx-text-fill: green; -fx-font-style: italic;");
                        else setStyle("-fx-text-fill: black;");
                    }
                }
            });
        }
        
        // ==========================================
        // AGGIUNTO: LOGICA COLONNA AUDIT TRAIL
        // ==========================================
        if (auditTrailColumn != null) {
            // Dice alla colonna di stampare semplicemente la stringa contenuta nella lista
            auditTrailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        }
    }

    // ==========================================
    // SEZIONE LIBRI
    // ==========================================
    @FXML private void onCercaLibro() { mainView.gestisciRicercaLibro(); }
    @FXML private void onResetLibri() { if(sistema != null) tableLibri.getItems().setAll(sistema.ottieniCatalogoOrdinato()); }
    @FXML private void onAggiungiLibro() { mainView.gestisciAggiuntaLibro(); }
    @FXML private void onModificaLibro() { mainView.gestisciModificaLibro(); }
    @FXML private void onRimuoviLibro() { mainView.gestisciEliminazioneLibro(); }

    // ==========================================
    // SEZIONE UTENTI
    // ==========================================
    @FXML private void onCercaUtente() { mainView.gestisciRicercaUtente(); }
    @FXML private void onResetUtenti() { if(sistema != null) tableUtenti.getItems().setAll(sistema.ottieniAnagraficaOrdinata()); }
    @FXML private void onAggiungiUtente() { mainView.gestisciAggiuntaUtente(); }
    @FXML private void onModificaUtente() { mainView.gestisciModificaUtente(); }
    @FXML private void onRimuoviUtente() { mainView.gestisciEliminazioneUtente(); }

    @FXML
    private void onVediPrestitiAttiviUtente() {
        Utente utenteSelezionato = getUtenteSelezionato();
        
        if (utenteSelezionato == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nessuna Selezione");
            alert.setHeaderText(null);
            alert.setContentText("Seleziona prima un utente dalla tabella per vederne i prestiti.");
            alert.showAndWait();
            return;
        }

        if (sistema != null && tablePrestiti != null) {
            List<Prestito> prestitiAttivi = sistema.ottieniReportPrestiti().stream()
                .filter(p -> p.getUtente().getMatricola().equals(utenteSelezionato.getMatricola()))
                .filter(p -> p.getDataEffettivaRestituzione() == null)
                .collect(Collectors.toList());

            tablePrestiti.getItems().setAll(prestitiAttivi);
            
            if (tabPane != null && tabPrestiti != null) {
                tabPane.getSelectionModel().select(tabPrestiti);
            }
        }
    }

    // ==========================================
    // SEZIONE PRESTITI 
    // ==========================================
    
    @FXML
    private void onResetPrestiti() {
         if (sistema != null && tablePrestiti != null) {
             tablePrestiti.getItems().setAll(sistema.ottieniReportPrestiti());
         }
    }

    @FXML private void onNuovoPrestito() { mainView.gestisciRegistrazionePrestito(); }
    @FXML private void onRegistraRestituzione() { mainView.gestisciRestituzionePrestito(); }
    
    // ==========================================
    // GETTERS & HELPERS
    // ==========================================
    
    public TableView<Libro> getTableLibri() { return tableLibri; }
    public TableView<Utente> getTableUtenti() { return tableUtenti; }
    public TableView<Prestito> getTablePrestiti() { return tablePrestiti; }
    public TableView<String> getTableAuditTrail() { return tableAuditTrail; }
    
    public Libro getLibroSelezionato() { return (tableLibri != null) ? tableLibri.getSelectionModel().getSelectedItem() : null; }
    public Utente getUtenteSelezionato() { return (tableUtenti != null) ? tableUtenti.getSelectionModel().getSelectedItem() : null; }
    public Prestito getPrestitoSelezionato() { return (tablePrestiti != null) ? tablePrestiti.getSelectionModel().getSelectedItem() : null; }
}