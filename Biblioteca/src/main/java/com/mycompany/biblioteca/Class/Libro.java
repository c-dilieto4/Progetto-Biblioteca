/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.biblioteca.Class;

import java.time.*;
        
/**
 *
 * @author alessandro
 */
public class Libro {
    
    private final String ISBN;
    private String titolo;
    private String autore;
    private LocalDate annoPubblicazione;
    private int copieTotali;
    private int copieDisponibili;
    
    public Libro(String ISBN, String titolo, LocalDate annoPubblicazione, int copieTotali){
        
        this.ISBN=ISBN;
        this.titolo=titolo;
        this.annoPubblicazione=annoPubblicazione;
        this.copieTotali=copieTotali;
        this.copieDisponibili=copieTotali;
        
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public LocalDate getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public int getCopieTotali() {
        return copieTotali;
    }

    public int getCopieDisponibili() {
        return copieDisponibili;
    }
    
}
