# eCommerce Config Server

This repository is the **Git-backed configuration store** for the [eCommerce microservices project](https://github.com/KamaliyaVishal/eCommerce). It doesn't contain a running application itself — it holds the externalized `.yaml` configuration files that the Spring Cloud **Config Server** service reads from and serves to every other microservice at startup.

## What This Repo Is For

In a Spring Cloud microservices setup, services don't keep their configuration (ports, database URLs, routes, feature flags, etc.) hardcoded inside their own codebase. Instead:

1. All configuration lives centrally, in this Git repository.
2. The `config-server` application (a separate Spring Boot service) is pointed at this repo as its backing store.
3. Each microservice (API Gateway, Order Service, Inventory Service, etc.) asks the Config Server for its configuration by name when it boots up.
4. Because config is decoupled from code, settings can be changed and versioned here — through normal Git commits — without redeploying the service itself.

## Configuration Files

| File | Used By |
|---|---|
| `application.yaml` | Shared/default configuration common to all services. |
| `api-gateway.yaml` | Configuration specific to the `api-gateway` service (routes, ports, etc.). |
| `inventory-service.yaml` | Configuration specific to the `inventory-service`. |
| `order-service.yaml` | Configuration specific to the `order-service`. |
| `order-service-dev.yaml` | Configuration overrides for the `order-service` when running under the `dev` Spring profile. |

Spring Cloud Config resolves these using the convention `{service-name}-{profile}.yaml`, falling back to `{service-name}.yaml`, and finally to the shared `application.yaml` for anything not overridden.

## How It's Used

The Config Server service (in the main `eCommerce` repo's `config-server` module) is configured to treat this repository as its config source, typically via a `spring.cloud.config.server.git.uri` property pointing here. On startup, the Config Server clones/pulls this repo and exposes each file's contents over HTTP, e.g.:

```
GET http://<config-server-host>:<port>/order-service/dev
GET http://<config-server-host>:<port>/inventory-service/default
```

Each downstream microservice is configured with a `spring.config.import=configserver:` (or the older `bootstrap.yml` `spring.cloud.config.uri`) pointing at the running Config Server, so it automatically pulls the matching file from this repo at startup.

## Making Changes

1. Clone this repo:
   ```bash
   git clone https://github.com/KamaliyaVishal/eCommerce-config-server.git
   ```
2. Edit the relevant `.yaml` file for the service and environment you want to change.
3. Commit and push.
4. Restart (or trigger a refresh on) the Config Server / affected microservices so the new configuration is picked up.

> If Spring Cloud Bus / `/actuator/refresh` is set up, config changes can be picked up by running services without a restart — document that here if enabled.

## Configuring the Main `eCommerce` Repo to Use This Config Store

The `config-server` module inside the [eCommerce](https://github.com/KamaliyaVishal/eCommerce) repo is the actual running Spring Boot application. It needs to be pointed at **this** repository as its Git backend so it knows where to pull configuration from.

In the `config-server` module's `application.yaml` / `application.properties`, add:

```yaml
server:
  port: 8888

spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/KamaliyaVishal/eCommerce-config-server
          default-label: main
          clone-on-start: true
          # For a private repo, add credentials instead:
          # username: <your-github-username>
          # password: <personal-access-token>

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

- `spring.cloud.config.server.git.uri` — points at this repo, so it becomes the single source of truth for configuration.
- `default-label` — the branch to pull from (`main`).
- `clone-on-start` — clones this repo locally as soon as the Config Server boots, instead of waiting for the first request.

### How the Other Services Connect Back

Each downstream service in the main `eCommerce` repo (`api-gateway`, `inventory-service`, `order-service`) then points at the running Config Server itself, e.g. in `application.yaml` or `bootstrap.yaml`:

```yaml
spring:
  config:
    import: "configserver:http://localhost:8888"
  application:
    name: order-service   # must match the file name in this repo, e.g. order-service.yaml
  profiles:
    active: dev            # resolves to order-service-dev.yaml here
```

That `spring.application.name` value is what maps a service to its file in this repository (`order-service` → `order-service.yaml`, then `order-service-dev.yaml` if the `dev` profile is active), and `spring.config.import` is what tells the service to fetch its settings from the Config Server rather than its own local file.

### Startup Order

Because every other service depends on the Config Server being reachable, the recommended startup order across both repos is:

1. `config-server` (from the main `eCommerce` repo) — pulls its configuration source from **this** repo
2. `discovery-service`
3. `api-gateway`
4. `inventory-service`
5. `order-service`

## Related Repositories

- [eCommerce](https://github.com/KamaliyaVishal/eCommerce) — main project, including the `config-server`, `discovery-service`, `api-gateway`, `inventory-service`, and `order-service` modules.

## License

Specify your chosen license here (e.g. MIT, Apache 2.0).

## Contact

**Vishal Kamaliya**
GitHub: [@KamaliyaVishal](https://github.com/KamaliyaVishal)
