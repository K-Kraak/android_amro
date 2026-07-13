package com.amro.core.common.locale

@JvmInline
value class Language(
    val tag: String,
) {
    init {
        require(tag.isNotBlank())
    }
}