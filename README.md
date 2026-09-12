# eCommerce — Microservices Platform

A Spring Boot / Spring Cloud based e-commerce backend built with a microservices architecture. Each business capability (product inventory, orders, etc.) runs as its own independently deployable service, fronted by an API Gateway and coordinated through service discovery and centralized configuration.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Services](#services)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Services](#running-the-services)
- [Configuration](#configuration)
- [API Gateway Routing](#api-gateway-routing)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Overview

This project demonstrates a microservices-based approach to building an e-commerce system, rather than a single monolithic application. Each service owns its own domain, can be built, deployed, and scaled independently, and communicates with the rest of the system through a discovery layer and a shared configuration server.

## Architecture

```
                         ┌───────────────────┐
                         │   Config Server   │
                         └─────────▲─────────┘
                                   │  fetches shared config
                                   │
        ┌──────────────┐   ┌──────┴───────┐   ┌──────────────────┐
Client ─▶│ API Gateway │──▶│  Discovery  │◀──│  Inventory /     │
        │              │   │  Service     │──▶│  Order Services  │
        └──────────────┘   │  (Eureka)    │   └──────────────────┘
                           └──────────────┘
```

- **Config Server** centralizes configuration for every service so environment-specific settings live in one place instead of being duplicated.
- **Discovery Service** acts as a service registry (Eureka-style), letting services find and call each other by name instead of hardcoded hosts/ports.
- **API Gateway** is the single entry point for external clients, routing requests to the correct downstream service.
- **Inventory Service** and **Order Service** are the business-logic microservices that own their respective domains and data.

## Services

| Service | Responsibility |
|---|---|
| `config-server` | Serves centralized, environment-specific configuration to all other services. |
| `discovery-service` | Service registry that all microservices register with, enabling dynamic service-to-service lookup. |
| `api-gateway` | Single entry point for clients; routes and load-balances requests to the appropriate backend service. |
| `inventory-service` | Manages product catalog and stock levels. |
| `order-service` | Handles order creation, status, and order history. |

> Update this table as services are added, renamed, or split further (e.g. user/auth service, payment service, notification service).

### What each infrastructure service actually does

**`config-server`**
Every microservice needs settings — database URLs, credentials, feature flags, per-environment values (dev/staging/prod), etc. Instead of duplicating that configuration inside each service, the Config Server holds it centrally and hands it out on startup. This means:
- One place to change a setting instead of editing five services
- Different environments (dev/prod) can get different values without changing code
- Services stay "dumb" about where their config comes from — they just ask the Config Server for it

**`discovery-service`**
In a microservices system, services need to call each other (e.g. `order-service` might need to check stock via `inventory-service`), but their network location (host/port) can change — especially if they're scaled up/down or redeployed. The Discovery Service (typically Eureka) solves this by acting as a phone book:
- Every service registers itself here when it starts up ("Hi, I'm `inventory-service`, reach me at this address")
- Other services ask the registry to find each other by name instead of hardcoding IPs/ports
- If a service goes down or a new instance spins up, the registry reflects that automatically

**`api-gateway`**
This is the single front door for the entire system — external clients (a web app, mobile app, Postman, etc.) never talk to `inventory-service` or `order-service` directly. Instead they talk to the API Gateway, which:
- Routes each incoming request to the correct downstream service based on the URL path
- Can apply cross-cutting concerns in one place — authentication, rate limiting, logging, CORS — instead of repeating that logic in every service
- Looks up service locations via the Discovery Service, so it always routes to a live instance

Together, these three form the "plumbing" of the architecture: Config Server feeds settings, Discovery Service lets services find each other, and API Gateway is the single entry point that ties it all together for the outside world.

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot, Spring Cloud (Config, Gateway, Netflix Eureka)
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA (`.idea` project files included)

> Adjust this list to match the exact versions/dependencies declared in each service's `pom.xml`.

## Project Structure

```
eCommerce/
├── .idea/                 # IntelliJ project settings
├── config-server/         # Centralized configuration service
├── discovery-service/     # Eureka-based service registry
├── api-gateway/           # Entry point / request routing
├── inventory-service/     # Product & stock management
├── order-service/         # Order processing
└── README.md
```

## Prerequisites

- Java JDK 11+ (match whatever `java.version` is set in the service `pom.xml` files)
- Maven 3.6+
- An IDE such as IntelliJ IDEA (optional, project already includes `.idea` config)
- A running database instance if any service persists data (e.g. MySQL/PostgreSQL) — update this once each service's `application.yml`/`application.properties` is finalized

## Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/KamaliyaVishal/eCommerce.git
   cd eCommerce
   ```

2. **Build all services**
   ```bash
   mvn clean install
   ```
   Or build each service individually from its own folder:
   ```bash
   cd config-server && mvn clean install && cd ..
   cd discovery-service && mvn clean install && cd ..
   cd api-gateway && mvn clean install && cd ..
   cd inventory-service && mvn clean install && cd ..
   cd order-service && mvn clean install && cd ..
   ```

## Running the Services

Services should be started in this order so that each one can register/fetch configuration correctly:

1. **Config Server** — provides configuration to everything else
   ```bash
   cd config-server && mvn spring-boot:run
   ```
2. **Discovery Service** — service registry
   ```bash
   cd discovery-service && mvn spring-boot:run
   ```
3. **API Gateway**
   ```bash
   cd api-gateway && mvn spring-boot:run
   ```
4. **Inventory Service**
   ```bash
   cd inventory-service && mvn spring-boot:run
   ```
5. **Order Service**
   ```bash
   cd order-service && mvn spring-boot:run
   ```

> Fill in the actual port numbers here once confirmed (e.g. Config Server `:8888`, Discovery Service `:8761`, API Gateway `:8080`, etc.), and add a Eureka dashboard link (typically `http://localhost:8761`) so it's easy to verify all services registered successfully.

## Configuration

Shared and service-specific configuration is managed through the `config-server` module. Each service reads its properties from there on startup rather than bundling all settings locally. Document here:

- Where the config repository/files live (local `config` folder, or a separate Git repo)
- Any required environment variables or secrets (DB credentials, JWT secrets, etc.)
- Active Spring profiles used (`dev`, `prod`, etc.)

## API Gateway Routing

The `api-gateway` is the single entry point for all client requests and forwards them to the correct downstream service by name via the discovery service. Document the actual route mappings here, for example:

| Path | Routed To |
|---|---|
| `/inventory/**` | `inventory-service` |
| `/orders/**` | `order-service` |

## Roadmap

Ideas for extending the platform:

- [ ] User / authentication service (JWT-based login & registration)
- [ ] Payment service integration
- [ ] Cart / checkout service
- [ ] Notification service (email/SMS on order updates)
- [ ] Centralized logging & monitoring (e.g. ELK stack, Prometheus/Grafana)
- [ ] Dockerize each service and add a `docker-compose.yml` for one-command startup
- [ ] CI/CD pipeline (GitHub Actions)

## Contributing

Contributions are welcome. To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes
4. Push to your branch and open a Pull Request

## License

Specify your chosen license here (e.g. MIT, Apache 2.0). If unsure, [choosealicense.com](https://choosealicense.com/) can help pick one, and a `LICENSE` file should be added to the repo root.

## Contact

**Vishal Kamaliya**
GitHub: [@KamaliyaVishal](https://github.com/KamaliyaVishal)
