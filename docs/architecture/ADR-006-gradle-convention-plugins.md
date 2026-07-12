# ADR-006: Gradle convention plugins

- Status: Accepted
- Date: 2026-07-10

## Context

Many modules require identical Android, Kotlin, Compose, and feature configuration. Copying this setup would create drift and maintenance overhead.

## Decision

Shared build configuration is implemented in an included build under build-logic. Modules apply amro.android.application, amro.android.library, amro.kotlin.library, amro.compose, or amro.feature. Dependencies and plugin versions are declared through the version catalog.

## Consequences

Module build files remain small and declarative. Build logic becomes production code that requires compatibility maintenance when Gradle or AGP changes.
