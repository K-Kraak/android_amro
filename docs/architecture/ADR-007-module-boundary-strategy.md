# ADR-007: Capability-oriented module boundaries

- Status: Accepted
- Date: 2026-07-10

## Context

Very broad layer modules become ownership and change bottlenecks, while a module per class or entity creates excessive build complexity. Movies, genres, images, provider identities, and catalog metadata belong to one movie capability.

## Decision

Layer boundaries are split by business capability: domain:movie, application:movie, and data:movie. Presentation features are independent modules such as feature:trending. Future capabilities receive equivalent modules only when they represent genuine ownership and change boundaries.

## Consequences

Dependencies are enforceable at compile time and capabilities can evolve independently. The project accepts more Gradle modules than a simple sample app but avoids entity-level fragmentation.
