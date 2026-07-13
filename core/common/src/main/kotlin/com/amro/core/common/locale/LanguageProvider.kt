package com.amro.core.common.locale

import kotlinx.coroutines.flow.StateFlow

interface LanguageProvider {
    val language: StateFlow<Language>
}