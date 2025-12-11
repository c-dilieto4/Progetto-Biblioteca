/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;

import it.unisa.sgbu.domain.Libro;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CatalogoTest {
    
    private Catalogo catalogo;
    private Libro l1;
    private Libro l2;
    private Libro l3;
    
    
    /**
     * @brief Fixture di test: Configurazione iniziale.
     * Questo metodo viene eseguito automaticamente prima di ogni singolo test.
     * Si occupa di istanziare un nuovo oggetto Catalogo.
     */
    @BeforeEach
    public void setUp() {
        catalogo = new Catalogo();
        
        // Creazione Libro 1
        List<String> aut1 = new ArrayList<>();
        aut1.add("Joshua Bloch");
        l1 = new Libro("978-0134685991", "Effective Java", aut1, 2018, 5);
        
        // Creazione Libro 2
        List<String> aut2 = new ArrayList<>();
        aut2.add("Robert C. Martin");
        l2 = new Libro("978-0132350884", "Clean Code", aut2, 2008, 3);
        
        // Creazione Libro 3
        List<String> aut3 = new ArrayList<>();
        aut3.add("Gamma");
        aut3.add("Helm"); // Più autori
        l3 = new Libro("111-2223334445", "Design Patterns", aut3, 1994, 2);
    }

    /**
     * @brief Test Aggiunta Libro.
     * Verifica inserimento corretto e gestione duplicati.
     */
    @Test
    public void testAggiungiLibro() {
        
        // Inserimento valido
        assertTrue(catalogo.aggiungiLibro(l1));
        assertNotNull(catalogo.getLibro("978-0134685991"));
        
        // Inserimento duplicato (stesso ISBN)
        assertFalse(catalogo.aggiungiLibro(l1), "Non deve inserire ISBN duplicati");
        
        // Null
        assertFalse(catalogo.aggiungiLibro(null));
    }

    /**
     * @brief Test Rimuovi Libro.
     */
    @Test
    public void testRimuoviLibro() {
        catalogo.aggiungiLibro(l1);
        
        // Rimozione valida
        assertTrue(catalogo.rimuoviLibro(l1.getISBN()));
        assertNull(catalogo.getLibro(l1.getISBN()));
        
        // Rimozione non esistente
        assertFalse(catalogo.rimuoviLibro("0000000000"));
    }

    /**
     * @brief Test Ricerca.
     * Verifica i filtri per ISBN, Titolo e Autore.
     */
    @Test
    public void testRicerca() {
        catalogo.aggiungiLibro(l1); // Effective Java, Bloch
        catalogo.aggiungiLibro(l2); // Clean Code, Martin
        catalogo.aggiungiLibro(l3); // Design Patterns, Gamma/Helm
        
        // Cerca per Titolo (parziale)
        List<Libro> res = catalogo.ricerca("Clean", "Titolo");
        assertEquals(1, res.size());
        assertEquals("Clean Code", res.get(0).getTitolo());
        
        // Cerca per Autore (uno dei tanti)
        res = catalogo.ricerca("Helm", "Autore");
        assertEquals(1, res.size());
        assertEquals("Design Patterns", res.get(0).getTitolo());
        
        // Cerca per ISBN
        res = catalogo.ricerca("978-0134685991", "ISBN");
        assertEquals(1, res.size());
        assertEquals("Effective Java", res.get(0).getTitolo());
        
        // Non trovato
        res = catalogo.ricerca("Harry Potter", "Titolo");
        assertTrue(res.isEmpty());
    }

    /**
     * @brief Test Modifica Libro.
     */
    @Test
    public void testModificaLibro() {
        catalogo.aggiungiLibro(l1);
        
        // Caso 1: Modifica semplice (Titolo)
        List<String> aut = new ArrayList<>();
        aut.add("Joshua Bloch");
        Libro l1Mod = new Libro("978-0134685991", "Effective Java 3rd Edition", aut, 2018, 5);
        
        assertTrue(catalogo.modificaLibro(l1.getISBN(), l1Mod));
        assertEquals("Effective Java 3rd Edition", catalogo.getLibro(l1.getISBN()).getTitolo());
        
        // Caso 2: Cambio ISBN
        Libro l1NewIsbn = new Libro("999-9999999999", "Effective Java", aut, 2018, 5);
        assertTrue(catalogo.modificaLibro("978-0134685991", l1NewIsbn));
        
        assertNull(catalogo.getLibro("978-0134685991"), "Vecchio ISBN deve sparire");
        assertNotNull(catalogo.getLibro("999-9999999999"), "Nuovo ISBN deve esistere");
    }

    /**
     * @brief Test Ordinamento.
     * Verifica l'ordine alfabetico per Titolo.
     * Ordine atteso: Clean Code (C), Design Patterns (D), Effective Java (E).
     */
    @Test
    public void testVisualizzaOrdinata() {
        System.out.println("Test Visualizza Ordinata");
        
        // Inserisco in ordine casuale
        catalogo.aggiungiLibro(l1); // E
        catalogo.aggiungiLibro(l3); // D
        catalogo.aggiungiLibro(l2); // C
        
        List<Libro> ordinata = catalogo.visualizzaOrdinata();
        
        assertEquals(3, ordinata.size());
        assertEquals("Clean Code", ordinata.get(0).getTitolo());
        assertEquals("Design Patterns", ordinata.get(1).getTitolo());
        assertEquals("Effective Java", ordinata.get(2).getTitolo());
    }
    
}
