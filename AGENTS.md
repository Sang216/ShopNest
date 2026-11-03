Follow these team rules when working on the ShopNest backend.

- Use constructor injection only for all services and dependencies.
- Use Lombok `@Getter` and `@Setter` on entities and DTOs.
- Follow conventional commits with a scope, for example `feat(product)`.
- Keep `mvnw` on LF and `mvnw.cmd` on CRLF through `.gitattributes`.
- Run `./mvnw clean compile` and `./mvnw test` before considering the work done.

- Library or cloud docs: ask the Context7 tool first, name the page you used, and check any artifact on Maven Central.
- The catalog MCP server is read-only. Do not add write tools or tokens to it without asking.
- Never paste a secret into a prompt or a note.
