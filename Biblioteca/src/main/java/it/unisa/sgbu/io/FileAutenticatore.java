/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.io;

/**
 * @brief Implementazione del meccanismo di autenticazione basato su file.
 * 
 * Questa classe verifica le credenziali del Bibliotecario leggendole da un
 * supporto di memorizzazione persistente. Soddisfa il requisito Non Funzionale
 * di Sicurezza, garantendo che l'accesso alle funzionalità di gestione
 * sia limitato al solo personale autorizzato (Attore: Bibliotecario).
 */
public class FileAutenticatore implements IAutenticatore{
    
    private final String credentialsFile;
    private final IArchivioDati archivioDati;
    
    
    /**
     * @brief Costruttore della classe FileAutenticatore.
     * 
     * Configura il sistema di autenticazione.
     * 
     * @param[in] credentialsFile Il percorso del file dove risiedono username e password corretti.
     * @param[in] archivioDati L'interfaccia per la lettura del file.
     */
    public FileAutenticatore(String credentialsFile, IArchivioDati archivioDati){
        this.credentialsFile = credentialsFile;
        this.archivioDati = archivioDati;
    }
    
    
    /**
     * @brief Verifica la validità delle credenziali fornite.
     * 
     * Implementa il controllo di sicurezza. Confronta l'input fornito
     * con le credenziali memorizzate su file.
     * Il successo di questa operazione è la Pre-condizione necessaria per
     * l'esecuzione di tutti i casi d'uso gestionali.
     * 
     * @param[in] user Il nome utente inserito dall'operatore.
     * @param[in] pass La password inserita dall'operatore.
     * 
     * @return true se le credenziali corrispondono a quelle autorizzate, false altrimenti.
     * 
     * @pre
     * - Il file delle credenziali deve esistere ed essere leggibile.
     * 
     * @post
     * - Se restituisce true, il Bibliotecario è considerato "Autenticato" nel sistema.
     */
    @Override
    public boolean verificaCredenziali(String user, String pass){
        return false;
    }
}
