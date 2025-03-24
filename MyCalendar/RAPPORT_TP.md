# Rapport TP Noté - Maintenance Applicative & TDD en Java

## Résumé des modifications

Dans le cadre de ce TP, j'ai implémenté un nouveau type d'événement (ReminderEvent) dans l'application de calendrier existante, en suivant strictement les principes du TDD et les contraintes imposées.

## Démarche TDD appliquée

J'ai suivi la démarche TDD en trois étapes :

1. **RED** : J'ai d'abord écrit les tests pour le nouveau type d'événement (`ReminderEventTest.java`) qui échouaient initialement puisque la classe n'existait pas encore.
2. **GREEN** : J'ai implémenté le code minimal nécessaire pour faire passer les tests.
3. **REFACTOR** : J'ai amélioré le code tout en m'assurant que les tests continuaient à passer.

## Respect des contraintes

### Utilisation exclusive de Value Objects
- Création d'un nouveau Value Object `MessageEvent` pour encapsuler le message du rappel
- Utilisation des Value Objects existants comme `DateEvent`, `TitleEvent`, `OwnerEvent` et `EventId`
- Aucune primitive nue n'est utilisée dans le domaine métier

### Solution basée sur le polymorphisme
- La classe `ReminderEvent` étend `AbstractEvent` et implémente l'interface `Event`
- Surcharge des méthodes comme `conflictsWith()` et `description()` avec un comportement spécifique
- Utilisation du pattern Strategy dans `EventFactory` pour éviter les conditionnels
- Utilisation de classes spécifiques (`TypeEventExecutor`, `TypeEventMatcher`) pour gérer les comportements variables sans conditionnels

## Modifications apportées

### Nouvelles classes
1. **MessageEvent** : Value Object pour encapsuler le message du rappel
2. **ReminderEvent** : Nouvelle classe d'événement pour les rappels

### Modifications de classes existantes
1. **TypeEvent** : Ajout de l'énumération `RAPPEL`
2. **EventFactory** (domain) : Ajout du support pour créer des ReminderEvent
3. **EventFactory** (UI) : Ajout de la méthode `createReminderEvent` et mise à jour de `createEvent`
4. **EventInputHelper** : Ajout de la méthode `inputReminderEvent`
5. **EventTypeMenu** : 
   - Ajout de la description pour le type RAPPEL
   - Ajout du type RAPPEL à la liste des types d'événements
   - Mise à jour de la méthode `executeAction` pour gérer le type RAPPEL

### Tests
1. **ReminderEventTest** : Tests pour la nouvelle classe ReminderEvent
2. **EventFactoryTest** : Ajout de tests pour la création de ReminderEvent
3. **CalendarManagerTest** : Ajout de tests pour vérifier l'intégration des ReminderEvent avec le CalendarManager

## Problèmes rencontrés et solutions

1. **Problème de compilation** : Lors de l'ajout de la méthode `createReminderEvent` dans la classe UI `EventFactory`, j'ai rencontré une erreur de syntaxe qui empêchait la compilation.
   - **Solution** : J'ai corrigé la syntaxe en supprimant un caractère erroné qui s'était glissé dans le code.

2. **Problème d'intégration avec l'UI** : L'UI n'était pas configurée pour gérer le nouveau type d'événement.
   - **Solution** : J'ai mis à jour la classe `EventTypeMenu` pour ajouter le type RAPPEL à la liste des types d'événements et j'ai modifié la méthode `executeAction` pour gérer ce nouveau type.

3. **Cohérence du polymorphisme** : Il fallait s'assurer que le nouveau type d'événement s'intègre bien dans l'architecture polymorphique existante.
   - **Solution** : J'ai suivi le même modèle que les autres types d'événements, en étendant `AbstractEvent` et en implémentant les méthodes spécifiques.

## Fonctionnalités implémentées

1. **Ajout d'un nouveau type d'événement** : Le type ReminderEvent a été ajouté au calendrier.
2. **Détection des conflits** : Les ReminderEvent ne sont pas considérés comme étant en conflit avec d'autres événements.
3. **Description spécifique** : Chaque ReminderEvent génère une description spécifique incluant le titre, le message et la date.
4. **Suppression par identifiant** : Les ReminderEvent peuvent être supprimés par leur identifiant comme les autres types d'événements.

## Fonctionnalités bonus implémentées

### 1. Sérialisation/Désérialisation vers JSON (+1 point)

J'ai implémenté une fonctionnalité complète de sérialisation/désérialisation JSON pour tous les types d'événements :

- Ajout des dépendances Jackson dans le fichier pom.xml
- Création d'une classe utilitaire `JsonUtils` qui fournit des méthodes pour :
  - Sérialiser un événement ou une liste d'événements en JSON
  - Désérialiser un événement ou une liste d'événements depuis JSON
  - Sauvegarder des événements dans un fichier JSON
  - Charger des événements depuis un fichier JSON
- Création de tests unitaires pour valider le bon fonctionnement de la sérialisation/désérialisation

Cette fonctionnalité permet de sauvegarder et restaurer l'état du calendrier, facilitant ainsi la persistance des données.

### 2. Interface utilisateur minimale (+3 points)

J'ai développé une interface utilisateur graphique complète en utilisant JavaFX :

- Ajout des dépendances JavaFX dans le fichier pom.xml
- Création d'une classe principale `CalendarApp` qui lance l'application
- Implémentation d'un contrôleur `CalendarController` qui gère l'interaction avec l'utilisateur
- Création d'un fichier FXML `CalendarView.fxml` qui définit la structure de l'interface

L'interface utilisateur permet de :
- Visualiser tous les événements dans une table
- Ajouter de nouveaux événements de tous types (y compris le nouveau type ReminderEvent)
- Supprimer des événements existants
- Exporter et importer des événements au format JSON

L'interface s'adapte dynamiquement au type d'événement sélectionné, affichant uniquement les champs pertinents pour ce type.

## Conclusion

L'implémentation du nouveau type d'événement ReminderEvent a été réalisée avec succès en suivant strictement les principes du TDD et les contraintes imposées. Le code est maintenant plus robuste, maintenable et évolutif grâce à l'utilisation de Value Objects et du polymorphisme.

Les tests unitaires garantissent le bon fonctionnement de la nouvelle fonctionnalité et son intégration harmonieuse avec le reste de l'application.

Les fonctionnalités bonus (sérialisation JSON et interface utilisateur) ajoutent une valeur significative à l'application, la rendant plus pratique et conviviale pour l'utilisateur final.
