/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.biblioteca.Class;

/**
 *
 * @author alessandro
 */
public class Utente {
    
    private final String matricola;
    private String nome;
    private String cognome;
    private String email;
    private .... prestitiAttivi;
    
    public Utente(String matricola, String nome, String cognome, String email, .... prestitiAttivi){
        
        this.matricola=matricola;
        this.nome=nome;
        this.cognome=cognome;
        this.email=email;
        this.prestitiAttivi=prestitiAttivi;
        
    }

    public String getMatricola() {
        return matricola;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }
    
    public ... getPrestitiAttivi(){
        return;
    }
    
    
}
