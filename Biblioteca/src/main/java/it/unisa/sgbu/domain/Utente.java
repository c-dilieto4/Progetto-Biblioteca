/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.util.*;

/**
 *
 * @author alessandro
 */
public class Utente {
    
    private final String matricola;
    private String nome;
    private String cognome;
    private String email;
    private List<Prestito> prestitiAttivi;
    
    public Utente(String matricola, String nome, String cognome, String email,List<Prestito> prestitiAttivi){   
    }

    public String getMatricola() {
    }
    
    public String getCognome() {
    }

    public int getNumeroPrestitiAttivi(){
    }
    
    public void aggiungiPrestito(Prestito p){
    }
    
    public void rimuoviPrestito(Prestito p){
    }
    
    public boolean verificaLimite(){
    }
    
    
}
