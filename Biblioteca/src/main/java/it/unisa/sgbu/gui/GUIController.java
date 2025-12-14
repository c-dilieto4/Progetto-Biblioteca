/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import it.unisa.sgbu.io.*;
import it.unisa.sgbu.service.*;
import it.unisa.sgbu.domain.*;
import javafx.collections.FXCollections; // NECESSARIO PER LA GUI
import javafx.collections.ObservableList; // NECESSARIO PER LA GUI
import java.time.LocalDate;
import java.util.*;

/**
 * @brief Controller principale del sistema.
 * 
 * Questa classe funge da orchestratore centrale e punto di ingresso per tutte le operazioni
 * logiche richieste dall'interfaccia grafica. 
 * 
 * Le sue responsabilità includono:
 * - Coordinamento tra GUI e logica di dominio (Catalogo, Anagrafica, Registro).
 * - Gestione della persistenza dei dati (Caricamento/Salvataggio).
 * - Validazione dei dati in ingresso tramite ValidatoreDati.
 * - Sincronizzazione del logging tra file su disco e visualizzazione a schermo.
 */
public class GUIController {
    
    private IArchivioDati archivio;
    private ILogger logger;
    private IAutenticatore autenticatore;
    private ValidatoreDati valida;

    // Servizi del Dominio
    private Catalogo catalogo;
    private Anagrafica anagrafica;
    private RegistroPrestiti registro;
    
    // Lista osservabile per aggiornare la tabella dei log in tempo reale
    private ObservableList<String> observableLog; 
    
    // Nomi dei file di persistenza
    private static final String FILE_LIBRI = "libri.dat";
    private static final String FILE_UTENTI = "utenti.dat";
    private static final String FILE_PRESTITI = "prestiti.dat";

    
    /**
     * @brief Costruttore: Inietta tutte le dipendenze e inizializza il logging.
     * 
     * @param[in] archivio Implementazione per la persistenza dei dati.
     * @param[in] logger Implementazione per la registrazione degli eventi.
     * @param[in] autenticatore Modulo per la verifica delle credenziali.
     * @param[in] catalogo Gestore del catalogo libri.
     * @param[in] anagrafica Gestore degli utenti.
     * @param[in] registro Gestore dei prestiti.
     * @param[in] valida Componente utility per la validazione dei formati.
     * 
     * @post
     * - I log esistenti vengono caricati dal file.
     * - Viene creata una ObservableList invertita (dal più recente al più vecchio) per la GUI.
     */
    public GUIController(IArchivioDati archivio, ILogger logger, IAutenticatore autenticatore, Catalogo catalogo, Anagrafica anagrafica, RegistroPrestiti registro, ValidatoreDati valida){
        this.archivio = archivio;
        this.logger = logger;
        this.autenticatore = autenticatore;
        this.catalogo = catalogo;
        this.anagrafica = anagrafica;
        this.registro = registro;
        this.valida = valida;
        
        
        List<String> logsDalFile = logger.visualizzaLog();
        
        // Crea una NUOVA lista temporanea copiando i dati (per non toccare l'originale del logger)
        List<String> logsPerGui = new ArrayList<>();
        if (logsDalFile != null) {
            logsPerGui.addAll(logsDalFile);
        }
        
        // Inverti la copia
        Collections.reverse(logsPerGui);
        
        // Inizializza la lista osservabile della GUI con la lista invertita
        this.observableLog = FXCollections.observableArrayList(logsPerGui);
    }
    
