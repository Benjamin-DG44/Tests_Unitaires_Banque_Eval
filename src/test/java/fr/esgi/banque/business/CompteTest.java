package fr.esgi.banque.business;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test d'exemple fourni pour vérifier que le projet est bien configuré.
 * À compléter (partie A).
 */
class CompteTest {

    public static final BigDecimal SOLDE_INITIAL = new BigDecimal("100.00");
    public static final BigDecimal DECOUVERT_AUTORISE = new BigDecimal("200");

    @ParameterizedTest(name = "Cas {index} : numero={0}, titulaire={1} -> {2}")
    @DisplayName("C1 : Un numéro et un titulaire sont obligatoires")
    @CsvSource({
            // numero,   titulaire, messageAttendu
            "        ,   Alice,     Le numéro de compte est obligatoire", // numero null
            "''      ,   Alice,     Le numéro de compte est obligatoire", // numero vide
            "'   '   ,   Alice,     Le numéro de compte est obligatoire", // numero blank (espaces)
            "FR001   ,        ,     Le titulaire est obligatoire",        // titulaire null
            "FR001   ,   ''   ,     Le titulaire est obligatoire",        // titulaire vide
            "FR001   ,   '   ',     Le titulaire est obligatoire",        // titulaire blank (spaces)
            "        ,        ,     Le numéro de compte est obligatoire", // les deux null
            "''      ,   ''   ,     Le numéro de compte est obligatoire",  // les deux vides
            "'   '      ,   '   '   ,     Le numéro de compte est obligatoire"  // les deux vides (spaces)
    })
    void shouldThrowIllegalArgumentExceptionWhenNumeroOrTitulaireIsInvalid(
            String numero,
            String titulaire,
            String expectedMessage) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Compte(numero, titulaire, SOLDE_INITIAL, DECOUVERT_AUTORISE)
        );

        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    void shouldIncreaseBalanceWhenCrediting() {
        // Arrange
        Compte compte = new Compte("FR001", "Alice", SOLDE_INITIAL, DECOUVERT_AUTORISE);

        // Act
        compte.crediter(new BigDecimal("50.00"));

        // Assert
        assertThat(compte.getSolde()).isEqualByComparingTo("150.00");
    }

}
