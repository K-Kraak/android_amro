# AMRO — Advanced Movie Recommendation Organisation

A production-oriented Android reference architecture using capability modules, Clean Architecture principles, Compose, Hilt, Room, Paging, Retrofit and Kotlin Serialization.

## Run
1. Install Android Studio with JDK 17+ and Android SDK 37.
2. Copy `local.properties.example` to `local.properties` and set `sdk.dir` plus `TMDB_API_KEY`.
3. Sync Gradle and run `app`.

The first launch fetches up to five TMDB trending pages (maximum 100 movies). Room is the source of truth. Search, genre filtering and sorting operate locally over the stored provider/locale-specific catalog.

## Architecture
`domain:movie` is framework-free. `application:movie` intentionally exposes `PagingData` and owns repository contracts, use cases and freshness policy. `data:movie` implements provider-aware persistence and the TMDB provider. `feature:trending` owns presentation.

See `docs/architecture` for ADRs and `docs/review-batches.md` for an incremental walkthrough.
