/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

/**
 * @brief Rappresenta le credenziali di accesso al sistema.
 * 
 * Questa classe incapsula le informazioni di autenticazione necessarie per
 * il login. È destinata all'uso esclusivo dell'attore Bibliotecario,
 * l'unico autorizzato ad accedere all'interfaccia grafica per eseguire operazioni di gestione.
 * 
 * Implementa la struttura dati richiesta per soddisfare il requisito di
 * sicurezza sull'Autenticazione.
 */
public class Credenziali {
    private final String user;
    private final String password;
    
    
    /**
     * @brief Costruttore della classe Credenziali.
     * 
     * Crea un oggetto credenziali immutabile da utilizzare durante la fase
     * di login all'avvio dell'applicazione o della sessione di lavoro.
     * 
     * @param[in] user L'identificativo dell'operatore (Bibliotecario).
     * @param[in] password La chiave di accesso segreta.
     * 
     * @pre
     * - user e password non devono essere nulli o vuoti per permettere 
     * un tentativo di autenticazione valido.
     * 
     * @post
     * - L'oggetto Credenziali è inizializzato.
     */
    public Credenziali(String user, String password){
    }
    
    
    /**
     * @brief Restituisce il nome utente.
     * @return Una stringa contenente lo username.
     */
    public String getUser(){
    }
    
    
    /**
     * @brief Restituisce la password.
     * @return Una stringa contenente la password.
     */
    public String getPassword(){
    }
}
