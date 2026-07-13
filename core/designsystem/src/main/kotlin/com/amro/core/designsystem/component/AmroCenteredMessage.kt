package com.amro.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.amro.core.designsystem.theme.Dimens

@Composable
fun AmroCenteredMessage(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(Dimens.spaceHuge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = Dimens.spaceLarge,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        if (actionLabel != null && onActionClick != null) {
            Button(onClick = onActionClick) { Text(text = actionLabel) }
        }
    }
}
