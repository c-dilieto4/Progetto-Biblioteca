/*
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import it.unisa.sgbu.domain.*;

public class DashboardController {

    // --- TABELLE ---
    @FXML private TableView<Libro> tableLibri;
    @FXML private TableView<Utente> tableUtenti;
    @FXML private TableView<Prestito> tablePrestiti;
    @FXML private TextField txtCercaLibro;
    @FXML private TextField txtCercaUtente;
    @FXML private TextField txtCercaPrestito;

    private GUIController sistema;
    private GUIView mainView;

    public void setSistema(GUIController sistema, GUIView view) {
        this.sistema = sistema;
        this.mainView = view;
    }

    @FXML
    public void initialize() {
        
        tableLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableUtenti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablePrestiti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
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
    
    
    public TableView<Libro> getTableLibri() { return tableLibri; }
    public TableView<Utente> getTableUtenti() { return tableUtenti; }
    public TableView<Prestito> getTablePrestiti() { return tablePrestiti; }
}