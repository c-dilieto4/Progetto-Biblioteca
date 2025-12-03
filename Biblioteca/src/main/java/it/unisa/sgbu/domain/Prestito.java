/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.time.*;

/**
 *
 * @author dilie
 */
public class Prestito {
    
    private final int idPrestito;
    private Libro libro;
    private Utente utente;
    private LocalDate dataInizio;
    private LocalDate dataPrevistaRestituzione;
    private LocalDate dataEffettivaRestituzione;
    
    
    public Prestito(int idPrestito, Libro libro, Utente utente, LocalDate dataInizio, LocalDate dataPrevistaRestituzione){
    }

    public int getIdPrestito() {
    }

    public Libro getLibro() {
    }

    public Utente getUtente() {
    }
        
    public void chiudiPrestito(LocalDate dataEffettiva){
    }
    
    public boolean verificaRitardo(){
    }
    
    
}
