/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.io;

/**
 * @brief Interfaccia per la gestione della persistenza dei dati.
 * 
 * Definisce il contratto per l'interazione con l'archivio dati esterno (sistema operativo/file system),
 * come richiesto dal requisito di Interfaccia Software.
 * Questa astrazione permette al sistema di effettuare il salvataggio persistente e il ricaricamento
 * dello stato (Libri, Utenti, Prestiti) indipendentemente dalla tecnologia sottostante,
 * supportando il caso d'uso "Gestione persistenza dati".
 */
public interface IArchivioDati {
    
    
    /**
     * @brief Salva lo stato di un oggetto in modo persistente.
     * 
     * Implementa l'operazione di salvataggio richiesta alla chiusura dell'applicazione.
     * L'implementazione concreta di questo metodo deve garantire la Tolleranza agli errori:
     * in caso di fallimento della scrittura, l'archivio precedente non deve essere corrotto.
     * 
     * @param[in] dati L'oggetto contenente lo stato da salvare (es. l'intera collezione di dati).
     * @param[in] nomeFile Il nome o percorso della destinazione di salvataggio.
     * 
     * @return true se l'operazione è completata con successo, false altrimenti.
     */
    public boolean salvaStato(Object dati, String nomeFile){
    }
 
    
    /**
     * @brief Carica lo stato di un oggetto dall'archivio.
     * 
     * Implementa l'operazione di ricaricamento richiesta all'avvio dell'applicazione.
     * Si basa sull'Assunzione n°2 presupponendo che l'archivio abbia una struttura
     * definita e leggibile dall'applicazione.
     * 
     * @param[in] nomeFile Il nome o percorso del file sorgente.
     * 
     * @return L'oggetto recuperato, oppure null se il caricamento fallisce.
     */
    public Object caricaStato(String nomeFile){
    }
    
    
    /**
     * @brief Verifica la presenza fisica dell'archivio dati.
     * 
     * Metodo di supporto utilizzato per gestire i flussi alternativi del relativo Caso d'Uso:
     * se il file non esiste, il sistema deve essere in grado di avviarsi con un archivio vuoto
     * senza generare errori critici.
     * 
     * @param[in] nomeFile Il nome o percorso del file da controllare.
     * 
     * @return true se il file esiste, false altrimenti.
     */
    public boolean verificaEsistenzaFile(String nomeFile){
    }
}
