package fr.esgi.banque.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CalculFraisTest {

    private final BigDecimal SOLDE_MOYEN = new BigDecimal("100.00");

    private CalculFrais calculFrais;

    @BeforeEach
    void setUp() {
        calculFrais = new CalculFrais();
    }

    @Test
    @DisplayName("F1 : Un titulaire mineur déclenche une IllegalArgumentException")
    void shouldThrowIllegalArgumentExceptionWhenTitulaireIsMinor() {
        // Act and Assert
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class,
                () -> calculFrais.fraisMensuels(CalculFrais.AGE_MAJORITE - 1, SOLDE_MOYEN, false));
        assertEquals("Le titulaire doit être majeur", result.getMessage());
    }

    @Test
    @DisplayName("F1 : Un titulaire majeur avec un solde renseigné est accepté")
    void shouldNotThrowIllegalArgumentExceptionWhenTitulaireIsMajor() {
        // Act and Assert
        assertDoesNotThrow(
                () -> calculFrais.fraisMensuels(CalculFrais.AGE_MAJORITE, SOLDE_MOYEN, false)
        );
    }

    @Test
    @DisplayName("F1 : Un solde moyen null déclenche une IllegalArgumentException")
    void shouldThrowIllegalArgumentExceptionWhenSoldeMoyenIsNull() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculFrais.fraisMensuels(CalculFrais.AGE_MAJORITE, null, false)
        );

        // Assert
        assertEquals("Le solde moyen est obligatoire", exception.getMessage());
    }
}
