/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;


import it.unisa.sgbu.domain.*;
import it.unisa.sgbu.io.*;
import java.util.*;

/**
 *
 * @author dilie
 */
public class Anagrafica {
    
    private Map<String, Utente> registroUtenti;
    private IArchivioDati archivio;
    private ILogger logger;
    
    public Anagrafica(IArchivioDati archivio, ILogger logger){
    }
    
    public boolean aggiungiUtente(Utente u){
    }
    
    public boolean rimuoviUtente(String matiricola){
    }
    
    public boolean modificaUtente(String matiricola, Utente u){
    }
    
    public Utente getUtente(String matiricola){
    }
    
    public List<Utente> cercaUtente(String query, String campo){
    }
    
    public List<Utente> visualizzaOrdinata(){
    }
}
