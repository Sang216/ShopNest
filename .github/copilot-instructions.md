# ShopNest - Copilot Instructions

## Project

Spring Boot 4.1.1 backend (Maven, single module) for a product catalog. Java 17 source level (`pom.xml`), groupId `com.shopnest`, artifact `ProductCatalog`. Entry point: `src/main/java/com/shopnest/productcatalog/ProductCatalogApplication.java`.

- Feature package layout: `com/shopnest/productcatalog/{controller,dto,model,repository,security,service}`.
- README.md is just `# ShopNest` - no docs to consult.

## Commands

- Run: `./mvnw spring-boot:run` (or `mvnw.cmd` on Windows; IDE IntelliJ Run config `ProductCatalogApplication` also exists).
- Build: `./mvnw clean compile`
- Test: `./mvnw test` - only one test exists: `ProductCatalogApplicationTests.contextLoads` (full `@SpringBootTest` context load).

There is no CI, lint, format, or typecheck configuration; verification is effectively compile + that single context test.

## Setup / env required to run or test

`src/main/resources/application.properties` requires environment variables:

- `GROQ_API_KEY` - Groq LLM key (pitch generation).
- `DB_PASSWORD` - MySQL password.
- `AUTH_SERVICE_URL` - auth service base URL (defaults to `http://localhost:8081`).

Datasource: MySQL on `localhost:3306`, database `productcatalog`, user `root`. `spring.jpa.hibernate.ddl-auto=update` (schema auto-generated). `spring.jpa.show-sql=true`.

The `@SpringBootTest` context test will **not pass** without a running local MySQL and these env vars - there is no testcontainers/H2 setup.

## Architecture notes / gotchas

- Service layer: `IProductService` interface with two impls - `StorageProductService` (`@Primary`, JPA-backed) and `DummyProductService` (calls `https://dummyjson.com/products`, registered as `@Qualifier("dummyProductService")`). Injecting `IProductService` yields the storage impl.
- `ProductImportService` copies products from the dummy (supplier) service into storage via `POST /internal/products/import`.
- Soft delete only: `deleteProductById` sets `State.INACTIVE` (no hard deletes). `update`/`patch` operate only on `State.ACTIVE` products; reads filtered by state.
- Product price is `BigDecimal` (`@Column(precision = 19, scale = 3)`); search by price range + pagination via `GET /products?minPrice=&maxPrice=&pageable` returning `PagedModel`.
- `GET /products/{id}/pitch` calls Groq (`openai/gpt-oss-20b` at `api.groq.com/openai/v1/chat/completions`) with a push-notification prompt; falls back to a static message on failure.
- `TokenValidationFilter` (all mutating endpoints require auth): skips `GET`s, requires `Authorization: Bearer <token>`, validates via auth service `${AUTH_SERVICE_URL}/auth/validateToken`; `DELETE` additionally requires `ADMIN` role; returns 503 if auth service is unreachable.

## Conventions

- Lombok (`@Getter/@Setter`) used; constructor injection for services; DTOs with Lombok getters/setters.
- Git commits follow conventional style with scope, e.g. `feat(product): ...`, `fix(...): ...`.
- Line endings: `mvnw` LF, `mvnw.cmd` CRLF (`.gitattributes`).

## Instruction sources

- `.github/copilot-instructions.md` - primary agent guidance (GitHub Copilot)
- `AGENTS.md` / `CLAUDE.md` - mirrors (OpenCode / Claude Code)
- `opencode.json` - repo-local OpenCode config (tool permissions; deletion denied)
- `README.md` - project and architecture overview

Keep these files in sync when project structure, commands, or setup change.
