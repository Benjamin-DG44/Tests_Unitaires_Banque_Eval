# Rendu

## Identité

**Étudiant** : Benjamin DELAUNAY-GUITTON  
**Campus** : Nantes
----

## Partie A : tester et corriger l'existant

### Liste des défauts

#### Défaut C3 : `crediter()`

La méthode `crediter()` ne rejette pas les montants nuls avec une `IllegalArgumentException`.

La méthode de test :  
`shouldThrowIllegalArgumentExceptionWhenCreditingInvalidMontant()`
----

#### Défaut C5 : `debiter()`

La méthode `debiter()` ne permet pas d'atteindre la limite du découvert autorisé pour le solde.

La méthode de test :  
`shouldAllowDebitWhenReachingExactOverdraftLimit()`
----

#### Défaut C6 : `estVide()`

La méthode `estVide()` ne renvoie pas `true` pour un solde de 0.

La méthode de test :  
`shouldReturnTrueWhenBalanceIsZero()`
----

#### Défaut F3 : `fraisMensuels()`

La méthode `fraisMensuels()` renvoie `0` euro alors que le titulaire a 26 ans.

La méthode de test :  
`shouldReturnMonthlyAccountFeesWhenTitularIs26YearsOld()`
----

#### Défaut F4 : `fraisMensuels()`

La méthode `fraisMensuels()` renvoie `2.00` euros au lieu de `0.00` euro quand le solde moyen atteint 1 000 euros ou
plus.

La méthode de test :  
`shouldReturnZeroFeesWhenAverageBalanceIsAtLeast1000Euros()`
----

#### Défaut F5 : `fraisMensuels()`

La méthode `fraisMensuels()` renvoie `0.00` euro au lieu de `5.00` euros alors que le titulaire détient une carte
premium et que les frais de tenue de compte sont offerts.

La méthode de test :

- Avant refactoring : `shouldChargeFiveEurosForPremiumCardWhenAccountFeesAreFree()`
- Après refactoring :`shouldAlwaysChargeFiveEurosForPremiumCard()`

----