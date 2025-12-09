/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import it.unisa.sgbu.domain.Libro;
import it.unisa.sgbu.domain.Prestito;
import it.unisa.sgbu.domain.Utente;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @brief Classe Boundary che gestisce l'interfaccia utente (View).
 * 
 * Questa classe è responsabile di tutta l'interazione con l'attore (Bibliotecario).
 * Si occupa di:
 * 1. Catturare l'input dell'utente (dati per inserimenti, ricerche, transazioni).
 * 2. Invocare i metodi di orchestrazione del GUIController.
 * 3. Visualizzare i risultati e i messaggi di sistema.
 * Non contiene alcuna logica di business né accede direttamente ai dati.
 */
public class GUIView {
    private final GUIController sistema;
    private final MessaggiInterfaccia messaggi;
    private Stage primaryStage;
    
    /**
     * @brief Costruttore della classe GUIView.
     * 
     * Collega la vista al controller che gestirà la logica delle richieste.
     * 
     * @param[in] Sistema Istanza del controller principale (GUIController).
     */
    public GUIView(GUIController sistema, Stage primaryStage){
        this.sistema = sistema;
        this.messaggi = new MessaggiInterfaccia();
        this.primaryStage = primaryStage;
    }
    
    
    /**
     * @brief Avvia l'interfaccia grafica principale.
     * 
     * Da invocare dopo che il login è avvenuto con successo.
     * Inizializza i componenti grafici della dashboard principale.
     */
    public void avviaInterfaccia(){
        try {
            // Carico il file XML della dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardView.fxml"));
            Parent root = loader.load();
            
            DashboardController controller = loader.getController();
            controller.setSistema(this.sistema, this);
            
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("SGBU - Biblioteca Dashboard");
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * @brief Gestisce il form di inserimento di un nuovo libro.
     * 
     * 1. Mostra il form di input.
     * 2. Raccoglie i dati (ISBN, Titolo, Autori, Anno, Copie).
     * 3. Chiama `Sistema.aggiungiLibro()`.
     */
    public void gestisciAggiuntaLibro(){
    }
    
    
    /**
     * @brief Gestisce il form di registrazione di un nuovo utente.
     * 
     * 1. Mostra il form di input.
     * 2. Raccoglie i dati (Matricola, Nome, Cognome, Email).
     * 3. Chiama `sistema.aggiungiUtente(Utente u)`.
     */
    public void gestisciAggiuntaUtente(){
    }
    
    
    /**
     * @brief Gestisce la modifica di un libro esistente.
     * 
     * Cattura l'ISBN del libro da modificare e i nuovi dati, 
     * invocando poi `sistema.modificaLibro(String isbnOriginale, Libro lNuovo)`.
     */
    public void gestisciModificaLibro(){
    }
    
    
    /**
     * @brief Gestisce la modifica di un utente esistente.
     * 
     * Cattura la matricola dell'utente da modificare e i nuovi dati anagrafici,
     * invocando poi `sistema.modificaUtente(String matrOriginale, Utente uNuovo)`.
     */
    public void gestisciModificaUtente(){
    }
    
    
    /**
     * @brief Gestisce la cancellazione di un utente.
     * 
     * Richiede la matricola dell'utente da eliminare e invoca `sistema.rimuoviUtente(String matr)`.
     * Visualizza eventuali errori se l'utente ha prestiti attivi.
     */
    public void gestisciEliminazioneUtente(){
    }
    
    
    /**
     * @brief Gestisce la cancellazione di un libro.
     * 
     * Richiede l'ISBN del libro da eliminare e invoca `sistema.rimuoviLibro(String isbn)`.
     */
    public void gestisciEliminazioneLibro(){
    }
    
    
    /**
     * @brief Gestisce l'interazione per la registrazione di un prestito.
     * 
     * Implementa l'interfaccia per il relativo Caso d'Uso.
     * 1. Richiede ISBN libro e Matricola utente.
     * 2. Richiede la data prevista di restituzione.
     * 3. Invoca `sistema.gestisciPrestito(String isbn, String matricola, LocalDate dataPrevistaRestituzione)`.
     */
    public void gestisciRegistrazionePrestito(){
    }
    
    
    /**
     * @brief Gestisce l'interazione per la restituzione di un libro.
     * 
     * Implementa l'interfaccia per il relativo Caso d'Uso.
     * 1. Richiede l'ID del prestito o scansiona il libro.
     * 2. Invoca `sistema.gestisciRestituzione(int idPrestito, LocalDate dataEffettivaRestituzione)`.
     */
    public void gestisciRestituzionePrestito(){
    }
    
    
    /**
     * @brief Gestisce la ricerca nel catalogo libri.
     * 
     * Raccoglie la query di ricerca e il campo filtro (Titolo, Autore, ISBN)
     * e chiama `sstema.cercaLibro(String query, String campo)`.
     */
    public void gestisciRicercaLibro(){
    }
    
    
    /**
     * @brief Gestisce la ricerca nell'anagrafica utenti.
     * 
     * Raccoglie la query di ricerca e il campo filtro (Cognome, Matricola)
     * e chiama `sistema.cercaUtente(String query, String campo)`.
     */
    public void gestisciRicercaUtente(){
    }
    
    
    /**
     * @brief Visualizza l'elenco dei libri.
     * 
     * Renderizza a video la lista fornita dal controller (ordinata per Titolo).
     * 
     * @param[in] Lista La lista di oggetti Libro da mostrare.
     */
    public void mostraListaLibri(List<Libro> Lista){
    }
    
    
    /**
     * @brief Visualizza l'elenco degli utenti.
     * 
     * Renderizza a video la lista fornita dal controller (ordinata per Cognome).
     * 
     * @param[in] Lista La lista di oggetti Utente da mostrare.
     */
    public void mostraListaUtenti(List<Utente> Lista){
    }
    
    
    /**
     * @brief Visualizza l'elenco dei prestiti attivi.
     * 
     * Mostra la tabella dei prestiti.
     * Supporta l'apposito requisito: evidenzia visivamente (es. colore rosso) 
     * le righe corrispondenti ai prestiti in ritardo.
     * 
     * @param[in] Lista La lista di prestiti attivi.
     */
    public void mostraListaPrestiti(List<Prestito> Lista){
    }
    
    
    /**
     * @brief Visualizza lo storico prestiti di un singolo utente.
     * 
     * @param[in] u L'utente di cui mostrare il report.
     */
    public void mostraReportUtente(Utente u){
    }
    
    
    /**
     * @brief Mostra un messaggio di feedback all'utente.
     * 
     * Utilizzato per notifiche di successo ("SUCCESSO_PRESTITO") o
     * messaggi di errore ("ERRORE_MATRICOLA_DUPLICATA").
     * 
     * @param[in] msg L'oggetto o la stringa contenente il messaggio standardizzato.
     */
    public void mostraMessaggio(MessaggiInterfaccia msg){
    }
    
    
    /**
     * @brief Visualizza la finestra di Login all'avvio.
     * 
     * Prima interfaccia mostrata al Bibliotecario.
     * Cattura Username e Password e invoca `Sistema.gestisciLogin(String user, String pass)`.
     */
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
   
    
    /**
     * @brief Visualizza il log delle operazioni di sistema (Audit Trail).
     * 
     * @param[in] log La lista delle stringhe di log da mostrare.
     */
    public void mostraLogDiSistema(List<String> log){
    }
    
    
}
