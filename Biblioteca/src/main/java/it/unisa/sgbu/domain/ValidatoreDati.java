/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.time.Year;

/**
 * @brief Classe di utilità per la validazione sintattica e logica dei dati di input.
 * 
 * Questa classe centralizza le regole di validazione definite nella sezione
 * "Dati e Formato Dati [DF]" . 
 * Viene utilizzata dai flussi di registrazione e modifica per garantire
 * l'integrità dei dati prima della persistenza.
 */
public class ValidatoreDati {

    
    /**
     * @brief Verifica il formato del codice ISBN.
     * 
     * Controlla che la stringa fornita sia un identificativo valido secondo le relative specifiche.
     * 
     * @param[in] isbn La stringa rappresentante il codice ISBN.
     * 
     * @return true se il formato è valido, false altrimenti.
     */
    public boolean validaISBN(String isbn){
        // Controlla che non sia null
        // Controlla che contenga solo numeri (0-9) o trattini (-)
        // Controlla che la lunghezza totale sia tra 13 e 17 caratteri
        return isbn != null && isbn.matches("^[0-9-]{13,17}$");
    }

    
    /**
     * @brief Verifica il formato della matricola utente.
     * 
     * Implementa il controllo rigoroso definito nell'Assunzione n°3 e richiamato
     * nello specifico requisito: la matricola deve essere composta esclusivamente
     * da 10 cifre numeriche. L'input di identificativi non conformi non è supportato.
     * 
     * @param[in] matricola La stringa da validare.
     * 
     * @return true se la matricola è numerica e di 10 cifre, false altrimenti.
     */
    public boolean validaMatricola(String matricola){
        return matricola != null && matricola.matches("^\\d{10}$");
    }

    
    /**
     * @brief Verifica il formato dell'indirizzo email.
     * 
     * Controlla che l'email rispetti il formato standard (es. utente@dominio.it)
     * come richiesto dal requisito dei Dati e Formati e verificato nel relativo flusso.
     * 
     * @param[in] email L'indirizzo email da validare.
     * 
     * @return true se il formato è valido, false altrimenti.
     */
    public boolean validaEmail(String email){
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    
    /**
     * @brief Verifica la validità dell'anno di pubblicazione.
     * 
     * Implementa la logica temporale definita dal requisito dei Dati e Formati: l'anno deve essere
     * un intero non negativo e non può essere superiore all'anno corrente
     * rilevato dal sistema operativo.
     * 
     * @param[in] anno L'anno di pubblicazione da verificare.
     * 
     * @return true se 0 <= anno <= anno corrente, false altrimenti.
     */
    public boolean validaAnnoPubblicazione(int anno){
        int annoCorrente = Year.now().getValue();
        // L'anno deve essere positivo e non nel futuro
        return anno >= 0 && anno <= annoCorrente;
    }
    
    
    /**
     * @brief Verifica il formato di Nome e Cognome.
     * 
     * Assicura che i campi contengano esclusivamente caratteri alfabetici,
     * vietando numeri o caratteri speciali, come esplicitamente richiesto
     * dal requisito dei Dati e Formati.
     * 
     * @param[in] nome Il nome da validare.
     * @param[in] cognome Il cognome da validare.
     * 
     * @return true se entrambi contengono solo caratteri alfabetici, false altrimenti.
     */
    public boolean validaNomeCognome(String nome, String cognome){
        if (nome == null || cognome == null) return false;
        
        // Verifica che contengano solo lettere, spazi o apostrofi
        return nome.matches("^[a-zA-Z\\s']+$") && cognome.matches("^[a-zA-Z\\s']+$");
    }
}
