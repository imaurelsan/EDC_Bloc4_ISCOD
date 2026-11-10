# Conception réelle de gamesUP

## Architecture

L'application Spring Boot expose une API REST. Elle utilise Spring Data JPA
pour les repositories et les entités persistées.

Les contrôleurs appellent les services. Les services utilisent les repositories.
Les contrôleurs de jeux utilisent `GameDTO`. Les contrôleurs d'auteurs et
d'éditeurs utilisent aussi leurs DTO.

La sécurité Spring utilise deux utilisateurs en mémoire :

- `client` avec le rôle `CLIENT` ;
- `admin` avec le rôle `ADMIN`.

Les routes de modification des jeux, des comptes et des commandes sont
protégées par `SecurityConfig`. Les créations, modifications et suppressions
des auteurs et éditeurs utilisent aussi `@PreAuthorize` avec le rôle `ADMIN`.

L'API Python est une API FastAPI séparée. Spring communique avec elle par
`RecommandationClient`, avec `RestTemplate`.

## Routes Spring

### Jeux

| Méthode | Chemin | Contrôleur |
|---|---|---|
| GET | `/jeux` | `GameController.getAllJeux` |
| GET | `/jeux/recherche?nom=...` | `GameController.rechercherParNom` |
| GET | `/jeux/recommandations/{idUtilisateur}` | `GameController.recommander` |
| GET | `/jeux/{id}` | `GameController.findById` |
| POST | `/jeux` | `GameController.ajouterJeu` |
| PUT | `/jeux/{id}` | `GameController.modifierJeu` |
| DELETE | `/jeux/{id}` | `GameController.supprimerJeu` |

### Auteurs

| Méthode | Chemin | Contrôleur |
|---|---|---|
| GET | `/auteurs` | `AuthorController.getAll` |
| GET | `/auteurs/{id}` | `AuthorController.findById` |
| POST | `/auteurs` | `AuthorController.enregistrer` |
| PUT | `/auteurs/{id}` | `AuthorController.modifier` |
| DELETE | `/auteurs/{id}` | `AuthorController.supprimer` |

### Éditeurs

| Méthode | Chemin | Contrôleur |
|---|---|---|
| GET | `/editeurs` | `PublisherController.getAll` |
| GET | `/editeurs/{id}` | `PublisherController.findById` |
| POST | `/editeurs` | `PublisherController.enregistrer` |
| PUT | `/editeurs/{id}` | `PublisherController.modifier` |
| DELETE | `/editeurs/{id}` | `PublisherController.supprimer` |

### Utilisateurs

| Méthode | Chemin | Contrôleur |
|---|---|---|
| GET | `/utilisateurs` | `UserController.getAll` |
| GET | `/utilisateurs/me` | `UserController.getCurrentUser` |
| GET | `/utilisateurs/{id}` | `UserController.findById` |
| POST | `/utilisateurs` | `UserController.enregistrer` |
| PUT | `/utilisateurs/{id}` | `UserController.modifier` |
| DELETE | `/utilisateurs/{id}` | `UserController.supprimer` |

### Commandes

| Méthode | Chemin | Contrôleur |
|---|---|---|
| GET | `/commandes` | `PurchaseController.getAll` |
| GET | `/commandes/{id}` | `PurchaseController.findById` |
| POST | `/commandes` | `PurchaseController.enregistrer` |
| PUT | `/commandes/{id}` | `PurchaseController.modifier` |
| DELETE | `/commandes/{id}` | `PurchaseController.supprimer` |

## Routes Python

Dans `CodeApiPython/main.py`, les routes liées aux recommandations sont :

| Méthode | Chemin | Fonction |
|---|---|---|
| POST | `/recommendations/` | `get_recommendations` |
| GET | `/recommandations/{idUtilisateur}` | `recommandations` |
| POST | `/interactions` | `ajouter_interaction` |

La route Python `POST /recommendations/` reçoit un `UserData` et appelle le
modèle KNN. La route `GET /recommandations/{idUtilisateur}` est celle appelée
par Spring.

## Chaîne de recommandation

1. Une interaction est envoyée à `POST /interactions`.
2. FastAPI ajoute l'interaction aux données.
3. Le modèle KNN est réentraîné avec `entrainer_modele`.
4. Spring appelle `GET /recommandations/{idUtilisateur}` avec
   `RecommandationClient`.
5. `GameService.recommander` récupère les identifiants retournés.
6. Chaque identifiant est recherché dans `GameRepository`.
7. Les jeux trouvés sont convertis en `GameDTO`.
8. `GET /jeux/recommandations/{idUtilisateur}` renvoie les jeux recommandés.

## Limites connues

- Spring n'expose pas de route `POST /recommendations/`.
- `RecommandationClient` utilise la route Python
  `GET /recommandations/{idUtilisateur}`.
- Si l'API Python est indisponible, `RecommandationClient.recommander` renvoie
  une liste vide et écrit une erreur dans les logs.
- Les jeux recommandés qui n'existent plus dans la base sont ignorés par
  `GameService`.
