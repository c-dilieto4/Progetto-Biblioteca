/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.io;

/**
 * @brief Interfaccia per il servizio di autenticazione del sistema.
 * 
 * Definisce il contratto per la verifica dell'identità dell'operatore.
 * Questa interfaccia è fondamentale per soddisfare il requisito non funzionale
 * di Sicurezza, garantendo che l'accesso alla GUI sia riservato
 * esclusivamente al Bibliotecario.
 */
public interface IAutenticatore {
    
    
    /**
     * @brief Verifica la validità delle credenziali di accesso.
     * 
     * Metodo essenziale per permettere il login del Bibliotecario.
     * Il successo di questo metodo è la pre-condizione necessaria per
     * l'accesso a tutti i casi d'uso di gestione.
     * 
     * @param[in] user Il nome utente fornito in input.
     * @param[in] pass La password fornita in input.
     * 
     * @return true se le credenziali sono corrette e l'accesso è autorizzato, false altrimenti.
     */
    public boolean verificaCredenziali(String user, String pass);
}
