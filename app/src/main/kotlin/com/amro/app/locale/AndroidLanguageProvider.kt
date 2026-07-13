package com.amro.app.locale

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import com.amro.core.common.locale.Language
import com.amro.core.common.locale.LanguageProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AndroidLanguageProvider @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : LanguageProvider,
    ComponentCallbacks {

    private val mutableLanguage = MutableStateFlow(currentLanguage())

    override val language: StateFlow<Language> =
        mutableLanguage.asStateFlow()

    init {
        context.registerComponentCallbacks(this)
    }

    override fun onConfigurationChanged(
        newConfig: Configuration,
    ) {
        mutableLanguage.value = newConfig.currentLanguage()
    }

    @Deprecated("Deprecated in Java")
    override fun onLowMemory() = Unit

    private fun currentLanguage(): Language = context.resources.configuration.currentLanguage()

    private fun Configuration.currentLanguage(): Language {
        val locale = locales.get(0)
        return Language(
            tag = locale.normalizedLanguageTag(),
        )
    }

    private fun Locale.normalizedLanguageTag(): String =
        toLanguageTag().takeIf(String::isNotBlank) ?: language
}