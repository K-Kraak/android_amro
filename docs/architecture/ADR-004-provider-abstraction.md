# ADR-004: Provider Abstraction

## Status

Accepted

## Context

AMRO initially integrates with TMDB but must support additional movie
providers in the near future.

Provider-specific DTOs, authentication, pagination behavior, and API
semantics must not leak into repository contracts or application code.

The MovieProvider abstraction is only needed by the data layer. The
application layer requests catalog operations through MovieRepository
and does not directly communicate with external providers.

## Decision

MovieProvider is an internal abstraction owned by data:movie.

Each provider implementation:

- Implements MovieProvider.
- Maps provider-specific DTOs into provider-neutral data-layer models.
- Owns its API, networking configuration, authentication, DTOs, and mappers.
- Registers itself using Hilt multibinding.

MovieProviderRegistry receives all registered MovieProvider
implementations and resolves one by MovieProviderType.

MovieRepositoryImpl depends on MovieProviderRegistry rather than directly
on TmdbMovieProvider.

Provider-neutral models are not Room entities. MovieRepositoryImpl maps
provider models into provider-aware and language-aware persistence
entities.

## Consequences

### Positive

- Application code is unaware of provider implementation details.
- New providers can be added without modifying MovieRepositoryImpl.
- Provider authentication and DTOs remain isolated.
- Room schema changes do not affect provider contracts.
- External API changes do not directly affect application contracts.

### Negative

- An additional normalized provider model exists between DTOs and Room
  entities.
- Provider resolution happens at runtime.
- Duplicate provider registration must be detected.

## Rejected alternatives

### Provider abstraction in application:movie

Rejected because no application use case needs to call MovieProvider
directly. MovieProvider is an implementation detail used by the data
layer to fulfil MovieRepository.

### Providers returning Room entities

Rejected because this couples external API integrations to the
persistence schema and makes provider implementations aware of Room
storage decisions.