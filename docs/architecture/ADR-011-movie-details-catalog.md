# ADR-011: Provider-neutral movie details with local persistence

## Status
Accepted

## Context
Trending responses contain summary data only. A details screen needs additional data such as runtime, tagline, full genres, status, rating, and backdrop imagery. TMDB exposes these through `GET /3/movie/{movie_id}`. AMRO remains offline-first, provider-aware, and language-aware.

## Decision
- `MovieDetails` is a domain model.
- `MovieRepository` exposes observing and refreshing details by `MovieIdentifier` and language.
- The internal data-layer `MovieProvider` gains a details operation returning `ProviderMovieDetails`.
- TMDB maps its details DTO into that provider-neutral model.
- Details are persisted in Room with identity `(provider, providerMovieId, language)`.
- The repository records successful refresh time with the injected AMRO `Clock`.
- `feature:details` observes Room and requests refresh through use cases.
- Credits and videos are intentionally deferred; TMDB's `append_to_response` can add them later.

## Consequences
Previously viewed details remain available offline. Provider DTOs stay isolated, locale-specific records can coexist, and future providers implement the same internal contract. This adds a Room entity, DAO, migration, mappings, and a separate feature module.
