# ADR-010: Atomic Catalog Replacement

## Status

Accepted

## Context

Trending movies and genres are retrieved from an external provider and
persisted locally.

Clearing existing data before all remote pages have been fetched can
leave the catalog empty or partially populated when a request fails.

Writing genres, movies, and catalog metadata independently can also
produce inconsistent persisted state.

Catalog freshness is a business rule evaluated by
TrendingMoviesPolicy. Persistence records when a catalog was
successfully replaced but does not decide how long that catalog remains
valid.

## Decision

A catalog refresh consists of two distinct stages:

1. Fetch and normalize the complete provider catalog in memory.
2. Replace the persisted catalog atomically in a Room transaction.

No persisted data is mutated until all required provider requests
complete successfully.

The transaction replaces, for one provider and language:

- Genres
- Trending movies
- Catalog metadata

Catalog metadata records:

- Provider
- Catalog type
- Language
- Successful replacement timestamp
- Number of fetched pages
- Number of persisted items

The successful replacement timestamp is obtained from the injected AMRO
Clock immediately before the transaction.

TrendingMoviesPolicy uses the same Clock abstraction to determine
whether the recorded timestamp is still fresh.

## Consequences

### Positive

- Failed network requests leave the existing catalog intact.
- Failed database writes are rolled back.
- Genres, movies, and metadata cannot represent different refreshes.
- Time-based behavior is deterministic in tests.
- Persistence remains provider-aware and language-aware.

### Negative

- The complete top-100 catalog is held in memory before persistence.
- Refresh requires one larger transaction.
- The repository needs an internal aggregate representing the fetched
  catalog.

## Rejected alternatives

### Clear before fetching

Rejected because a network failure can destroy valid offline data.

### Persist each page immediately

Rejected because users may observe a partially refreshed catalog and
metadata may not match the stored items.

### Use system time directly

Rejected because calls to Instant.now() or System.currentTimeMillis()
inside business and data logic make time-dependent tests
non-deterministic.