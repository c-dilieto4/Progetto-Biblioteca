/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author dilie
 */
public class LibroTest {
    
    private Libro libro;
    
    @BeforeEach
    public void setUp() {
        List<String> autori = new ArrayList<>();
        autori.add("Joshua Bloch");
        
        // Creo un libro con 2 copie totali
        libro = new Libro("978-0134685991", "Effective Java", autori, 2018, 2);
    }
    

    @Test
    public void testInizializzazioneCopie() {
        System.out.println("Test Inizializzazione Copie");
        // Verifica che le copie disponibili siano uguali a quelle totali (2)
        assertEquals(2, libro.getCopieDisponibili(), "Le copie disponibili iniziali errate");
        // Verifica che il libro risulti disponibile
        assertTrue(libro.isDisponibile(), "Il libro dovrebbe essere disponibile");
    }

    /**
     * Test of incrementaDisponibilità method.
     * Verifica che il contatore salga correttamente dopo una restituzione.
     */
    @Test
    public void testIncrementaDisponibilità() {
        System.out.println("Test incrementaDisponibilità");
        
        // Preparazione: decremento prima per poter incrementare
        libro.decrementaDisponibilità(); // copie = 1
        
        // Azione: restituzione
        libro.incrementaDisponibilità(); // copie = 2
        
        assertEquals(2, libro.getCopieDisponibili(), "Le copie dovrebbero tornare al massimo (2)");
    }

    /**
     * Test of decrementaDisponibilità method.
     * Verifica che il contatore scenda correttamente.
     */
    @Test
    public void testDecrementaDisponibilità() {
        System.out.println("Test decrementaDisponibilità");
        
        libro.decrementaDisponibilità();
        
        assertEquals(1, libro.getCopieDisponibili(), "Le copie dovrebbero essere 1 dopo un prestito");
        assertTrue(libro.isDisponibile(), "Con 1 copia il libro è ancora disponibile");
    }

    /**
     * Test of isDisponibile method (Caso Limite).
     * Verifica che il libro NON sia disponibile quando le copie arrivano a 0.
     */
    @Test
    public void testIsDisponibile_Falso_CopieEsaurite() {
        System.out.println("Test isDisponibile (Copie Esaurite)");
        
        // Simulo due prestiti per esaurire le copie
        libro.decrementaDisponibilità();
        libro.decrementaDisponibilità();
        
        assertEquals(0, libro.getCopieDisponibili());
        assertFalse(libro.isDisponibile(), "Il libro non deve essere disponibile con 0 copie");
    }
    
}
