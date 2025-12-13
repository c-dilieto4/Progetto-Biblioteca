/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

/**
 * @brief Classe contenitore per le costanti stringa dell'interfaccia utente.
 * * Questa classe centralizza tutti i messaggi di feedback (successo, errore, avviso)
 * mostrati all'utente tramite la GUIView.
 */
public class MessaggiInterfaccia {
    
    // --- MESSAGGI GENERICI E DI SISTEMA ---
    
    public static final String AVVISO_CARICAMENTO_FALLITO = "Caricamento Dati Fallito. Creazione Archivio Vuoto";
    public static final String ERRORE_GENERICO_SALVATAGGIO = "Impossibile completare l'operazione. Verificare i dati e riprovare.";
    public static final String CREDENZIALI_NON_VALIDE = "Le credenziali inserite non sono valide, riprova";

    // --- SEZIONE LIBRI ---

    /** @param %s Il titolo del libro. */
    public static final String SUCCESSO_AGGIUNTA_LIBRO = "Il libro '%s' è stato aggiunto al catalogo.";
    
    public static final String SUCCESSO_MODIFICA_LIBRO = "Libro modificato con successo.";
    public static final String SUCCESSO_RIMOZIONE_LIBRO = "Libro rimosso.";
    
    public static final String ERRORE_SELEZIONE_LIBRO = "Seleziona prima un libro dalla tabella!";
    
    /** @param %s L'ISBN. @param %s Il titolo esistente. */
    public static final String ERRORE_ISBN_DUPLICATO = "L'ISBN '%s' è già registrato nel catalogo per il libro '%s'.";
    
    public static final String ERRORE_FORMATO_ISBN = "Errore: Formato ISBN non valido.";
    public static final String ERRORE_MODIFICA_LIBRO = "Modifica fallita (ISBN duplicato o errore dati).";
    public static final String ERRORE_RIMOZIONE_LIBRO = "❌ Errore: Libro non trovato o in prestito.";
    public static final String ERRORE_ASSENZA_COPIE = "Nessuna copia disponibile del libro '%s' nel catalogo.";
    public static final String NESSUN_LIBRO_TROVATO = "Nessun libro trovato."; // O "con %s: %s" se usi format

    // --- SEZIONE UTENTI ---

    public static final String SUCCESSO_AGGIUNTA_UTENTE = "Utente registrato con successo.";
    public static final String SUCCESSO_MODIFICA_UTENTE = "Utente modificato con successo.";
    public static final String SUCCESSO_RIMOZIONE_UTENTE = "Utente rimosso.";
    
    public static final String ERRORE_SELEZIONE_UTENTE = "Seleziona prima un utente dalla tabella!";
    
    /** @param %s La matricola. */
    public static final String ERRORE_MATRICOLA_DUPLICATA = "La matricola '%s' è già presente nell'anagrafe.";
    
    public static final String ERRORE_MODIFICA_UTENTE = "Modifica fallita.\nCause possibili:\n1. Matricola duplicata.\n2. L'utente ha prestiti attivi.";
    public static final String ERRORE_RIMOZIONE_UTENTE = "❌ Errore: Utente non trovato o ha prestiti attivi.";
    public static final String NESSUN_UTENTE_TROVATO = "Nessun utente trovato.";

    // --- VALIDAZIONE CAMPI UTENTE ---

    public static final String INPUT_EMAIL_NON_VALIDO = "Formato e-mail non valido.";
    public static final String INPUT_EMAIL_DETTAGLI = "\n(Formato errato o doppi punti '..')";
    public static final String INPUT_NOME_NON_VALIDO = "Nome non valido.";
    public static final String INPUT_COGNOME_NON_VALIDO = "Cognome non valido.";
    public static final String INPUT_ANNO_NON_VALIDO = "Anno non valido.";
    public static final String INPUT_MATRICOLA_NON_VALIDO = "Formato matricola non valido.";

    // --- SEZIONE PRESTITI ---

    /** @param %d L'ID del prestito. */
    public static final String SUCCESSO_PRESTITO = "Il prestito (ID: %d) è stato registrato.";
    
    public static final String SUCCESSO_RESTITUZIONE = "Restituzione effettuata.";
    
    /** @param %s Nome. @param %s Cognome. */
    public static final String ERRORE_LIMITE_PRESTITO = "L'utente %s %s ha raggiunto il limite massimo di prestiti attivi.";
    
    public static final String ERRORE_REGISTRAZIONE_PRESTITO = "❌ Errore Registrazione Prestito.\n"
                        + "Possibili cause:\n"
                        + "1. Copie del libro esaurite.\n"
                        + "2. L'utente ha raggiunto il limite prestiti.\n"
                        + "3. ISBN o Matricola errati/inesistenti.";
    
    public static final String ERRORE_RESTITUZIONE_NON_TROVATO = "Errore: Prestito non trovato o ID errato.";
    public static final String ERRORE_ID_NON_NUMERICO = "Errore: L'ID deve essere un numero.";
    
    /** @param %d L'ID del prestito. */
    public static final String RITARDO_SEGNALATO = "Il prestito (ID: %d) è in ritardo (superata data prevista di restituzione).";

}