# ADR-008: Provider-aware catalog model

## Status
Accepted

## Decision
Provider and locale are part of persisted catalog identity. Persistence records catalog observations (`updatedAt`, pages and item count), but it does not own freshness rules. Provider implementations are isolated under `data:movie/provider/<provider>`.

## Consequences
The same logical movie may have distinct provider identifiers. Multiple localized catalogs can coexist. The application policy decides whether an observation is stale using the injected AMRO `Clock`.