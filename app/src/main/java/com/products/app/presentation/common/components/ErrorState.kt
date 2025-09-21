package com.products.app.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.products.app.R

/**
 * Composable component that displays an error state with optional retry functionality.
 * 
 * This component provides a consistent error display across the app with:
 * - Customizable error icon (defaults to warning icon)
 * - Error title and message text
 * - Optional retry button for user actions
 * - Material Design 3 styling with proper color theming
 * 
 * The component centers content and provides appropriate spacing and typography
 * for clear error communication to users.
 * 
 * @param modifier Modifier to be applied to the component
 * @param icon Custom error icon (uses default warning icon if empty)
 * @param title Error title text (uses default error title if empty)
 * @param message Error message text (uses default error message if empty)
 * @param onRetry Optional callback for retry button (button not shown if null)
 */
@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    icon: String = "",
    title: String = "",
    message: String = "",
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = icon.ifEmpty { stringResource(R.string.icon_warning) },
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = title.ifEmpty { stringResource(R.string.error_title) },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Text(
                text = message.ifEmpty { stringResource(R.string.error_message) },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            onRetry?.let { retry ->
                Button(
                    onClick = retry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.retry))
                }
            }
        }
    }
}