    /**
     * @brief Metodo helper per la sincronizzazione dei Log.
     * 
     * Scrive il log sia sul sistema di backend (file) che sulla lista osservabile
     * collegata alla GUI.
     * 
     * @param[in] messaggio Stringa descrittiva dell'evento.
     * 
     * @post
     * - Backend: Il messaggio è accodato al file di log.
     * - Frontend: Il messaggio è inserito in posizione 0 (in cima) nella tabella della GUI.
     */
    private void scriviLog(String messaggio) {
        // Backend: Salva in coda (ordine cronologico corretto per il file)
        logger.registraAzione(messaggio);
        
        // Frontend: Aggiunge in CIMA (indice 0) per visualizzare l'ultimo evento in alto
        if (observableLog != null) {
            observableLog.add(0, messaggio);
        }
    }

    
    /**
     * @brief Avvia il sistema caricando i dati persistenti.
     * 
     * @post
     * - I file "libri.dat", "utenti.dat", "prestiti.dat" vengono deserializzati.
     * - Le dipendenze circolari (Registro -> Catalogo/Anagrafica) vengono risolte.
     * - In caso di errore, l'eccezione viene catturata e loggata.
     * 
     * @return true se il caricamento ha successo, false altrimenti.
     */
    public boolean avviaSistema(){
        try {
            scriviLog("--- AVVIO SISTEMA ---");

            // Carica CATALOGO
            Object catObj = archivio.caricaStato(FILE_LIBRI);
            if (catObj instanceof Catalogo) {
                this.catalogo = (Catalogo) catObj;
            }

            // Carica ANAGRAFICA
            Object anagObj = archivio.caricaStato(FILE_UTENTI);
            if (anagObj instanceof Anagrafica) {
                this.anagrafica = (Anagrafica) anagObj;
            }

            // Carica REGISTRO PRESTITI
            Object regObj = archivio.caricaStato(FILE_PRESTITI);
            if (regObj instanceof RegistroPrestiti) {
                this.registro = (RegistroPrestiti) regObj;
            }

            // RICOLLEGAMENTO DIPENDENZE
            this.registro.setCatalogo(this.catalogo);
            this.registro.setAnagrafica(this.anagrafica);

            scriviLog("Dati caricati correttamente.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            scriviLog("Errore durante il caricamento dati: " + e.getMessage());
            return false;
        }
    }
    
    
    /**
     * @brief Esegue la procedura di shutdown sicuro del sistema.
     * 
     * @post
     * - Lo stato corrente di Catalogo, Anagrafica e Registro viene serializzato su file.
     * - Il buffer dei log viene scaricato su file di testo.
     * 
     * @return true se il salvataggio ha successo, false altrimenti.
     */
    public boolean chiudiSistema(){
        try {
            scriviLog("--- CHIUSURA SISTEMA ---");

            archivio.salvaStato(catalogo, FILE_LIBRI);
            archivio.salvaStato(anagrafica, FILE_UTENTI);
            archivio.salvaStato(registro, FILE_PRESTITI);
            
            // Salva il Log completo su file
            archivio.salvaStato(logger.visualizzaLog(), AuditTrail.NOME_FILE_LOG);

            return true;
        } catch (Exception e) {
            // Qui usiamo logger diretto perché la GUI si sta chiudendo
            logger.registraAzione("Errore salvataggio chiusura: " + e.getMessage());
            return false;
        }
    }

    
    /**
     * @brief Aggiunge un nuovo utente al sistema.
     * 
     * @param[in] u Oggetto Utente da aggiungere.
     * 
     * @pre
     * - Matricola deve rispettare il formato (10 cifre).
     * - Email deve essere valida.
     * - Nome e Cognome devono contenere solo caratteri alfabetici.
     * 
     * @post
     * - Se valido e non duplicato: l'utente è aggiunto e l'azione è loggata.
     * - Se invalido o duplicato: l'operazione è annullata e l'errore loggato.
     * 
     * @return true se l'aggiunta ha successo.
     */
    public boolean aggiungiUtente(Utente u){
        if (u == null) return false;
        
        if (!valida.validaMatricola(u.getMatricola())) return false;
        if (!valida.validaEmail(u.getEmail())) return false;
        if (!valida.validaNomeCognome(u.getNome(), u.getCognome())) return false;
        
        boolean esito = anagrafica.aggiungiUtente(u);
        
        if (esito){
            scriviLog("Aggiunto nuovo utente: " + u.getMatricola());
        }
        else{
            scriviLog("Tentativo aggiunta utente fallito (Duplicato): " + u.getMatricola());
        }
        
        return esito;
    }
    
    
    /**
     * @brief Modifica i dati di un utente esistente.
     * 
     * @param[in] matrOriginale Matricola attuale dell'utente (chiave di ricerca).
     * @param[in] uNuovo Oggetto contenente i nuovi dati.
     * 
     * @pre
     * - I nuovi dati devono superare la validazione formale.
     * - Se si tenta di cambiare la Matricola, l'utente NON deve avere prestiti attivi.
     * 
     * @return true se la modifica ha successo.
     */
    public boolean modificaUtente(String matrOriginale, Utente uNuovo){
        if (matrOriginale == null || uNuovo == null) return false;
        
        if (!valida.validaMatricola(uNuovo.getMatricola())) return false;
        if (!valida.validaEmail(uNuovo.getEmail())) return false;
        if (!valida.validaNomeCognome(uNuovo.getNome(), uNuovo.getCognome())) return false;
        
        // Se la matricola è cambiata, verifico che non ci siano prestiti sulla vecchia
        if (!matrOriginale.equals(uNuovo.getMatricola())) {
            if (registro.haPrestitiAttivi(matrOriginale)) {
                scriviLog("Modifica UTENTE fallita: " + matrOriginale + " ha prestiti attivi. Impossibile cambiare Matricola.");
                return false;
            }
        }
        
        boolean esito = anagrafica.modificaUtente(matrOriginale, uNuovo);
        
        if (esito){
            scriviLog("Modificato utente: " + matrOriginale);
        }
        return esito;
    }
    
    
    /**
     * @brief Rimuove un utente dal sistema.
     * 
     * @param[in] matr Matricola dell'utente da rimuovere.
     * 
     * @pre 
     * - L'utente non deve avere prestiti attivi (libri non restituiti).
     * 
     * @post
     * - Se non ha prestiti: rimosso dall'anagrafica.
     * 
     * @return true se rimosso correttamente, false se ha pendenze o non esiste.
     */
    public boolean rimuoviUtente(String matr){
        if (!valida.validaMatricola(matr)) return false;
        
        if (registro.haPrestitiAttivi(matr)) {
            scriviLog("Rimozione utente " + matr + " BLOCCATA: ha prestiti attivi.");
            return false;
        }
        
        boolean esito = anagrafica.rimuoviUtente(matr);
        
        if (esito){
            scriviLog("Rimosso utente: " + matr);
        }
        return esito;
    }

    
    /**
     * @brief Esegue una ricerca tra gli utenti registrati.
     * 
     * @param[in] query La stringa da cercare.
     * @param[in] campo Il criterio di ricerca.
     * 
     * @return Una lista di utenti che soddisfano i criteri di ricerca.
     */
    public List<Utente> cercaUtente(String query, String campo){
        return anagrafica.cercaUtente(query, campo);
    }

    
    /**
     * @brief Restituisce l'intera anagrafica utenti.
     * 
     * @return Una lista di tutti gli utenti ordinata (default per Cognome).
     */
    public List<Utente> ottieniAnagraficaOrdinata(){
        return anagrafica.visualizzaOrdinata();
    }
    
    
    /**
     * @brief Aggiunge un nuovo libro al catalogo.
     * 
     * @param[in] l Oggetto Libro da aggiungere.
     * 
     * @pre
     * - ISBN valido.
     * - Anno pubblicazione valido.
     * - Titolo non vuoto.
     * 
     * @return true se aggiunto con successo.
     */
    public boolean aggiungiLibro(Libro l){
        if (l == null) return false;
        
        if (!valida.validaISBN(l.getISBN())) return false;
        if (!valida.validaAnnoPubblicazione(l.getAnno())) return false;
        if (l.getTitolo() == null || l.getTitolo().isEmpty()) return false;
        
        boolean esito = catalogo.aggiungiLibro(l);
        
        if (esito){
            scriviLog("Aggiunto libro: " + l.getISBN());
        }
        else{
            scriviLog("Tentativo aggiunta libro fallito (Duplicato): " + l.getISBN());
        }
        
        return esito;
    }
    
    
    /**
     * @brief Modifica i dati di un libro esistente.
     * 
     * @param[in] isbnOriginale ISBN attuale del libro.
     * @param[in] lNuovo Oggetto con i nuovi dati.
     * 
     * @pre
     * - Se l'ISBN cambia, il libro NON deve essere attualmente in prestito.
     * 
     * @return true se la modifica ha successo.
     */
    public boolean modificaLibro(String isbnOriginale, Libro lNuovo){
        if (isbnOriginale == null || lNuovo == null) return false;
        
        if (!valida.validaISBN(lNuovo.getISBN())) return false;
        if (!valida.validaAnnoPubblicazione(lNuovo.getAnno())) return false;
        
        // Se l'ISBN è cambiato, verifico che il vecchio libro non sia in prestito
        if (!isbnOriginale.equals(lNuovo.getISBN())) {
            List<Prestito> attivi = registro.getPrestitiAttivi();
            for(Prestito p : attivi){
                if(p.getLibro().getISBN().equals(isbnOriginale)){
                    scriviLog("Modifica LIBRO fallita: " + isbnOriginale + " è in prestito. Impossibile cambiare ISBN.");
                    return false;
                }
            }
        }
        
        boolean esito = catalogo.modificaLibro(isbnOriginale, lNuovo);
        
        if (esito){
            scriviLog("Modificato libro: " + isbnOriginale);
        }
        return esito;
    }
    
    
    /**
     * @brief Rimuove un libro dal catalogo.
     * 
     * @param[in] isbn Codice del libro da rimuovere.
     * 
     * @pre
     * - Il libro NON deve essere attualmente in prestito.
     * 
     * @return true se rimosso, false se in uso o non trovato.
     */
    public boolean rimuoviLibro(String isbn){
        if (!valida.validaISBN(isbn)) return false;
        
        List<Prestito> attivi = registro.getPrestitiAttivi();
        for(Prestito p : attivi){
            if(p.getLibro().getISBN().equals(isbn)){
                scriviLog("Rimozione libro " + isbn + " BLOCCATA: è attualmente in prestito.");
                return false;
            }
        }
        
        boolean esito = catalogo.rimuoviLibro(isbn);
        
        if (esito){
            scriviLog("Rimosso libro: " + isbn);
        }
        return esito;
    }

    
    /**
     * @brief Esegue una ricerca nel catalogo libri.
     * 
     * @param[in] query La stringa da cercare.
     * @param[in] campo Il criterio.
     * 
     * @return Una lista di libri che soddisfano i criteri.
     */
    public List<Libro> cercaLibro(String query, String campo){
        return catalogo.ricerca(query, campo);
    }
    
    
    /**
     * @brief Restituisce l'intero catalogo libri.
     * 
     * @return Una lista di tutti i libri ordinata (default per Titolo).
     */
    public List<Libro> ottieniCatalogoOrdinato(){
        return catalogo.visualizzaOrdinata();
    }

    
    /**
     * @brief Registra un nuovo prestito.
     * 
     * @param[in] isbn ISBN del libro richiesto.
     * @param[in] matricola Matricola dell'utente richiedente.
     * @param[in] dataPrevistaRestituzione Data di scadenza del prestito.
     * 
     * @pre
     * - ISBN e Matricola validi nel formato.
     * - Utente non deve aver superato il limite di prestiti (5).
     * - Libro deve avere copie disponibili (> 0).
     * 
     * @post
     * - Crea un oggetto Prestito.
     * - Decrementa le copie disponibili del libro.
     * - Salva lo stato del catalogo su file.
     * 
     * @return true se il prestito è registrato con successo.
     */
    public boolean gestisciPrestito(String isbn, String matricola, LocalDate dataPrevistaRestituzione){
        if (!valida.validaISBN(isbn) || !valida.validaMatricola(matricola)) {
            return false;
        }
        
        Prestito p = registro.registraPrestito(isbn, matricola, dataPrevistaRestituzione);
        archivio.salvaStato(catalogo, FILE_LIBRI);
        
        if (p != null) {
            scriviLog("NUOVO PRESTITO: ID " + p.getIdPrestito() + " | Libro: " + isbn + " | Utente: " + matricola);
            return true;
        } else {
            scriviLog("Prestito FALLITO (Limiti superati o Libro non disponibile): " + isbn + " -> " + matricola);
            return false;
        }
    }
    
    
    /**
     * @brief Registra la restituzione di un libro.
     * 
     * @param[in] idPrestito ID univoco del prestito.
     * @param[in] dataEffettivaRestituzione Data in cui avviene la riconsegna.
     * 
     * @post
     * - Aggiorna la data di restituzione nel prestito.
     * - Incrementa le copie disponibili del libro.
     * - Aggiorna il file del catalogo.
     * 
     * @return true se la restituzione è registrata, false se ID non trovato o già chiuso.
     */
    public boolean gestisciRestituzione(int idPrestito, LocalDate dataEffettivaRestituzione){
        boolean esito = registro.registraRestituzione(idPrestito, dataEffettivaRestituzione);
        
        if (esito) {
            scriviLog("RESTITUZIONE REGISTRATA: ID " + idPrestito);
        } else {
            scriviLog("Errore Restituzione: Prestito ID " + idPrestito + " non trovato.");
        }
        
        archivio.salvaStato(catalogo, FILE_LIBRI);
        return esito;
    }
    
    
    /**
     * @brief Recupera la lista dei prestiti attivi nel sistema.
     * 
     * Questo metodo è utilizzato per popolare la tabella dei prestiti nella Dashboard.
     * Delega la chiamata al RegistroPrestiti.
     * 
     * @return Una lista di oggetti Prestito non ancora conclusi (senza data restituzione effettiva).
     */
    public List<Prestito> ottieniReportPrestiti(){
        return registro.getPrestitiAttivi();
    }

    
    /**
     * @brief Restituisce la lista osservabile dei log per il data-binding.
     * 
     * @return ObservableList contenente le stringhe di log (Audit Trail).
     */
    public ObservableList<String> ottieniAuditTrail(){
        return (ObservableList<String>)observableLog; 
    }
    
    
    /**
     * @brief Gestisce l'autenticazione dell'operatore.
     * 
     * @param[in] user Username.
     * @param[in] pass Password.
     * 
     * @return true se le credenziali sono valide.
     */
    public boolean gestisciLogin(String user, String pass){
        boolean esito = autenticatore.verificaCredenziali(user, pass);
        if (esito) scriviLog("Login effettuato: " + user);
        else scriviLog("Login fallito: " + user);
        return esito;
    }
}