# ADR-003: Language-aware catalog data

- Status: Accepted
- Date: 2026-07-10

## Context

Titles, overviews, genre names, and potentially image choices vary by requested locale. A movie record fetched for one locale is not universally valid.

## Decision

Locale is part of the identity of localized catalog records and catalog availability metadata. Multiple locales may coexist in persistence.

## Consequences

Changing language can reuse previously obtained data without destructive clearing. Queries and uniqueness constraints must include locale where data is localized.
