package fr.esgi.banque.business;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test d'exemple fourni pour vérifier que le projet est bien configuré.
 * À compléter (partie A).
 */
class CompteTest {

    public static final String NUMERO_VALIDE = "FR001";
    public static final String TITULAIRE_VALIDE = "Alice";
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

    @ParameterizedTest(name = "Cas {index} : soldeInitial={0}, decouvertAutorise={1}")
    @DisplayName("C2 : Le solde initial et le découvert autorisé sont obligatoires et le découvert doit être >= 0")
    @CsvSource({
            // soldeInitial, decouvertAutorise
            "              , 200.00",  // soldeInitial null
            "100.00        ,       ",  // decouvertAutorise null
            "              ,       ",  // les deux null
            "100.00        , -50.00"   // decouvertAutorise négatif
    })
    void shouldThrowIllegalArgumentExceptionWhenSoldeOrDecouvertIsInvalid(
            BigDecimal soldeInitial,
            BigDecimal decouvertAutorise) {

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Compte(NUMERO_VALIDE, TITULAIRE_VALIDE, soldeInitial, decouvertAutorise)
        );
    }

    @ParameterizedTest(name = "Cas {index} : montant={0}")
    @NullSource
    @ValueSource(strings = {"0.00", "-0.01", "-10.00"})
    @DisplayName("C3 : Créditer avec un montant négatif, nul, ou null")
    void shouldThrowIllegalArgumentExceptionWhenCreditingInvalidMontant(BigDecimal montantInvalide) {
        // Arrange
        Compte compte = new Compte(NUMERO_VALIDE, TITULAIRE_VALIDE, SOLDE_INITIAL, DECOUVERT_AUTORISE);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> compte.crediter(montantInvalide)
        );

        assertEquals("Le montant à créditer doit être strictement positif", exception.getMessage());
    }

    @Test
    void shouldIncreaseBalanceWhenCrediting() {
        // Arrange
        Compte compte = new Compte(NUMERO_VALIDE, TITULAIRE_VALIDE, SOLDE_INITIAL, DECOUVERT_AUTORISE);

        // Act
        compte.crediter(new BigDecimal("50.00"));

        // Assert
        assertThat(compte.getSolde()).isEqualByComparingTo("150.00");
    }

    @Test
    @DisplayName("C5 : Un débit valide diminue le solde en le laissant positif")
    void shouldDecreaseBalanceWhenDebitingLeavesPositiveBalance() {
        // Arrange
        Compte compte = new Compte(NUMERO_VALIDE, TITULAIRE_VALIDE, SOLDE_INITIAL, DECOUVERT_AUTORISE);

        // Act
        compte.debiter(new BigDecimal("50.00"));

        // Assert
        assertThat(compte.getSolde()).isEqualByComparingTo("50.00");
    }

    @Test
    @DisplayName("C5 : Un débit égal au solde ramène le solde à zéro")
    void shouldSetBalanceToZeroWhenDebitingExactBalance() {
        // Arrange
        Compte compte = new Compte(NUMERO_VALIDE, TITULAIRE_VALIDE, SOLDE_INITIAL, DECOUVERT_AUTORISE);

        // Act
        compte.debiter(new BigDecimal("100.00"));

        // Assert
        assertThat(compte.getSolde()).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("C5 : Un débit dans la limite du découvert autorisé rend le solde négatif")
    void shouldDecreaseBalanceWhenDebitingWithinOverdraftLimit() {
        // Arrange
        Compte compte = new Compte(NUMERO_VALIDE, TITULAIRE_VALIDE, SOLDE_INITIAL, DECOUVERT_AUTORISE);

        // Act
        compte.debiter(new BigDecimal("250.00"));

        // Assert
        assertThat(compte.getSolde()).isEqualByComparingTo("-150.00");
    }

    @Test
    @DisplayName("C5 : Un débit atteignant exactement la limite du découvert autorisé est accepté")
    void shouldAllowDebitWhenReachingExactOverdraftLimit() {
        // Arrange (100.00 - 300.00 = -200.00, soit -DECOUVERT_AUTORISE)
        Compte compte = new Compte(NUMERO_VALIDE, TITULAIRE_VALIDE, SOLDE_INITIAL, DECOUVERT_AUTORISE);

        // Act
        compte.debiter(new BigDecimal("300.00"));

        // Assert
        assertThat(compte.getSolde()).isEqualByComparingTo("-200.00");
    }

    @Test
    @DisplayName("C5 : Un débit au-delà du découvert autorisé lève SoldeInsuffisantException sans modifier le solde")
    void shouldThrowSoldeInsuffisantExceptionWhenExceedingOverdraftLimit() {
        Compte compte = new Compte(NUMERO_VALIDE, TITULAIRE_VALIDE, SOLDE_INITIAL, DECOUVERT_AUTORISE);
        BigDecimal montantTropEleve = new BigDecimal("500.00");

        // Act & Assert
        assertThrows(
                SoldeInsuffisantException.class,
                () -> compte.debiter(montantTropEleve)
        );

        // Vérifie que le solde n'a pas changé
        assertThat(compte.getSolde()).isEqualByComparingTo(SOLDE_INITIAL);
    }
}
