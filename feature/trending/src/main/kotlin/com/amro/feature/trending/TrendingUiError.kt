package com.amro.feature.trending

sealed interface TrendingUiError {
    data object RefreshFailed : TrendingUiError
}
