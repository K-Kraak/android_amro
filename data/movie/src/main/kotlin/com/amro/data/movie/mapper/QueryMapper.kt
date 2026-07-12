package com.amro.data.movie.mapper

import androidx.sqlite.db.SimpleSQLiteQuery
import com.amro.application.movie.model.MovieSortField
import com.amro.application.movie.model.SortDirection
import com.amro.application.movie.model.TrendingQuery

internal fun TrendingQuery.toSQLiteQuery(): SimpleSQLiteQuery {
    val whereClauses = mutableListOf(
        "providerId = ?",
        "language = ?",
    )

    val args = mutableListOf<Any>(
        provider.id,
        language,
    )

    val search = search.trim()

    if (search.isNotEmpty()) {
        whereClauses += "title LIKE ? COLLATE NOCASE"
        args += "%$search%"
    }

    if (genreIds.isNotEmpty()) {
        val genreClauses = genreIds.map {
            "(',' || genreIds || ',') LIKE '%,' || ? || ',%'"
        }

        whereClauses += genreClauses.joinToString(
            separator = " OR ",
            prefix = "(",
            postfix = ")",
        )

        args.addAll(genreIds)
    }

    val sortColumn = when (sortField) {
        MovieSortField.POPULARITY -> "popularity"
        MovieSortField.TITLE -> "title COLLATE NOCASE"
        MovieSortField.RELEASE_DATE -> "releaseDate"
    }

    val sortDirection = when (direction) {
        SortDirection.ASCENDING -> "ASC"
        SortDirection.DESCENDING -> "DESC"
    }

    val sql = buildString {
        append("SELECT * FROM movies")
        append(" WHERE ")
        append(whereClauses.joinToString(" AND "))
        append(" ORDER BY ")
        append(sortColumn)
        append(' ')
        append(sortDirection)
    }

    return SimpleSQLiteQuery(
        query = sql,
        bindArgs = args.toTypedArray(),
    )
}