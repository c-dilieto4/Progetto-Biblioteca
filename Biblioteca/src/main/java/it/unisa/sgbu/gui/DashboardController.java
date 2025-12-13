/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import javafx.event.ActionEvent; 
import javafx.fxml.FXML;
import javafx.scene.control.Button; 
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn; // AGGIUNTO
import javafx.scene.control.TableCell; // AGGIUNTO
import javafx.beans.property.SimpleStringProperty; // AGGIUNTO
import java.time.LocalDate; // AGGIUNTO per le date

import java.util.List; 
import java.util.stream.Collectors; 
import it.unisa.sgbu.domain.*;

public class DashboardController {

    // --- TABELLE ---
    @FXML private TableView<Libro> tableLibri;
    @FXML private TableView<Utente> tableUtenti;
    @FXML private TableView<Prestito> tablePrestiti;
    @FXML private TableView<String> tableAuditTrail; 
    
    // --- COLONNE SPECIALI ---
    @FXML private TableColumn<Prestito, String> colRitardo; // AGGIUNTO: Riferimento alla colonna stato

    private GUIController sistema;
    private GUIView mainView;
    
    // Stato del filtro prestiti
    private boolean mostraSoloAttivi = false;

    public void setSistema(GUIController sistema, GUIView view) {
        this.sistema = sistema;
        this.mainView = view;
    }
    
    @FXML
    public void initialize() {
        // Imposta il ridimensionamento automatico delle colonne
        if(tableLibri != null) tableLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        if(tableUtenti != null) tableUtenti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        if(tablePrestiti != null) tablePrestiti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        if(tableAuditTrail != null) tableAuditTrail.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // ==========================================
        // AGGIUNTO: LOGICA COLONNA RITARDO
        // ==========================================
        if (colRitardo != null) {
            
            // 1. Calcolo del valore da mostrare
            colRitardo.setCellValueFactory(cellData -> {
                Prestito p = cellData.getValue();
                
                // Se c'è una data di restituzione effettiva, il prestito è chiuso
                if (p.getDataEffettivaRestituzione() != null) {
                    return new SimpleStringProperty("Restituito");
                }
                
                // Se non è restituito, controlliamo se oggi è dopo la scadenza
                if (LocalDate.now().isAfter(p.getDataPrevistaRestituzione())) {
                    return new SimpleStringProperty("⚠️ IN RITARDO");
                }
                
                return new SimpleStringProperty("Regolare");
            });

            // 2. Stile della cella (Colori)
            colRitardo.setCellFactory(column -> new TableCell<Prestito, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        
                        // Logica colori
                        if (item.contains("RITARDO")) {
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        } else if (item.equals("Restituito")) {
                            setStyle("-fx-text-fill: green; -fx-font-style: italic;");
                        } else {
                            setStyle("-fx-text-fill: black;");
                        }
                    }
                }
            });
        }
    }

    // ==========================================
    // SEZIONE LIBRI
    // ==========================================

    @FXML
    private void onCercaLibro() {
        mainView.gestisciRicercaLibro(); 
    }
    
    @FXML
    private void onResetLibri() {
        if(sistema != null && tableLibri != null) {
            tableLibri.getItems().setAll(sistema.ottieniCatalogoOrdinato());
        }
    }

    @FXML
    private void onAggiungiLibro() {
        mainView.gestisciAggiuntaLibro(); 
    }

    @FXML
    private void onModificaLibro() {
        mainView.gestisciModificaLibro();
    }

    @FXML
    private void onRimuoviLibro() {
        mainView.gestisciEliminazioneLibro(); 
    }

    // ==========================================
    // SEZIONE UTENTI
    // ==========================================

    @FXML
    private void onCercaUtente() {
        mainView.gestisciRicercaUtente();
    }
    
    @FXML
    private void onResetUtenti() {
        if(sistema != null && tableUtenti != null) {
            tableUtenti.getItems().setAll(sistema.ottieniAnagraficaOrdinata());
        }
    }

    @FXML
    private void onAggiungiUtente() {
        mainView.gestisciAggiuntaUtente();
    }

    @FXML
    private void onModificaUtente() {
        mainView.gestisciModificaUtente();
    }

    @FXML
    private void onRimuoviUtente() {
        mainView.gestisciEliminazioneUtente();
    }

    // ==========================================
    // SEZIONE PRESTITI 
    // ==========================================

    /**
     * Gestisce il filtro "Solo Attivi" vs "Tutti".
     * Cambia aspetto del bottone e contenuto della tabella.
     */
    @FXML
    private void onMostraSoloAttivi(ActionEvent event) {
        if (sistema == null || tablePrestiti == null) return;

        // Invertiamo lo stato
        mostraSoloAttivi = !mostraSoloAttivi;
        
        // Recuperiamo il bottone che ha scatenato l'evento per cambiarne lo stile
        Button btn = (Button) event.getSource();

        if (mostraSoloAttivi) {
            // --- STATO: SOLO ATTIVI ---
            
            // 1. Cambia grafica bottone
            btn.setText("📋 Mostra Tutti");
            btn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
            
            // 2. Filtra la lista
            List<Prestito> tutti = sistema.ottieniReportPrestiti();
            List<Prestito> attivi = tutti.stream()
                .filter(p -> p.getDataEffettivaRestituzione() == null) 
                .collect(Collectors.toList());
            
            tablePrestiti.getItems().setAll(attivi);

        } else {
            // --- STATO: MOSTRA TUTTI ---
            
            // 1. Cambia grafica bottone
            btn.setText(" Solo Attivi");
            btn.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
            
            // 2. Ripristina lista completa
            tablePrestiti.getItems().setAll(sistema.ottieniReportPrestiti());
        }
    }

    @FXML
    private void onNuovoPrestito() {
        mainView.gestisciRegistrazionePrestito();
    }
    
    @FXML
    private void onRegistraRestituzione() {
        mainView.gestisciRestituzionePrestito();
    }
    
    // ==========================================
    // GETTERS & HELPERS
    // ==========================================
    
    public TableView<Libro> getTableLibri() { return tableLibri; }
    public TableView<Utente> getTableUtenti() { return tableUtenti; }
    public TableView<Prestito> getTablePrestiti() { return tablePrestiti; }
    public TableView<String> getTableAuditTrail() { return tableAuditTrail; }
    
    public Libro getLibroSelezionato() {
        if (tableLibri != null) {
            return tableLibri.getSelectionModel().getSelectedItem();
        }
        return null;
    }

    public Utente getUtenteSelezionato() {
        if (tableUtenti != null) {
            return tableUtenti.getSelectionModel().getSelectedItem();
        }
        return null;
    }
    
    public Prestito getPrestitoSelezionato() {
        if (tablePrestiti != null) {
            return tablePrestiti.getSelectionModel().getSelectedItem();
        }
        return null;
    }
}