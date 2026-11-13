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

    @Test
    @DisplayName("C1 : Un numéro ne doit pas être null ou vide")
    void shouldThrowIllegalArgumentExceptionWhenNumeroIsNullOrAndBlank() {
        // Act and Assert
        IllegalArgumentException resultNumeroNull = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Compte(null, "Alice", SOLDE_INITIAL, DECOUVERT_AUTORISE);
                }
        );
        IllegalArgumentException resultNumeroBlank = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Compte(null, "Alice", SOLDE_INITIAL, DECOUVERT_AUTORISE);
                }
        );
        IllegalArgumentException resultNumeroWithSpaces = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Compte(null, "Alice", SOLDE_INITIAL, DECOUVERT_AUTORISE);
                }
        );

        assertEquals("Le numéro de compte est obligatoire", resultNumeroNull.getMessage());
        assertEquals("Le numéro de compte est obligatoire", resultNumeroBlank.getMessage());
        assertEquals("Le numéro de compte est obligatoire", resultNumeroWithSpaces.getMessage());
    }

    @Test
    @DisplayName("C1 : Un titulaire ne doit pas être null ou vide")
    void shouldThrowIllegalArgumentExceptionWhenTitulaireIsNullOrAndBlank() {
        // Act and Assert
        IllegalArgumentException resultTitulaireNull = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Compte("FR001", null, SOLDE_INITIAL, DECOUVERT_AUTORISE);
                }
        );
        IllegalArgumentException resultTitulaireBlank = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Compte("FR002", "", SOLDE_INITIAL, DECOUVERT_AUTORISE);
                }
        );
        IllegalArgumentException resultTitulaireWithSpaces = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Compte("FR002", "  ", SOLDE_INITIAL, DECOUVERT_AUTORISE);
                }
        );

        assertEquals("Le titulaire est obligatoire", resultTitulaireNull.getMessage());
        assertEquals("Le titulaire est obligatoire", resultTitulaireBlank.getMessage());
        assertEquals("Le titulaire est obligatoire", resultTitulaireWithSpaces.getMessage());
    }

    @Test
    @DisplayName("C1 : Un numéro et un titulaire ne doivent pas être null ou vide")
    void shouldThrowIllegalArgumentExceptionWhenNumeroAndTitulaireIsNullOrAndBlank() {
        // Act and Assert
        IllegalArgumentException resultNumeroNull = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Compte(null, null, SOLDE_INITIAL, DECOUVERT_AUTORISE);
                }
        );
        IllegalArgumentException resultNumeroBlank = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Compte("", "", SOLDE_INITIAL, DECOUVERT_AUTORISE);
                }
        );
        IllegalArgumentException resultNumeroWithSpaces = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Compte("", "", SOLDE_INITIAL, DECOUVERT_AUTORISE);
                }
        );

        assertEquals("Le numéro de compte est obligatoire", resultNumeroNull.getMessage());
        assertEquals("Le numéro de compte est obligatoire", resultNumeroBlank.getMessage());
        assertEquals("Le numéro de compte est obligatoire", resultNumeroWithSpaces.getMessage());
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
