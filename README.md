# Service de Traitement CSV

## Description
Ce projet implémente un service API REST en Scala avec Akka HTTP pour automatiser le traitement des fichiers CSV de ventes. Il permet la validation des fichiers, la gestion des erreurs et la génération de rapports au format JSON.

## Fonctionnalités
1. **API REST** : Endpoint `GET /api/process` pour traiter les fichiers CSV.
2. **Validation** : Contrôle des en-têtes et des données.
3. **Gestion des erreurs** : Tri des fichiers invalides.
4. **Reporting** : Génération d'un résumé des résultats.

## Structure des Répertoires
```
project/
├── data/
│   ├── input/        # Fichiers CSV à traiter
│   ├── processed/    # Fichiers traités avec succès
│   └── error/        # Fichiers invalides
├── logs/             # Journaux d'exécution
└── src/              # Code source
```

## Démarrage
1. Installer les dépendances avec SBT.
2. Lancer le service :
```bash
sbt run
```
3. Accéder à l'API : `http://localhost:8080/api/process`

## Technologies
- **Scala 2.13.8** : Langage principal.
- **Akka HTTP** : Framework pour l'API REST.
- **Play JSON** : Traitement des données JSON.

## Contributeurs
Merci de contribuer en soumettant des pull requests ou en signalant des issues.

