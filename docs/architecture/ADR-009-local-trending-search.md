# ADR-009: Local search on Trending

## Status
Accepted

## Decision
The Trending screen searches, filters and sorts the provider/locale-specific top-100 catalog locally. Remote movie search remains a future independent feature module.

## Consequences
Interactions are immediate and offline-capable and do not trigger API calls. Search results are intentionally limited to the Trending catalog.