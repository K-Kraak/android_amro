# AMRO — Advanced Movie Recommendation Organisation

A production-oriented Android reference architecture using capability modules, Clean Architecture principles, Compose, Hilt, Room, Paging, Retrofit and Kotlin Serialization.

It is focussed on a enterprise structure that allows multiple teams to create independent features.
It demonstrates the way I think and would build an application.

In the next sprint to ensure the quality, the focus should be on:
- Additional unit tests
- Defining common UI components (design system) that will be shared over multiple features
- Proper implementation for the TrendingScreen instead of the AI generated UI.
  - Proper use of constants for dimensions and shared UI components
  - Localization in String resources
  - Empty state (Respecting search / loading)
  - Error state with proper error mapping
- The detail page
- UI tests

## Run
1. Install Android Studio with JDK 17+ and Android SDK 37.
2. Copy `tmdb.properties.example` to `tmdb.properties` and set `TMDB_ACCESS_TOKEN`.
3. Sync Gradle and run `app`.

The first launch fetches up to 100 trending movies from TMDB. Room is the source of truth. Search, genre filtering and sorting operate locally over the stored provider/locale-specific catalog.

## Architecture
`domain:movie` is framework-free. `application:movie` intentionally exposes `PagingData` and owns repository contracts, use cases and freshness policy. `data:movie` implements provider-aware persistence and the TMDB provider. `feature:trending` owns presentation.

See `docs/architecture` for relevant ADRs.
