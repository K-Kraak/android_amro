# ADR-005: Trending catalog freshness policy

- Status: Accepted
- Date: 2026-07-10

## Context

The business requirement states that the top 100 trending catalog becomes outdated after one day. This is a freshness rule, not a Room cache configuration.

## Decision

application:movie owns TrendingMoviesPolicy. It decides whether available catalog data requires refresh based on catalog metadata and an injected AMRO Clock abstraction that supplies the current Instant. The policy never calls system time directly and does not know how data is persisted.

## Consequences

Freshness behavior is deterministic and easy to test. Data implementations must expose enough catalog metadata for the policy without leaking Room entities.
