/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

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
public class ValidatoreDatiTest {
    
    public ValidatoreDatiTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of validaISBN method, of class ValidatoreDati.
     */
    @Test
    public void testValidaISBN() {
        System.out.println("validaISBN");
        String isbn = "";
        ValidatoreDati instance = new ValidatoreDati();
        boolean expResult = false;
        boolean result = instance.validaISBN(isbn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validaMatricola method, of class ValidatoreDati.
     */
    @Test
    public void testValidaMatricola() {
        System.out.println("validaMatricola");
        String matricola = "";
        ValidatoreDati instance = new ValidatoreDati();
        boolean expResult = false;
        boolean result = instance.validaMatricola(matricola);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validaEmail method, of class ValidatoreDati.
     */
    @Test
    public void testValidaEmail() {
        System.out.println("validaEmail");
        String email = "";
        ValidatoreDati instance = new ValidatoreDati();
        boolean expResult = false;
        boolean result = instance.validaEmail(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validaAnnoPubblicazione method, of class ValidatoreDati.
     */
    @Test
    public void testValidaAnnoPubblicazione() {
        System.out.println("validaAnnoPubblicazione");
        int anno = 0;
        ValidatoreDati instance = new ValidatoreDati();
        boolean expResult = false;
        boolean result = instance.validaAnnoPubblicazione(anno);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validaNomeCognome method, of class ValidatoreDati.
     */
    @Test
    public void testValidaNomeCognome() {
        System.out.println("validaNomeCognome");
        String nome = "";
        String cognome = "";
        ValidatoreDati instance = new ValidatoreDati();
        boolean expResult = false;
        boolean result = instance.validaNomeCognome(nome, cognome);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
