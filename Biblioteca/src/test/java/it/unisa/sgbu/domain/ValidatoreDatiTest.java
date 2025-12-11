/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ValidatoreDatiTest {
    
    private ValidatoreDati validatore;
    
    /**
     * @brief Fixture di test: Configurazione iniziale.
     * Questo metodo viene eseguito automaticamente prima di ogni singolo test.
     * Si occupa di istanziare un nuovo oggetto ValidatoreDati
     * per garantire che i test siano indipendenti l'uno dall'altro.
     */
    @BeforeEach
    public void setUp() {
        validatore = new ValidatoreDati();
    }

    /**
     * @brief Test validaISBN.
     * Regola: Codice non vuoto che rispetta un formato base (cifre e trattini).
     */
    @Test
    public void testValidaISBN() {
        
        // Casi Validi
        assertTrue(validatore.validaISBN("978-0-13-468599-1"));
        assertTrue(validatore.validaISBN("0134685997123"));
        
        // Casi Invaidi
        assertFalse(validatore.validaISBN(""), "ISBN vuoto non valido");
        assertFalse(validatore.validaISBN(null));
    }
    
    /**
     * @brief Test validaMatricola.
     * Regola: Deve essere composta esattamente da 10 cifre numeriche.
     */
    @Test
    public void testValidaMatricola() {
        // Caso Valido
        assertTrue(validatore.validaMatricola("0123456789"), "Matricola 10 cifre deve essere valida");
        
        // Casi Invalidi
        assertFalse(validatore.validaMatricola("123"), "Matricola corta deve fallire");
        assertFalse(validatore.validaMatricola("012345678900"), "Matricola lunga deve fallire");
        assertFalse(validatore.validaMatricola("ABC1234567"), "Matricola con lettere deve fallire");
        assertFalse(validatore.validaMatricola(null), "Matricola null deve fallire");
        assertFalse(validatore.validaMatricola(""), "Matricola vuota deve fallire");
    }
    
    /**
     * @brief Test validaEmail.
     * Regola: Formato standard x@y.z
     */
    @Test
    public void testValidaEmail() {
        
        // Casi Validi
        assertTrue(validatore.validaEmail("mario.rossi@unisa.it"));
        assertTrue(validatore.validaEmail("test@gmail.com"));
        
        // Casi Invalidi
        assertFalse(validatore.validaEmail("mariorossi.it"), "Manca la chiocciola");
        assertFalse(validatore.validaEmail("mario@.it"), "Dominio incompleto");
        assertFalse(validatore.validaEmail("@gmail.com"), "Manca l'utente");
        assertFalse(validatore.validaEmail(null));
    }
    
    /**
     * @brief Test validaAnnoPubblicazione.
     * Regola: 0 <= anno <= Anno Corrente.
     */
    @Test
    public void testValidaAnnoPubblicazione() {
        
        int annoCorrente = LocalDate.now().getYear();
        
        // Casi Validi
        assertTrue(validatore.validaAnnoPubblicazione(2000));
        assertTrue(validatore.validaAnnoPubblicazione(annoCorrente), "L'anno corrente deve essere valido");
        
        // Casi Invalidi
        assertFalse(validatore.validaAnnoPubblicazione(-100), "Anno negativo non valido");
        assertFalse(validatore.validaAnnoPubblicazione(annoCorrente + 1), "Anno futuro non valido");
    }
    
    /**
     * @brief Test validaNomeCognome.
     * Regola: Solo caratteri alfabetici (ammettiamo spazi e apostrofi per nomi reali).
     */
    @Test
    public void testValidaNomeCognome() {
        
        // Casi Validi
        assertTrue(validatore.validaNomeCognome("Mario", "Rossi"));
        assertTrue(validatore.validaNomeCognome("De Luca", "D'Amico"), "Spazi e apostrofi dovrebbero essere accettati");
        
        // Casi Invalidi
        assertFalse(validatore.validaNomeCognome("Mario123", "Rossi"), "Numeri non ammessi");
        assertFalse(validatore.validaNomeCognome("Mario", ""), "Stringa vuota non ammessa");
        assertFalse(validatore.validaNomeCognome(null, "Rossi"), "Null non ammesso");
    }
    
}
