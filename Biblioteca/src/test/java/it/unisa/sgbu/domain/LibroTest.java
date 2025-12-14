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


public class LibroTest {
    
    private Libro libro;
    
    /**
     * @brief Fixture di test: Configurazione iniziale.
     * Questo metodo viene eseguito automaticamente prima di ogni singolo test.
     * Si occupa di istanziare un nuovo oggetto Libro "pulito" (con 2 copie totali)
     * per garantire che i test siano indipendenti l'uno dall'altro.
     */
    @BeforeEach
    public void setUp() {
        List<String> autori = new ArrayList<>();
        autori.add("Joshua Bloch");
        
        // Creo un libro con 2 copie totali
        libro = new Libro("978-0134685991", "Effective Java", autori, 2018, 2);
    }
    

    /**
     * @brief Test della coerenza dello stato iniziale.
     * @test Verifica che, subito dopo l'istanziazione dell'oggetto tramite costruttore:
     *   Il numero di copie disponibili sia esattamente uguale al numero di copie totali.
     */
    @Test
    public void testInizializzazioneCopie() {
        // Appena creato, le copie disponibili devono essere uguali a quelle totali
        assertEquals(2, libro.getCopieDisponibili(), "Le copie disponibili iniziali devono essere 2");
    }
    
    /**
     * @brief Test dei metodi di accesso ai dati anagrafici (Getter).
     * @test Verifica che i metodi getISBN, getTitolo, getAutore e getCopieTotali
     * restituiscano esattamente i valori passati al costruttore durante il setup.
     */
    @Test
    public void testMetodiGetter() {
        // Verifica ISBN
        assertEquals("978-0134685991", libro.getISBN(), "L'ISBN non corrisponde");
        
        // Verifica Titolo
        assertEquals("Effective Java", libro.getTitolo(), "Il titolo non corrisponde");
        
        // Verifica Autore (Gestione della Lista)
        assertNotNull(libro.getAutore(), "La lista autori non deve essere null");
        assertEquals(1, libro.getAutore().size(), "Deve esserci un solo autore");
        assertEquals("Joshua Bloch", libro.getAutore().get(0), "Il nome dell'autore non corrisponde");
        
        // Verifica Copie Totali e Disponibili
        assertEquals(2, libro.getCopieTotali(), "Le copie totali non corrispondono");
        assertEquals(2, libro.getCopieDisponibili(), "getCopieDisponibili iniziale deve essere uguale al totale");
        
        assertEquals(2018, libro.getAnno(), "L'anno di pubblicazione non corrisponde");
    }

    /**
     * @brief Test of incrementaDisponibilità method.
     * Verifica che il contatore salga correttamente dopo una restituzione.
     */
    @Test
    public void testIncrementaDisponibilità() {
        // Preparazione: Porto le copie a 1
        libro.decrementaDisponibilità(); 
        assertEquals(1, libro.getCopieDisponibili());
        
        // Test: Incremento
        libro.incrementaDisponibilità();
        assertEquals(2, libro.getCopieDisponibili(), "Le copie dovrebbero tornare a 2");
        
        // Test di robustezza: provo a incrementare oltre il totale (2)
        libro.incrementaDisponibilità();
        assertEquals(2, libro.getCopieDisponibili(), "Le copie non devono superare il totale");
    }

    /**
     * @brief Test of decrementaDisponibilità method.
     * Verifica che il contatore scenda correttamente.
     */
    @Test
    public void testDecrementaDisponibilità() {
        // Stato iniziale: 2 copie
        libro.decrementaDisponibilità();
        assertEquals(1, libro.getCopieDisponibili(), "Dovrebbe rimanere 1 copia");
        
        libro.decrementaDisponibilità();
        assertEquals(0, libro.getCopieDisponibili(), "Dovrebbero rimanere 0 copie");
        
        // Test di robustezza: provo a decrementare ancora
        libro.decrementaDisponibilità(); 
        assertEquals(0, libro.getCopieDisponibili(), "Le copie non devono scendere sotto zero");
    }

    /**
     * @brief Test of isDisponibile method (Caso Limite).
     * Verifica che il libro NON sia disponibile quando le copie arrivano a 0.
     */
    @Test
    public void testIsDisponibile() {
        // Caso 1: Ci sono copie (2) -> Deve essere true
        assertTrue(libro.isDisponibile(), "Il libro dovrebbe essere disponibile");
        
        // Caso 2: Esaurisco le copie -> Deve essere false
        libro.decrementaDisponibilità();
        libro.decrementaDisponibilità();
        assertFalse(libro.isDisponibile(), "Il libro non dovrebbe essere disponibile con 0 copie");
    }
    
    
    /**
     * @brief Test del metodo toString().
     * @test Verifica che il metodo toString restituisca l'ISBN del libro,
     * come ci si aspetta per la visualizzazione nelle interfacce testuali o di log.
     */
    @Test
    public void testToString() {
        // Il metodo toString() è stato sovrascritto per restituire l'ISBN
        String risultatoAtteso = "978-0134685991";
        
        assertEquals(risultatoAtteso, libro.toString(), "Il metodo toString() dovrebbe restituire l'ISBN del libro");
    }
    
}
