package it.unisa.sgbu.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn; 
import javafx.beans.property.SimpleStringProperty; 
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList; 
import java.util.List; 
import java.util.ArrayList; 
import it.unisa.sgbu.domain.*;

public class DashboardController {

    // --- TABELLE & CAMPI ---
    @FXML private TableView<Libro> tableLibri;
    @FXML private TableView<Utente> tableUtenti;
    @FXML private TableView<Prestito> tablePrestiti;
    @FXML private TextField txtCercaLibro;
    @FXML private TextField txtCercaUtente;
    @FXML private TextField txtCercaPrestito;
    
    // Dichiarazioni per l'Audit Trail
    @FXML private TableView<String> tableAuditTrail;
    @FXML private TableColumn<String, String> auditTrailColumn; 
    
    private GUIController sistema;
    private GUIView mainView;

    // Metodo chiamato dopo che FXML è caricato
    public void initialize() {
        
        tableLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableUtenti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablePrestiti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableAuditTrail.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        
        if (auditTrailColumn != null) {
            auditTrailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        }
    }
    
    public void setSistema(GUIController sistema, GUIView view) {
        this.sistema = sistema;
        this.mainView = view;

        // Verifichiamo che il sistema sia valido per evitare NPE
        if (this.sistema != null) {
            // 1. Otteniamo la lista di stringhe dal sistema
            List<String> logList = this.sistema.ottieniAuditTrail();

            // 2. Carichiamo la lista nella TableView
            // Chiamiamo il metodo che converte in ObservableList e fa il setItems()
            caricaAuditTrail(logList);
        }
    }

    /**
     * Popola la tableAuditTrail convertendo la List<String> in ObservableList.
     * @param listaLog La lista di stringhe da visualizzare.
     */
    public void caricaAuditTrail(List<String> listaLog) {
        if (tableAuditTrail == null) {
            System.err.println("Errore: tableAuditTrail non inizializzata. Chiamare dopo l'initialize.");
            return;
        }

        // 1. Controlla se la lista in ingresso è null
        if (listaLog == null) {
             // Usiamo new ArrayList<>() come fallback sicuro (compatibile con tutte le versioni Java)
            listaLog = new ArrayList<>(); 
        }

        // 2. Crea l'istanza di ObservableList (datiObservable)
        ObservableList<String> datiObservable = FXCollections.observableArrayList(listaLog);
        
        // 3. Carica l'istanza nella TableView
        tableAuditTrail.setItems(datiObservable);
    }


    // --- Metodi Gestori (rimasti invariati) ---
    
    @FXML
    private void onCercaLibro() {
        String query = txtCercaLibro.getText();
        System.out.println("Ricerca libro: " + query);
    }

    @FXML
    private void onAggiungiLibro() {
        mainView.gestisciAggiuntaLibro(); 
    }

    @FXML
    private void onModificaLibro() {
        Libro libroSelezionato = tableLibri.getSelectionModel().getSelectedItem();
        if (libroSelezionato != null) {
            System.out.println("Modifica libro: " + libroSelezionato.getTitolo());
        } else {
            System.out.println("Nessun libro selezionato per la modifica");
        }
    }

    @FXML
    private void onRimuoviLibro() {
        mainView.gestisciEliminazioneLibro(); 
    }

    @FXML
    private void onCercaUtente() {
        String query = txtCercaUtente.getText();
        System.out.println("Ricerca utente: " + query);
    }

    @FXML
    private void onAggiungiUtente() {
        mainView.gestisciAggiuntaUtente();
        
    }

    @FXML
    private void onModificaUtente() {
        Utente utenteSelezionato = tableUtenti.getSelectionModel().getSelectedItem();
        if (utenteSelezionato != null) {
            System.out.println("Modifica utente: " + utenteSelezionato.getCognome());
        }
    }

    @FXML
    private void onRimuoviUtente() {
        System.out.println("Richiesta rimozione utente");
    }


    @FXML
    private void onCercaPrestito() {
        String query = txtCercaPrestito.getText();
        System.out.println("Ricerca prestito: " + query);
    }

    @FXML
    private void onNuovoPrestito() {
        mainView.gestisciRegistrazionePrestito();
    }

    @FXML
    private void onModificaPrestito() {
        Prestito prestitoSelezionato = tablePrestiti.getSelectionModel().getSelectedItem();
        if (prestitoSelezionato != null) {
            System.out.println("Modifica prestito ID: " + prestitoSelezionato.getIdPrestito());
        }
    }

    @FXML
    private void onRegistraRestituzione() {
        mainView.gestisciRestituzionePrestito();
    }
    
    @FXML
    private void onRimuoviPrestito() {
        System.out.println("Richiesta rimozione prestito");
    }
    
    // --- Getter (rimasti invariati) ---
    public TableView<Libro> getTableLibri() { return tableLibri; }
    public TableView<Utente> getTableUtenti() { return tableUtenti; }
    public TableView<Prestito> getTablePrestiti() { return tablePrestiti; }
    public TableView<String> getTableAuditTrail() { return tableAuditTrail; }
}