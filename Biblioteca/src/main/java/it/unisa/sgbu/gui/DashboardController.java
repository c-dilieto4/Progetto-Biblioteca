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


/**
 * @brief Controller per la Dashboard principale (GUI).
 * 
 * Gestisce l'interazione tra l'utente e il sistema attraverso l'interfaccia grafica.
 * Si occupa di popolare le tabelle, gestire gli eventi dei pulsanti e coordinare
 * i flussi di visualizzazione dati.
 */
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
    @FXML private TableColumn<String, String> auditTrailColumn; 

    private GUIController sistema;
    private GUIView mainView;
    
    
    /**
     * @brief Inizializza il collegamento tra GUI e logica di dominio.
     * 
     * @param[in] sistema Istanza del controller logico del sistema.
     * @param[in] view Istanza della vista principale per la gestione delle finestre.
     * 
     * @pre 
     * - I parametri sistema e view devono essere inizializzati (non null).
     * 
     * @post 
     * - I riferimenti interni sono impostati.
     * - Le tabelle (Libri, Utenti, Prestiti) vengono popolate con i dati attuali tramite ObservableList.
     * - La tabella AuditTrail viene collegata al log di sistema.
     */
    public void setSistema(GUIController sistema, GUIView view) {
        this.sistema = sistema;
        this.mainView = view;
        
        if (tableLibri != null) {
            tableLibri.setItems(FXCollections.observableArrayList(sistema.ottieniCatalogoOrdinato()));
        }
        
        if (tableUtenti != null) {
            tableUtenti.setItems(FXCollections.observableArrayList(sistema.ottieniAnagraficaOrdinata()));
        }
        
        if (tablePrestiti != null) {
            tablePrestiti.setItems(FXCollections.observableArrayList(sistema.ottieniReportPrestiti()));
        }
        
        if (tableAuditTrail != null) {
            tableAuditTrail.setItems(sistema.ottieniAuditTrail());
        }
    }
    
    
    /**
     * @brief Metodo di inizializzazione automatica JavaFX.
     * 
     * Viene invocato automaticamente dopo il caricamento del file FXML.
     * Configura le proprietà visuali delle tabelle e le logiche delle celle personalizzate.
     * 
     * @post 
     * - Le policy di ridimensionamento delle colonne sono impostate su CONSTRAINED_RESIZE.
     * - La colonna "Ritardo" è configurata per mostrare i relativi stati.
     * - Lo stile della colonna "Ritardo" cambia colore (Rosso/Verde) in base allo stato.
     */
    @FXML
    public void initialize() {
        // Imposta il ridimensionamento automatico delle colonne
        if(tableLibri != null){
            tableLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        if(tableUtenti != null){
            tableUtenti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        if(tablePrestiti != null) {
            tablePrestiti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        if(tableAuditTrail != null){
            tableAuditTrail.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        
      
        if (colRitardo != null) {
            colRitardo.setCellValueFactory(cellData -> {
                Prestito p = cellData.getValue();
                if (p.getDataEffettivaRestituzione() != null){
                    return new SimpleStringProperty("Restituito");
                }
                if (LocalDate.now().isAfter(p.getDataPrevistaRestituzione())){
                    return new SimpleStringProperty("⚠️ IN RITARDO");
                }
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
                        if (item.contains("RITARDO")){
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        }
                        else if (item.equals("Restituito")){
                            setStyle("-fx-text-fill: green; -fx-font-style: italic;");
                        }
                        else setStyle("-fx-text-fill: black;");
                    }
                }
            });
        }

        if (auditTrailColumn != null) {
            // Dice alla colonna di stampare semplicemente la stringa contenuta nella lista
            auditTrailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        }
    }

    
    /**
     * @brief Gestore evento: Ricerca Libro.
     * @post Viene invocato il metodo gestisciRicercaLibro() della MainView.
     */
    @FXML 
    private void onCercaLibro() { 
        mainView.gestisciRicercaLibro(); 
    }
    
    
    /**
     * @brief Gestore evento: Reset filtri tabella Libri.
     * @post La tableLibri viene ripopolata con l'intero catalogo ordinato.
     */
    @FXML 
    private void onResetLibri() { 
        if(sistema != null){
            tableLibri.getItems().setAll(sistema.ottieniCatalogoOrdinato());
        } 
    }
    
    
    /**
     * @brief Gestore evento: Aggiungi Libro.
     * @post Apre la finestra di dialogo per l'inserimento di un nuovo libro.
     */
    @FXML 
    private void onAggiungiLibro() { 
        mainView.gestisciAggiuntaLibro(); 
    }
    
    
    /**
     * @brief Gestore evento: Modifica Libro.
     * @post Apre la finestra di dialogo per la modifica del libro selezionato.
     */
    @FXML 
    private void onModificaLibro() { 
        mainView.gestisciModificaLibro(); 
    }
    
    
    /**
     * @brief Gestore evento: Rimuovi Libro.
     * @post Avvia il flusso di eliminazione per il libro selezionato.
     */
    @FXML 
    private void onRimuoviLibro() { 
        mainView.gestisciEliminazioneLibro(); 
    }

    
    /**
     * @brief Gestore evento: Cerca Utente.
     * @post Viene invocato il metodo gestisciRicercaUtente() della MainView.
     */
    @FXML 
    private void onCercaUtente() { 
        mainView.gestisciRicercaUtente(); 
    }
    
    
    /**
     * @brief Gestore evento: Reset filtri tabella Utenti.
     * @post La tableUtenti viene ripopolata con l'intera anagrafica ordinata.
     */
    @FXML 
    private void onResetUtenti() { 
        if(sistema != null){
            tableUtenti.getItems().setAll(sistema.ottieniAnagraficaOrdinata());
        } 
    }
    
    
    /**
     * @brief Gestore evento: Aggiungi Utente.
     * @post Apre la finestra di dialogo per la registrazione di un nuovo utente.
     */
    @FXML 
    private void onAggiungiUtente() { 
        mainView.gestisciAggiuntaUtente(); 
    }
    
    
    /**
     * @brief Gestore evento: Modifica Utente.
     * @post Apre la finestra di dialogo per la modifica dell'utente selezionato.
     */
    @FXML 
    private void onModificaUtente() { 
        mainView.gestisciModificaUtente(); 
    }
    
    
    /**
     * @brief Gestore evento: Rimuovi Utente.
     * @post Avvia il flusso di eliminazione per l'utente selezionato.
     */
    @FXML 
    private void onRimuoviUtente() { 
        mainView.gestisciEliminazioneUtente(); 
    }

    
    /**
     * @brief Visualizza i prestiti attivi per l'utente selezionato.
     * 
     * @pre 
     * - Un utente deve essere selezionato nella tabella tableUtenti.
     * 
     * @post 
     * - Se un utente è selezionato: la tabella tablePrestiti mostra solo i prestiti non restituiti di quell'utente e il focus passa al tab Prestiti.
     * - Se nessun utente è selezionato: viene mostrato un Alert di avviso.
     */
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
    
    
    /**
     * @brief Gestore evento: Reset filtri tabella Prestiti.
     * @post La tablePrestiti viene ripopolata con lo storico completo dei prestiti.
     */
    @FXML
    private void onResetPrestiti() {
         if (sistema != null && tablePrestiti != null) {
             tablePrestiti.getItems().setAll(sistema.ottieniReportPrestiti());
         }
    }
    

    /**
     * @brief Gestore evento: Nuovo Prestito.
     * @post Avvia il flusso di registrazione di un nuovo prestito.
     */
    @FXML 
    private void onNuovoPrestito() { 
        mainView.gestisciRegistrazionePrestito(); 
    }
    
    
    /**
     * @brief Gestore evento: Registra Restituzione.
     * @post Avvia il flusso di restituzione libro.
     */
    @FXML private void onRegistraRestituzione() { 
        mainView.gestisciRestituzionePrestito(); 
    }
    
    
    /**
     * @brief Getter per la tabella Libri.
     * @return Riferimento all'oggetto TableView<Libro>.
     */
    public TableView<Libro> getTableLibri() { 
        return tableLibri; 
    }
    
    
    /**
     * @brief Getter per la tabella Utenti.
     * @return Riferimento all'oggetto TableView<Utente>.
     */
    public TableView<Utente> getTableUtenti() { 
        return tableUtenti; 
    }
    
    
    /**
     * @brief Getter per la tabella Prestiti.
     * @return Riferimento all'oggetto TableView<Prestito>.
     */
    public TableView<Prestito> getTablePrestiti() { 
        return tablePrestiti; 
    }
    
    
    /**
     * @brief Getter per la tabella Audit Trail.
     * @return Riferimento all'oggetto TableView<String>.
     */
    public TableView<String> getTableAuditTrail() { 
        return tableAuditTrail; 
    }
    
    
    /**
     * @brief Recupera il libro attualmente selezionato.
     * @return L'oggetto Libro selezionato o null se nessuna selezione.
     */
    public Libro getLibroSelezionato() { 
        return (tableLibri != null) ? tableLibri.getSelectionModel().getSelectedItem() : null; 
    }
    
    
    /**
     * @brief Recupera l'utente attualmente selezionato.
     * @return L'oggetto Utente selezionato o null se nessuna selezione.
     */
    public Utente getUtenteSelezionato() { 
        return (tableUtenti != null) ? tableUtenti.getSelectionModel().getSelectedItem() : null; 
    }
    
    
    /**
     * @brief Recupera il prestito attualmente selezionato.
     * @return L'oggetto Prestito selezionato o null se nessuna selezione.
     */
    public Prestito getPrestitoSelezionato() { 
        return (tablePrestiti != null) ? tablePrestiti.getSelectionModel().getSelectedItem() : null; 
    }
}