# Rendu

## Identité

**Etudiant** : Benjamin DELAUNAY-GUITTON </br>
**Campus** : Nantes

## Partie A : tester et corriger l'existant

### Liste des défauts

#### Défaut C3 : crediter()

La méthode crediter() ne rejette pas l'erreur IllegalArgumentException pour les montants nuls. La méthode de test :
shouldThrowIllegalArgumentExceptionWhenCreditingInvalidMontant()

#### Défaut C5 : debiter()

La méthode debiter() ne permet pas d'atteindre la limite du découvert autorisé pour le solde. La méthode de test :
shouldAllowDebitWhenReachingExactOverdraftLimit()

#### Défaut C6 : estVide()

La méthode estVide() ne renvoie pas `true` pour un solde de 0. La méthode de test : shouldReturnTrueWhenBalanceIsZero()

#### Défaut F3 : fraisMensuel()

La méthode fraisMensuel() renvoie `0` euros alors que le titulaire à 26 ans. La méthode de test est :
shouldReturnMonthlyAccountFeesWhenTitularIs26YearsOld()

#### Défaut F4 : fraisMensuel()

La méthode fraisMensuel() renvoie `2.00` euros au lieu de `0.00` euros quand le solde moyen dépasse 1000 euros ou plus.
La méthode de test : shouldReturnZeroFeesWhenAverageBalanceIsAtLeast1000Euros()

#### Défaut F5 : fraisMensuel()

La méthode fraisMensuel() renvoie `0.00` euros au lieu de `5.00` euros alors que le titulaire détient une carte prenium
La méthode de test : shouldChargeFiveEurosForPremiumCardWhenAccountFeesAreFree()