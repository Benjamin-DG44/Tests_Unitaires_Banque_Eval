# Rendu

## Identité

**Etudiant** : Benjamin DELAUNAY-GUITTON **Campus** : Nantes

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