package com.amro.core.common.time

import java.time.Instant
import javax.inject.Inject

fun interface Clock {
    fun now(): Instant
}

class SystemClock @Inject constructor() : Clock {
    override fun now(): Instant = Instant.now()
}