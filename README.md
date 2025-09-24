# Agrilend Backend - Plateforme de Tokenisation Agricole

## Vue d'ensemble

Agrilend est une plateforme innovante de tokenisation des récoltes agricoles utilisant la blockchain Hedera Hashgraph. Cette solution permet aux agriculteurs de tokeniser leurs récoltes, aux acheteurs de passer des commandes sécurisées avec un système de séquestre, et aux administrateurs de gérer l'ensemble du processus de validation et de distribution.

## Architecture

### Composants Principaux

L'architecture suit le diagramme de composants fourni dans les spécifications :

- **Frontend** : Applications React.js (Back Office), Flutter (Mobile), et Site Vitrine
- **Backend** : API Gateway Spring Boot avec services métier
- **Blockchain** : Intégration Hedera Hashgraph (HTS, HSCS, HCS)
- **Base de données** : MySQL pour la persistance des données

### Services Backend

1. **Service Utilisateurs** : Gestion des comptes et authentification
2. **Service Produits** : Catalogue des produits agricoles
3. **Service Commandes** : Gestion des commandes et du séquestre
4. **Service Logistique** : Suivi des livraisons
5. **Service Notifications** : Système de notifications
6. **Service Hedera** : Intégration blockchain complète

## Fonctionnalités Principales

### Pour les Agriculteurs
- Création et gestion des offres de produits
- Livraison des récoltes à l'entrepôt
- Suivi du processus de tokenisation
- Réception des paiements via tokens

### Pour les Acheteurs
- Recherche et consultation des produits disponibles
- Passage de commandes avec paiement sécurisé
- Système de séquestre automatique (3 mois)
- Confirmation de livraison et libération des fonds

### Pour les Administrateurs
- Validation des reçus d'entrepôt
- Gestion du processus de tokenisation
- Supervision des transactions Hedera
- Administration générale de la plateforme

## Processus de Tokenisation

Le processus suit exactement les spécifications des diagrammes de séquence :

### Étape 0 : Livraison à l'entrepôt
L'agriculteur livre sa récolte et reçoit un reçu d'entrepôt avec :
- Numéro de lot unique
- Poids brut et net
- Localisation de stockage
- Grade de qualité
- Hash cryptographique pour l'audit

### Étape 1 : Validation
Un responsable qualité/auditeur valide le reçu après inspection :
- Vérification de la qualité
- Rapport d'inspection
- Signature numérique de l'auditeur

### Étape 2 : Transaction programmée
Création d'une transaction programmée sur Hedera pour le minting :
- Calcul automatique : 1 token = 1 kg de produit
- Création du token HTS si nécessaire
- Programmation de la transaction de minting

### Étape 3 : Signature et exécution
L'administrateur/auditeur signe la transaction programmée :
- Signature de la transaction Hedera
- Exécution automatique du minting
- Enregistrement sur HCS (Consensus Service)

### Étape 4 : Distribution
Distribution des tokens aux parties prenantes :
- Transfert vers les comptes des agriculteurs
- Allocation pour les investisseurs
- Mise à jour des balances

### Étape 5 : Rachat
Les acheteurs peuvent racheter les tokens contre les produits physiques :
- Transfert des tokens vers la trésorerie
- Planification de la livraison
- Libération des produits physiques

## Technologies Utilisées

- **Framework** : Spring Boot 3.2.0
- **Base de données** : MySQL 8.0 / H2 (tests)
- **Blockchain** : Hedera Hashgraph SDK 2.30.0
- **Sécurité** : Spring Security + JWT
- **Documentation** : OpenAPI 3 / Swagger
- **Build** : Maven
- **Java** : OpenJDK 17

## Installation et Configuration

### Prérequis
- Java 17+
- Maven 3.6+
- MySQL 8.0+ (optionnel, H2 inclus pour les tests)

### Configuration Hedera
Configurez les variables d'environnement suivantes :

```properties
HEDERA_NETWORK=testnet
HEDERA_OPERATOR_ACCOUNT_ID=0.0.YOUR_ACCOUNT
HEDERA_OPERATOR_PRIVATE_KEY=YOUR_PRIVATE_KEY
HEDERA_TREASURY_ACCOUNT_ID=0.0.YOUR_TREASURY
```

