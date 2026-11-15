package fr.esgi.banque.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    @DisplayName("F2 : Les frais de tenue de compte sont de 2,00 € par mois")
    void shouldReturnTwoEurosForMonthlyAccountFees() {
        // Arrange
        int ageTitulaire = 30;
        boolean cartePremium = false;

        // Act
        BigDecimal result = calculFrais.fraisMensuels(
                ageTitulaire,
                SOLDE_MOYEN,
                cartePremium
        );

        // Assert
        assertThat(result).isEqualByComparingTo("2.00");
    }

    @Test
    @DisplayName("F3 : Les frais de tenue de compte par mois sont gratuits pour les moins de 26 ans")
    void shouldReturnZeroFeesForTitulaireUnder26YearsOld() {
        // Arrange
        int ageTitulaire = 25;
        boolean cartePremium = false;

        // Act
        BigDecimal result = calculFrais.fraisMensuels(
                ageTitulaire,
                SOLDE_MOYEN,
                cartePremium
        );

        // Assert
        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("F3 : À 26 ans, les frais de tenue ne sont plus gratuits")
    void shouldReturnMonthlyAccountFeesWhenTitularIs26YearsOld() {
        // Arrange
        int ageTitulaire = 26;
        boolean cartePremium = false;

        // Act
        BigDecimal result = calculFrais.fraisMensuels(
                ageTitulaire,
                SOLDE_MOYEN,
                cartePremium
        );

        // Assert
        assertThat(result).isEqualByComparingTo("2.00");
    }
}
