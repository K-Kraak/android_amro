# ADR-001: AndroidX Paging across the application boundary

- Status: Accepted
- Date: 2026-07-10

## Context

Trending movies are delivered through Paging 3 and Room's RemoteMediator. Hiding Paging behind custom abstractions would add adapters throughout the pipeline without creating a meaningful portability benefit. Repository contracts therefore cannot remain framework-neutral.

## Decision

Repository contracts that expose Flow<PagingData<Movie>> live in application:movie. domain:movie remains free from AndroidX and other framework dependencies. feature modules consume the application contract directly.

## Consequences

The application layer depends on AndroidX Paging. AMRO deliberately gives up framework neutrality at this boundary in exchange for a simpler, idiomatic Android pipeline. A future KMP target would require revisiting this decision.
