# ADR-002: Room as the source of truth

- Status: Accepted
- Date: 2026-07-10

## Context

The application must work offline and present one consistent stream of data while remote providers and refresh operations change independently.

## Decision

UI-facing movie streams are read from Room. Provider responses are persisted first; Room then emits the updated catalog. RemoteMediator coordinates paged provider loading and persistence.

## Consequences

Reads remain consistent and offline-capable. Persistence schemas, migrations, and transactional updates become critical production concerns.
