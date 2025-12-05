/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;

import it.unisa.sgbu.domain.*;
import it.unisa.sgbu.io.*;
import java.util.*;
import java.time.*;

/**
 *
 * @author dilie
 */
public class RegistroPrestiti {
    
    private List<Prestito> prestitiAttivi;
    private final int LimitePrestiti=3;
    private Catalogo catologo;
    private Anagrafica anagrafica;
    
    public RegistroPrestiti(Catalogo catologo, Anagrafica anagrafica){
    }
    
    public Prestito registraPrestito(String isbn, String matr, LocalDate dataPrev){
    }
    
    public boolean registraRestituzione(String idPrestito, LocalDate dataEff){
    }
    
    public List<Prestito> getPrestitiAttivi(){
    }
    
    public List<Prestito> getPrestitiAttivi(Utente u){
    }
    
    public List<Prestito> getPrestitiInRitardo(){
    }
    
    public boolean haPrestitiAttivi(String matricola){
    }
    
    public Prestito trovaPrestito(int idPrestito){
    }
    
}
