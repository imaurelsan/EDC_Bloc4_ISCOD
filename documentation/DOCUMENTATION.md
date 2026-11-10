# Documentation de gamesUP

## 1. Architecture

```mermaid
flowchart LR
    Client --> Spring[API Spring Boot]
    Spring --> Base[(Base relationnelle H2)]
    Spring --> Python[API FastAPI]
```

Le projet utilise Spring Boot 3.3.4 et Java 21. Les contrôleurs appellent les
services. Les services utilisent les repositories JPA. Spring appelle l'API
Python avec `RecommandationClient`.

La sécurité prévoit un utilisateur `client` avec le rôle `CLIENT` et un
utilisateur `admin` avec le rôle `ADMIN`. Les deux peuvent consulter les jeux.
Les modifications sont réservées à l'administrateur, selon
`gamesUP/src/main/java/com/gamesUP/gamesUP/config/SecurityConfig.java:35`.

## 2. Diagramme de classes

```mermaid
classDiagram
    Game --> Author
    Game --> Publisher
    Game --> Category
    Purchase --> User
    PurchaseLine --> Purchase
    PurchaseLine --> Game
    User --> Wishlist
    User --> Avis
```

Les classes principales sont `Game`, `Author`, `Publisher`, `Category`, `User`,
`Purchase`, `PurchaseLine`, `Wishlist` et `Avis`. Elles sont des entités JPA,
par exemple dans `gamesUP/src/main/java/com/gamesUP/gamesUP/model/Game.java:9`.

## 3. Composants

```mermaid
flowchart LR
    Controller[Contrôleurs Spring] --> Service[Services Spring]
    Service --> Repository[Repositories JpaRepository]
    Repository --> Database[(Base relationnelle H2)]
    GameService[GameService] --> Client[RecommandationClient]
    Client --> FastAPI[FastAPI Python]
```

La dépendance JPA est déclarée dans
`gamesUP/pom.xml:42`. Les repositories étendent `JpaRepository`, par exemple
`gamesUP/src/main/java/com/gamesUP/gamesUP/repository/GameRepository.java:9`.
La base H2 est déclarée en portée runtime dans `gamesUP/pom.xml:54`.

## 4. Séquence d'une recommandation

```mermaid
sequenceDiagram
    participant C as Client
    participant S as Spring
    participant P as API Python
    C->>S: GET /jeux/recommandations/{idUtilisateur}
    S->>P: GET /recommandations/{idUtilisateur}
    P->>P: Recherche KNN
    P-->>S: Liste d'identifiants de jeux
    S->>S: Recherche dans GameRepository
    S-->>C: Liste de GameDTO
```

La recherche de jeux est disponible avec
`GET /jeux/recherche?nom=...`, dans
`gamesUP/src/main/java/com/gamesUP/gamesUP/controller/GameController.java:27`.

La route Spring est dans
`gamesUP/src/main/java/com/gamesUP/gamesUP/controller/GameController.java:32`.
Le service appelle `RecommandationClient` dans
`gamesUP/src/main/java/com/gamesUP/gamesUP/service/GameService.java:64`.
La route Python est dans `CodeApiPython/main.py:38`.

## 5. Principes SOLID

- **Responsabilité unique** : `GameController` expose les routes et délègue
  la logique à `GameService`, dans
  `gamesUP/src/main/java/com/gamesUP/gamesUP/controller/GameController.java:14`.
- **Ouvert à l'extension** : les repositories utilisent l'interface
  `JpaRepository`, ce qui permet d'utiliser les opérations de base sans
  réécrire le repository, dans
  `gamesUP/src/main/java/com/gamesUP/gamesUP/repository/GameRepository.java:9`.
- **Substitution** : `GameService` dépend du type `GameRepository`, une
  interface Spring Data, dans
  `gamesUP/src/main/java/com/gamesUP/gamesUP/service/GameService.java:14`.
- **Séparation des interfaces** : les repositories sont séparés par entité,
  par exemple `GameRepository` et `AuthorRepository`.
- **Inversion des dépendances** : `GameService` reçoit ses dépendances par
  constructeur dans
  `gamesUP/src/main/java/com/gamesUP/gamesUP/service/GameService.java:17`.

## 6. Couverture des tests

La commande `.\mvnw.cmd clean test jacoco:report` a donné :

- 76 tests exécutés ;
- 0 échec ;
- 0 erreur ;
- 2 tests ignorés ;
- `BUILD SUCCESS`.

Depuis `gamesUP/target/site/jacoco/jacoco.csv` :

- instructions : `1053 / 1338`, soit **78,70 %** ;
- branches : `5 / 6`, soit **83,33 %**.

La couverture est identique à la référence demandée : **78,70 % / 83,33 %**.

## 7. Système de recommandation

Les données nécessaires sont `user_id`, `game_id` et `interaction`. Le fichier
`CodeApiPython/donnees_test.csv` contient des données **fictives** utilisées
pour vérifier le script.

`CodeApiPython/train_model.py:9` construit une matrice utilisateur-jeu,
entraîne `NearestNeighbors` avec la distance cosine et sauvegarde le modèle
dans un fichier pickle.

`CodeApiPython/main.py:12` charge `CodeApiPython/knn_model.pkl`. Si le fichier manque,
les routes de recommandation renvoient HTTP 503 avec le message
« Le modèle de recommandation est indisponible. », dans
`CodeApiPython/main.py:20`.

Le fichier `knn_model.pkl` n'est pas versionné. Il est régénéré avec
`train_model.py` à partir de données fictives.

Les fichiers concernés sont :

- `CodeApiPython/main.py` ;
- `CodeApiPython/recommendation.py` ;
- `CodeApiPython/train_model.py` ;
- `gamesUP/src/main/java/com/gamesUP/gamesUP/service/RecommandationClient.java`.

Limites constatées :

- les données de `donnees_test.csv` sont fictives ;
- le modèle a été produit avec ces données fictives ;
- les jeux recommandés absents de la base Spring sont ignorés dans
  `gamesUP/src/main/java/com/gamesUP/gamesUP/service/GameService.java:64`.

## 8. Lancer l'API Python

Depuis `CodeApiPython/` :

```powershell
py .\train_model.py .\donnees_test.csv .\knn_model.pkl
py -m uvicorn main:app --port 8000
```

Commandes HTTP testées :

```powershell
curl.exe -i http://localhost:8000/
curl.exe -i http://localhost:8000/recommandations/1
curl.exe -i -X POST http://localhost:8000/interactions -H "Content-Type: application/json" --data-binary "@interaction.json"
```

Résultats vérifiés :

- `/` : HTTP 200 ;
- `/recommandations/1` : HTTP 200, réponse `[3,5,4]` ;
- `/interactions` : HTTP 200 ;
- sans `knn_model.pkl`, `/recommandations/1` : HTTP 503 avec un message en français.

## 9. Réflexion

### Bonnes pratiques observées

1. Les contrôleurs délèguent la logique aux services.
2. Les tests de contrôleurs utilisent MockMvc, par exemple
   `gamesUP/src/test/java/com/gamesUP/gamesUP/controller/GameControllerTest.java:32`.
3. Les dépendances des services sont reçues par constructeur.

### Mauvaises pratiques rencontrées

1. Le fichier `.metals/metals.log` était suivi par Git ; il a été retiré du
   suivi et ajouté à `.gitignore`.
2. Le modèle KNN était d'abord entraîné en mémoire au démarrage au lieu d'être
   chargé depuis un fichier.
3. Les données de test KNN sont fictives et ne remplacent pas encore des
   données réelles.
