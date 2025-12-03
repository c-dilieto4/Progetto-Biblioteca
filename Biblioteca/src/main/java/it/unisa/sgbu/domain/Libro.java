/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.time.*;
import java.util.List;
        
/**
 *
 * @author alessandro
 */
public class Libro {
    
    private final String ISBN;
    private String titolo;
    private List<String> autore;
    private int annoPubblicazione;
    private int copieTotali;
    private int copieDisponibili;
    
    
    
    public Libro(String ISBN, String titolo,List<String> autore, int annoPubblicazione, int copieTotali){      
    }

    
    
    public String getISBN() {
    }

    public String getTitolo() {
    }

    public String getAutore() {
    }

    public int getCopieTotali() {
    }

    public int getCopieDisponibili() {
    }
    
    public void incrementaDisponibilità(){
    }
    
    public void decrementaDisponibilità(){
    }
    
    public boolean isDisponibile(){
    }
}