### Démarrage
```bash
# Cloner le projet
git clone <repository-url>
cd agrilend-backend

# Compiler et démarrer
mvn spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

## Documentation API

### Swagger UI
Accédez à la documentation interactive : `http://localhost:8080/swagger-ui.html`

### Endpoints Principaux

#### Authentification
- `POST /api/auth/register` - Inscription
- `POST /api/auth/login` - Connexion
- `POST /api/auth/refresh` - Renouvellement du token

#### Tokenisation
- `POST /api/tokenization/warehouse-receipts` - Créer un reçu d'entrepôt
- `POST /api/tokenization/warehouse-receipts/{id}/validate` - Valider un reçu
- `POST /api/tokenization/warehouse-receipts/{id}/prepare-mint` - Préparer le minting
- `POST /api/tokenization/scheduled-transactions/{id}/sign` - Signer la transaction
- `GET /api/tokenization/tokens` - Lister les tokens

#### Offres et Commandes
- `GET /api/offers` - Lister les offres
- `POST /api/offers` - Créer une offre
- `POST /api/orders` - Passer une commande
- `GET /api/orders/{id}` - Détails d'une commande

## Tests avec Postman

### Collection Postman Incluse

Le projet inclut une collection Postman complète avec :
- Tests d'authentification
- Scénarios de tokenisation complets
- Tests des commandes avec séquestre
- Validation des intégrations Hedera

### Scénarios de Test

1. **Inscription et Connexion**
   - Créer un compte agriculteur
   - Créer un compte acheteur
   - Obtenir les tokens JWT

2. **Processus de Tokenisation**
   - Créer un reçu d'entrepôt
   - Valider le reçu (admin)
   - Préparer la transaction programmée
   - Signer et exécuter le minting

3. **Commandes et Séquestre**
   - Rechercher des produits
   - Passer une commande
   - Vérifier le séquestre automatique
   - Confirmer la livraison

## Sécurité

### Authentification JWT
- Tokens sécurisés avec HS512
- Expiration configurable (24h par défaut)
- Refresh tokens (7 jours par défaut)

### Autorisation par Rôles
- `FARMER` : Gestion des offres et récoltes
- `BUYER` : Passage de commandes
- `ADMIN` : Administration complète
- `AUDITOR` : Validation et audit

### Sécurité Blockchain
- Clés privées chiffrées
- Transactions signées
- Audit trail complet sur HCS

## Monitoring et Logs

### Actuator Endpoints
- `/actuator/health` - État de santé
- `/actuator/metrics` - Métriques
- `/actuator/info` - Informations système

### Logs Structurés
- Logs détaillés des transactions Hedera
- Traçabilité complète des opérations
- Alertes automatiques en cas d'erreur

## Déploiement

### Environnement de Production
1. Configurer MySQL en production
2. Définir les variables d'environnement Hedera
3. Configurer les certificats SSL
4. Déployer avec Docker (Dockerfile inclus)

### Variables d'Environnement
```bash
DB_USERNAME=agrilend_user
DB_PASSWORD=secure_password
JWT_SECRET=your_jwt_secret_key
HEDERA_NETWORK=mainnet
HEDERA_OPERATOR_ACCOUNT_ID=0.0.production_account
```

## Support et Maintenance

### Logs et Debugging
Les logs sont configurés pour capturer :
- Toutes les transactions Hedera
- Erreurs de validation
- Tentatives d'authentification
- Opérations critiques

### Backup et Récupération
- Sauvegarde automatique de la base de données
- Export des clés Hedera sécurisé
- Procédures de récupération documentées

## Contribution

### Standards de Code
- Respect des conventions Spring Boot
- Tests unitaires obligatoires
- Documentation JavaDoc complète
- Validation des PR par l'équipe

### Roadmap
- [ ] Interface mobile Flutter
- [ ] Intégration IoT pour le suivi des récoltes
- [ ] Marketplace décentralisée
- [ ] Analytics avancées

## Licence

Ce projet est sous licence propriétaire Agrilend. Tous droits réservés.

## Contact

Pour toute question technique ou commerciale :
- Email : dev@agrilend.com
- Documentation : https://docs.agrilend.com
- Support : https://support.agrilend.com

