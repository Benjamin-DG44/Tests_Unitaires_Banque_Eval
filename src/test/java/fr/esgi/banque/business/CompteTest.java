package fr.esgi.banque.business;

import org.junit.jupiter.api.BeforeEach;
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

class CompteTest {

    private static final String NUMERO_VALIDE = "FR001";
    private static final String TITULAIRE_VALIDE = "Alice";
    private static final BigDecimal SOLDE_INITIAL = new BigDecimal("100.00");
    private static final BigDecimal DECOUVERT_AUTORISE = new BigDecimal("200.00");

    private Compte compteValide;

    @BeforeEach
    void setUp() {
        compteValide = new Compte(
                NUMERO_VALIDE,
                TITULAIRE_VALIDE,
                SOLDE_INITIAL,
                DECOUVERT_AUTORISE
        );
    }

    @ParameterizedTest(name = "Cas {index} : numero={0}, titulaire={1}")
    @DisplayName("C1 : Un numéro et un titulaire sont obligatoires")
    @CsvSource({
            // numero, titulaire, message attendu
            "        , Alice,     Le numéro de compte est obligatoire",
            "''      , Alice,     Le numéro de compte est obligatoire",
            "'   '   , Alice,     Le numéro de compte est obligatoire",
            "FR001   ,        ,     Le titulaire est obligatoire",
            "FR001   , ''     ,     Le titulaire est obligatoire",
            "FR001   , '   '  ,     Le titulaire est obligatoire",
            "        ,        ,     Le numéro de compte est obligatoire",
            "''      , ''     ,     Le numéro de compte est obligatoire",
            "'   '   , '   '  ,     Le numéro de compte est obligatoire"
    })
    void shouldThrowIllegalArgumentExceptionWhenNumeroOrTitulaireIsInvalid(
            String numero,
            String titulaire,
            String expectedMessage
    ) {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Compte(numero, titulaire, SOLDE_INITIAL, DECOUVERT_AUTORISE)
        );

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest(name = "Cas {index} : soldeInitial={0}, decouvertAutorise={1}")
    @DisplayName("C2 : Le solde initial et le découvert autorisé sont obligatoires et le découvert doit être >= 0")
    @CsvSource({
            // solde initial, découvert autorisé, message attendu
            "              , 200.00, Le solde initial et le découvert autorisé sont obligatoires",
            "100.00        ,       , Le solde initial et le découvert autorisé sont obligatoires",
            "              ,       , Le solde initial et le découvert autorisé sont obligatoires",
            "100.00        , -50.00, Le découvert autorisé doit être positif ou nul"
    })
    void shouldThrowIllegalArgumentExceptionWhenSoldeOrDecouvertIsInvalid(
            BigDecimal soldeInitial,
            BigDecimal decouvertAutorise,
            String expectedMessage
    ) {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Compte(NUMERO_VALIDE, TITULAIRE_VALIDE, soldeInitial, decouvertAutorise)
        );

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest(name = "Cas {index} : montant={0}")
    @NullSource
    @ValueSource(strings = {"0.00", "-0.01", "-10.00"})
    @DisplayName("C3 : Créditer avec un montant négatif, nul ou null")
    void shouldThrowIllegalArgumentExceptionWhenCreditingInvalidMontant(BigDecimal montantInvalide) {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> compteValide.crediter(montantInvalide)
        );

        // Assert
        assertEquals(
                "Le montant à créditer doit être strictement positif",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("C4 : Créditer un montant positif augmente le solde du compte")
    void shouldIncreaseBalanceWhenCrediting() {
        // Act
        compteValide.crediter(new BigDecimal("50.00"));

        // Assert
        assertThat(compteValide.getSolde()).isEqualByComparingTo("150.00");
    }

    @ParameterizedTest(name = "C5 : {0} (débit de {1} -> solde attendu {2})")
    @DisplayName("C5 : Un débit valide met à jour le solde, y compris jusqu'à la limite exacte du découvert")
    @CsvSource({
            "Diminue le solde en le laissant positif,       50.00,   50.00",
            "Ramène le solde à zéro,                       100.00,    0.00",
            "Rend le solde négatif dans le découvert,      250.00, -150.00",
            "Atteint exactement la limite du découvert,    300.00, -200.00"
    })
    void shouldDecreaseBalanceWhenDebitingWithinOverdraftLimit(
            String scenario,
            BigDecimal montantADebiter,
            BigDecimal soldeAttendu
    ) {
        // Act
        compteValide.debiter(montantADebiter);

        // Assert
        assertThat(compteValide.getSolde()).isEqualByComparingTo(soldeAttendu);
    }

    @Test
    @DisplayName("C5 : Un débit au-delà du découvert autorisé lève SoldeInsuffisantException sans modifier le solde")
    void shouldThrowSoldeInsuffisantExceptionWhenExceedingOverdraftLimit() {
        // Act
        assertThrows(
                SoldeInsuffisantException.class,
                () -> compteValide.debiter(new BigDecimal("500.00"))
        );

        // Assert
        assertThat(compteValide.getSolde()).isEqualByComparingTo(SOLDE_INITIAL);
    }

    @ParameterizedTest(name = "Cas {index} : solde={0} -> compte vide")
    @ValueSource(strings = {"0", "0.0", "0.00", "0.000", "0.0000"})
    @DisplayName("C6 : Un compte est vide lorsque son solde est égal à zéro")
    void shouldReturnTrueWhenBalanceIsZeroRegardlessOfScale(String solde) {
        // Arrange
        Compte compte = new Compte(
                NUMERO_VALIDE,
                TITULAIRE_VALIDE,
                new BigDecimal(solde),
                DECOUVERT_AUTORISE
        );

        // Act
        boolean result = compte.estVide();

        // Assert
        assertThat(result).isTrue();
    }

    @ParameterizedTest(name = "Cas {index} : solde={0} -> compte non vide")
    @ValueSource(strings = {"0.01", "10.00", "-10.00", "-0.01"})
    @DisplayName("C6 : Un compte n'est pas vide lorsque son solde est différent de zéro")
    void shouldReturnFalseWhenBalanceIsNotZero(String solde) {
        // Arrange
        Compte compte = new Compte(
                NUMERO_VALIDE,
                TITULAIRE_VALIDE,
                new BigDecimal(solde),
                DECOUVERT_AUTORISE
        );

        // Act
        boolean result = compte.estVide();

        // Assert
        assertThat(result).isFalse();
    }

    @ParameterizedTest(name = "Cas {index} : solde={0} -> estADecouvert={1}")
    @CsvSource({
            "-200.00, true",
            "-150.00, true",
            "-0.01,   true",
            "0.00,    false",
            "0.01,    false",
            "50.00,   false",
            "100.00,  false"
    })
    @DisplayName("C7 : Un compte est à découvert lorsque son solde est strictement négatif")
    void shouldDetermineIfAccountIsOverdrawn(String solde, boolean expectedResult) {
        // Arrange
        Compte compte = new Compte(
                NUMERO_VALIDE,
                TITULAIRE_VALIDE,
                new BigDecimal(solde),
                DECOUVERT_AUTORISE
        );

        // Act
        boolean result = compte.estADecouvert();

        // Assert
        assertThat(result).isEqualTo(expectedResult);
    }
}
