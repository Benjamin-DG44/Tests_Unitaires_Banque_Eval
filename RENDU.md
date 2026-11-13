# Rendu

## Identité

**Etudiant** : Benjamin DELAUNAY-GUITTON **Campus** : Nantes

## Partie A : tester et corriger l'existant

### Liste des défauts

#### Défaut C3 : crediter()

La méthode crediter() ne rejette pas l'erreur IllegalArgumentException pour les montants nuls. La méthode de test :
shouldThrowIllegalArgumentExceptionWhenCreditingInvalidMontant()