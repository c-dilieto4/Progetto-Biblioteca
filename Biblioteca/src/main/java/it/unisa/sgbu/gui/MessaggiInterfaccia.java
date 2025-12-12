/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

/**
 * @brief Classe contenitore per le costanti stringa dell'interfaccia utente.
 * 
 * Questa classe centralizza tutti i messaggi di feedback (successo, errore, avviso)
 * mostrati all'utente tramite la GUIView.
 * Alcune stringhe contengono specificatori di formato (%s, %d) e devono essere
 * formattate prima della visualizzazione.
 */
public class MessaggiInterfaccia {
    
    
    /**
     * @brief Messaggio di successo per l'inserimento di un libro.
     * 
     * @param %s Il titolo del libro aggiunto.
     */
    public static final String SUCCESSO_AGGIUNTA_LIBRO = "Il libro '%s' è stato aggiunto al catalogo.";
    
    
    /**
     * @brief Messaggio di successo per la registrazione di un prestito.
     * 
     * @param %d L'ID univoco del prestito appena creato.
     */
    public static final String SUCCESSO_PRESTITO = "Il prestito (ID: %d) è stato registrato.";
    
    
    /**
     * @brief Errore: Limite prestiti raggiunto.
     * 
     * @param %s Nome dell'utente.
     * @param %s Cognome dell'utente.
     */
    public static final String ERRORE_LIMITE_PRESTITO = "L'utente %s %s ha raggiunto il limite massimo di prestiti attivi.";
    
    
    /**
     * @brief Errore: ISBN duplicato.
     * 
     * @param %s Il codice ISBN duplicato.
     * @param %s Il titolo del libro già presente con questo ISBN.
     */
    public static final String ERRORE_ISBN_DUPLICATO = "L'ISBN '%s' è già registrato nel catalogo per il libro '%s'.";
    
    
    /**
     * @brief Errore: Matricola duplicata.
     * @param %s La matricola che viola l'univocità.
     */
    public static final String ERRORE_MATRICOLA_DUPLICATA = "La matricola '%s' è già presente nell'anagrafe.";
    
    
    /**
     * @brief Errore: Copie non disponibili.
     * 
     * @param %s Il titolo del libro richiesto.
     */
    public static final String ERRORE_ASSENZA_COPIE = "Nessuna copia disponibile del libro '%s' nel catalogo.";
    
    
    /**
     * @brief Errore di validazione: Formato Email non corretto.
     */
    public static final String INPUT_EMAIL_NON_VALIDO = "Formato e-mail non valido.";
    
    
    /**
     * @brief Errore di validazione: Nome contiene caratteri non alfabetici.
     */
    public static final String INPUT_NOME_NON_VALIDO = "Nome non valido.";
    
    
    /**
     * @brief Errore di validazione: Cognome contiene caratteri non alfabetici.
     */
    public static final String INPUT_COGNOME_NON_VALIDO = "Cognome non valido.";
    
    
    /**
     * @brief Errore di validazione: Anno pubblicazione futuro o negativo.
     */
    public static final String INPUT_ANNO_NON_VALIDO = "Anno non valido.";
    
    
    /**
     * @brief Errore di validazione: Formato Matricola (10 cifre) non rispettato.
     */
    public static final String INPUT_MATRICOLA_NON_VALIDO = "Formato matricola non valido.";
    
    
    /**
     * @brief Avviso: Segnalazione Ritardo Restituzione.
     * 
     * @param %d L'ID del prestito in ritardo.
     */
    public static final String RITARDO_SEGNALATO = "Il prestito (ID: %d) è in ritardo (superata data prevista di restituzione).";
    
    
    /**
     * @brief Avviso di sistema: Fallimento caricamento dati all'avvio.
     * Indica che il sistema è partito con un archivio vuoto per tolleranza ai guasti.
     */
    public static final String AVVISO_CARICAMENTO_FALLITO = "Caricamento Dati Fallito. Creazione Archivio Vuoto";
    
    /**
     * @brief Errore generico di salvataggio.
     * Usato quando non è possibile determinare la causa specifica (es. prestito fallito per cause multiple).
     */
    public static final String ERRORE_GENERICO_SALVATAGGIO = "Impossibile completare l'operazione. Verificare i dati e riprovare.";
    
    /**
     * @brief Errore: Autenticazione fallita.
     */
    public static final String CREDENZIALI_NON_VALIDE = "Le credenziali inserite non sono valide, riprova";
    
}
